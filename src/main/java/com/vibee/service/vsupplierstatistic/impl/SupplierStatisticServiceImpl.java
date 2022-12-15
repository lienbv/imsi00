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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
        List<VSupplier> suppliers = vSupplierRepo.findByStatus(1, pageable);
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
        response.setTotalItems(suppliers.size());
        response.setPage(page);
        response.setPageSize(record);
        response.setList(supplierStatistic);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("SupplierStatisticServiceImpl-getAll :: End");
        return response;
    }

    @Override
    public ImportOfSupplierResponse getImportsOfSupplier(int id, int page, int record) {
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: Start");
        ImportOfSupplierResponse response = new ImportOfSupplierResponse();
        List<ImportOfSupplierItem> importOfSupplier = new ArrayList<>();
        List<VImport> imports =  vImportRepo.getImportsOfSupplier(id);

        for (VImport vImport : imports) {
            ImportOfSupplierItem item = new ImportOfSupplierItem();
            item.setCreatedDate(vImport.getCreatedDate());
            item.setProductName(vProductRepo.getById(vWarehouseRepo.getById(vImport.getWarehouseId()).getProductId()).getProductName());
            item.setCreator(vImport.getCreator());
            item.setExpiredDate(vImport.getExpiredDate());
            item.setInMoney(vImport.getInMoney());
            item.setInAmount(vImport.getInAmount());
            item.setProductCode(vImport.getProductCode());
            item.setUnitName(vUnitRepo.findById(vImport.getUnitId()).getUnitName());
            item.setTotalPurchasePrice(new BigDecimal(vImport.getInAmount()).multiply(vImport.getInMoney()));
            importOfSupplier.add(item);
        }

        response.setTotalPages((int) Math.ceil(imports.size()/record));
        response.setTotalItems(imports.size());
        response.setPage(page);
        response.setPageSize(record);
        response.setImportsOfSupplier(importOfSupplier);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.delete.supplier.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("SupplierStatisticServiceImpl-getImportsOfSupplier :: End");
        return response;
    }
}
