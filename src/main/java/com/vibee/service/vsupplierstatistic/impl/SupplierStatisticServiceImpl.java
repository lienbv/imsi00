package com.vibee.service.vsupplierstatistic.impl;

import com.vibee.entity.VImport;
import com.vibee.entity.VSupplier;
import com.vibee.model.Status;
import com.vibee.model.item.ImportOfSupplierItem;
import com.vibee.model.item.SupplierStatisticItem;
import com.vibee.model.response.supplierstatistic.ImportOfSupplierResponse;
import com.vibee.model.response.supplierstatistic.SupplierStatisticResponse;
import com.vibee.repo.*;
import com.vibee.service.vsupplierstatistic.SupplierStatisService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import io.swagger.models.auth.In;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;


@Log4j2
@Service
public class SupplierStatisticServiceImpl implements SupplierStatisService {

    @Autowired
    private VImportRepo vImportRepo;

    @Autowired
    private VSupplierRepo vSupplierRepo;

    @Autowired
    private VUnitRepo vUnitRepo;

    @Autowired
    private VWarehouseRepo vWarehouseRepo;

    @Autowired
    private VProductRepo vProductRepo;


    @Override
    public SupplierStatisticResponse getAll(String nameSearch, int page, int record) {
        log.info("SupplierStatisticServiceImpl-getAll :: Start");
        Pageable pageable = PageRequest.of(page, record);
        List<VSupplier> suppliers = vSupplierRepo.findByStatusAndNameSup(1, "%"+nameSearch+"%", pageable);
        SupplierStatisticResponse response = new SupplierStatisticResponse();
        List<SupplierStatisticItem> supplierStatistic = new ArrayList<>();
        for (VSupplier supplier : suppliers) {
            SupplierStatisticItem item = new SupplierStatisticItem();
            item.setStatus(supplier.getStatus());
            item.setCreatedDate(supplier.getCreatedDate());
            item.setId(supplier.getId());
            item.setAddress(supplier.getAddress());
            item.setEmail(supplier.getEmail());
            item.setNameSup(supplier.getNameSup());
            item.setNumberPhone(supplier.getNumberPhone());
            item.setStatusName(Utiliies.convertStatusSupplier(supplier.getStatus(), "vi"));
            item.setNumberOfEntry(vImportRepo.getAmountImportsOfSupplier(supplier.getId()));
            supplierStatistic.add(item);
        }

        response.setTotalPages((int) Math.ceil(suppliers.size()/record));
        response.setTotalItems(vSupplierRepo.findByStatusAndNameSupCount(1, "%"+nameSearch+"%"));
        response.setPage(page);
        response.setPageSize(record);
        response.setList(supplierStatistic);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("SupplierStatisticServiceImpl-getAll :: End");
        return response;
    }

    @Override
    public ImportOfSupplierResponse getImportsOfSupplier(int id, int page, int record, String startDate, String endDate, String nameProduct) {
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: Start");
        Pageable pageable = PageRequest.of(page, record);
        List<VImport> imports = new ArrayList<>();
        String nameProductSQL = "%"+nameProduct+"%";
        Date start = Utiliies.formatStringDateNotTime(startDate);
        Date end = Utiliies.formatStringDateNotTime(endDate);
        if (start != null && end != null) {
            Calendar sDate = Calendar.getInstance();
            sDate.setTime(start);
            sDate.set(Calendar.HOUR_OF_DAY, 0);
            sDate.set(Calendar.MINUTE,1);
            sDate.set(Calendar.SECOND,0);
            sDate.set(Calendar.MILLISECOND,0);
            Calendar eDate = Calendar.getInstance();
            eDate.setTime(end);
            eDate.set(Calendar.HOUR_OF_DAY, 23);
            eDate.set(Calendar.MINUTE, 59);
            eDate.set(Calendar.SECOND, 59);
            eDate.set(Calendar.MILLISECOND, 999);

            imports = vImportRepo.getImportsOfSupplier(id, sDate.getTime(), eDate.getTime(), nameProductSQL, pageable);
        } else if (start == null && end == null) {
            imports = vImportRepo.getImportsOfSupplier(id, nameProductSQL,pageable);
        } else if (start == null) {
            Map<String, Calendar> map = this.getStartAndEndDate(end);
            imports = vImportRepo.getImportsOfSupplier(id, map.get("startDate").getTime(), map.get("endDate").getTime(), nameProductSQL,pageable);
        }  else if (end == null) {
            Map<String, Calendar> map = this.getStartAndEndDate(start);
            imports = vImportRepo.getImportsOfSupplier(id, map.get("startDate").getTime(), map.get("endDate").getTime(), nameProductSQL,pageable);
        }

        ImportOfSupplierResponse response = new ImportOfSupplierResponse();
        List<ImportOfSupplierItem> importOfSupplier = new ArrayList<>();

        BigDecimal totalOfPays = BigDecimal.ZERO;

        for (VImport vImport : imports) {
            ImportOfSupplierItem item = new ImportOfSupplierItem();
            item.setCreatedDate(vImport.getCreatedDate());
            item.setProductName(vProductRepo.getById(vWarehouseRepo.getById(vImport.getWarehouseId()).getProductId()).getProductName());
            item.setCreator(vImport.getCreator());
            item.setExpiredDate(vImport.getExpiredDate());
            item.setInMoney(vImport.getInMoney());
            item.setInAmount(vImport.getInAmount());
            item.setProductCode(vImport.getProductCode());
            if (vUnitRepo.findById(vImport.getUnitId()) != null) {
                item.setUnitName(vUnitRepo.findById(vImport.getUnitId()).getUnitName());
            } else {
                item.setUnitName("");
            }
            item.setTotalPurchasePrice(new BigDecimal(vImport.getInAmount()).multiply(vImport.getInMoney()));
            importOfSupplier.add(item);
            totalOfPays = totalOfPays.add(item.getTotalPurchasePrice());
        }

        response.setSupplier(vSupplierRepo.findbyid(id));
        response.setTotalPages((int) Math.ceil(imports.size()/record));
        response.setTotalItems(this.vImportRepo.getImportsOfSupplierCount(id, nameProductSQL
        ));
        response.setPage(page);
        response.setPageSize(record);
        response.setImportsOfSupplier(importOfSupplier);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: End");
        return response;
    }

    @Override
    public ImportOfSupplierResponse getImportLineChart(int year, int id) {
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: Start");
        ImportOfSupplierResponse response = new ImportOfSupplierResponse();
        List<VImport> imports =  vImportRepo.getImportsOfSupplier(id,year);
        BigDecimal totalOfPays = BigDecimal.ZERO;
        List<Integer> lineChart = new ArrayList<>();
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);
        lineChart.add(0);

        for (VImport vImport : imports) {
            addNumber(vImport.getInAmount().intValue(), lineChart, vImport.getCreatedDate().getMonth()+1);
            totalOfPays = totalOfPays.add(new BigDecimal(vImport.getInAmount()).multiply(vImport.getInMoney()));
        }

        Pageable pageable = PageRequest.of(0,5);
        List<Integer> warehouseIds = vImportRepo.getWareHouseId(id, year ,pageable);
        List<String> productName = new ArrayList<>();
        for (int i : warehouseIds) {
            productName.add(vProductRepo.getById(i).getProductName());
        }

        response.setLineChart(lineChart);
        response.setProductName(productName);
        response.setTotalOfEntries(imports.size());
        response.setTotalOfPays(totalOfPays);
        response.setSupplier(vSupplierRepo.findbyid(id));
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: End");
        return response;
    }


    private List<Integer> addNumber(int value, List<Integer> lineChart, int month) {
        switch (month) {
            case 1:
                lineChart.set(0, lineChart.get(0)+value);
                break;
            case 2:
                lineChart.set(1, lineChart.get(1)+value);
                break;
            case 3:
                lineChart.set(2, lineChart.get(2)+value);
                break;
            case 4:
                lineChart.set(3, lineChart.get(3)+value);
                break;
            case 5:
                lineChart.set(4, lineChart.get(4)+value);
                break;
            case 6:
                lineChart.set(5, lineChart.get(5)+value);
                break;
            case 7:
                lineChart.set(6, lineChart.get(6)+value);
                break;
            case 8:
                lineChart.set(7, lineChart.get(7)+value);
                break;
            case 9:
                lineChart.set(8, lineChart.get(8)+value);
                break;
            case 10:
                lineChart.set(9, lineChart.get(9)+value);
                break;
            case 11:
                lineChart.set(10, lineChart.get(10)+value);
                break;
            case 12:
                lineChart.set(11, lineChart.get(11)+value);
                break;
            default:
        }
        return lineChart;
    }

    public Map<String, Calendar> getStartAndEndDate(Date date) {
        Calendar sDate = Calendar.getInstance();
        sDate.setTime(date);
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,0);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);

        Calendar eDate = Calendar.getInstance();
        eDate.setTime(date);
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

        Map<String, Calendar> map = new HashMap<>();
        map.put("startDate", sDate);
        map.put("endDate", eDate);
        return map;
    }
}