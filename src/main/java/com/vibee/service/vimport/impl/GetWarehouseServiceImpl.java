package com.vibee.service.vimport.impl;

import com.vibee.entity.*;
import com.vibee.model.ObjectResponse.*;
import com.vibee.model.Status;
import com.vibee.model.item.*;
import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.warehouse.DetailWarehouseResponse;
import com.vibee.model.response.warehouse.FilterWarehouseResponse;
import com.vibee.model.response.warehouse.GetWarehousesResponse;
import com.vibee.model.result.GetProductResult;
import com.vibee.repo.*;
import com.vibee.service.vimport.GetWarehouseService;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class GetWarehouseServiceImpl implements GetWarehouseService {

    private final VProductRepo productRepo;
    private final VImportRepo importRepo;
    private final VWarehouseRepo warehouseRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final VExportRepo exportRepo;
    private final VUnitRepo unitRepo;
    private final VDetailBillRepo detailBillRepo;

    @Autowired
    public GetWarehouseServiceImpl(VProductRepo productRepo,
                                   VImportRepo importRepo,
                                   VWarehouseRepo warehouseRepo,
                                   VFileUploadRepo fileUploadRepo,
                                   VExportRepo exportRepo,
                                   VUnitRepo unitRepo,
                                   VDetailBillRepo detailBillRepo) {
        this.productRepo=productRepo;
        this.importRepo = importRepo;
        this.warehouseRepo=warehouseRepo;
        this.fileUploadRepo=fileUploadRepo;
        this.exportRepo=exportRepo;
        this.unitRepo=unitRepo;
        this.detailBillRepo=detailBillRepo;
    }
    @Override
    public GetWarehousesResponse getWarehouses(GetWarehouseRequest request) {
        log.info("getWereHouses :: Start");
        GetWarehousesResponse response = new GetWarehousesResponse();
        int productId = request.getProductId();
        FilterItem filterItem=request.getFilterItem();
        String language = request.getLanguage();
        GetProductObject product= this.productRepo.getProductById(productId);

        long totalItems=0;
        if(product==null) {
            log.error("getWereHouses :: Product not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
            return response;
        }
        List<GetCharWarehouseObject> charWarehouses=this.importRepo.getCharWarehouseByProductId(productId);
        if(charWarehouses==null || charWarehouses.size()==0) {
            log.error("getWereHouses :: charWarehouse not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
            return response;
        }
        List<Object> reportEst=this.importRepo.getReportEst(productId);
        if(reportEst.isEmpty()) {
            log.error("getWereHouses :: reportEst not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
            return response;
        }
//        List<GetWarehousesObject> warehouses=this.warehouseRepo.getWarehouseByProductId(productId,pageable);
        List<GetWarehousesObject> warehouses=this.importRepo.getWarehouseByProductId(productId,"v_import.created_date","desc");
        if(warehouses==null || warehouses.size()==0) {
            log.error("getWereHouses :: warehouses not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
            return response;
        }
        totalItems=warehouses.get(0).getCountWarehouse();
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"werehouse.found"));
        response.getPageItem().setPageSize(10);
        response.getPageItem().setPage(1);
        response.getPageItem().setTotalItems((int) totalItems);
        response.getPageItem().setTotalPages((int) totalItems/10);
        response.setGetWarehouseItems(this.convertWarehouse(warehouses,language));
        response.setFilterItem(filterItem);
        response.setGetLastImportItem(this.converReport(reportEst));
        response.setGetProductResult(this.convertWarehouse(product,language));
        response.setGetCharWareHouseItems(this.convertChar(charWarehouses));
        log.info("getWereHouses :: End");
        return response;
    }

    @Override
    public FilterWarehouseResponse filterWarehouses(GetWarehouseRequest request) {
        log.info("getWereHouses :: Start");
        FilterWarehouseResponse response = new FilterWarehouseResponse();
        int productId = request.getProductId();
        FilterItem filterItem=request.getFilterItem();
        String language = request.getLanguage();
        int pageNow=request.getPageItem().getPage();
        int pageSize=request.getPageItem().getPageSize();
        long totalItems=0;
        Pageable pageable=null;
        if(filterItem.getTypeFilter().equals("creator") ||
                filterItem.getTypeFilter().equals("createdDate")||
                filterItem.getTypeFilter().equals("inAmount")||
                filterItem.getTypeFilter().equals("status")||
                filterItem.getTypeFilter().equals("inMoney")
        ){
            if(filterItem.getValueFilter().equals("asc")){
                pageable= PageRequest.of(pageNow, pageSize, Sort.by(filterItem.getTypeFilter()).ascending());
            }else {
                pageable= PageRequest.of(pageNow, pageSize, Sort.by(filterItem.getTypeFilter()).descending());
            }
        }else if(filterItem.getTypeFilter().equals("inventory")
        ){
            if(filterItem.getValueFilter().equals("asc")){
                pageable= PageRequest.of(pageNow, pageSize, JpaSort.unsafe("(sum(w.inAmount)-sum(e.outAmount/un.amount))").ascending());
            }else {
                pageable= PageRequest.of(pageNow, pageSize, JpaSort.unsafe("(sum(w.inAmount)-sum(e.outAmount/un.amount))").descending());
            }
        }else if(filterItem.getTypeFilter().equals("outAmount")){
            if(filterItem.getValueFilter().equals("asc")){
                pageable= PageRequest.of(pageNow, pageSize, JpaSort.unsafe("sum(e.outAmount/un.amount)").ascending());
            }else {
                pageable= PageRequest.of(pageNow, pageSize, JpaSort.unsafe("sum(e.outAmount/un.amount)").descending());
            }
        }

//        List<GetWarehousesObject> warehouses=this.importRepo.getWarehouseByProductId(productId,filterItem.getTypeFilter(),filterItem.getValueFilter());
//        if(warehouses==null || warehouses.size()==0) {
//            log.error("getWereHouses :: warehouses not found");
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language, "product.not.found"));
//            return response;
//        }
//        totalItems=this.importRepo.countWarehouseByProductId(productId);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"werehouse.found"));
        response.getPageItem().setPageSize(pageSize);
        response.getPageItem().setPage(pageNow);
        response.getPageItem().setTotalItems((int) totalItems);
        response.getPageItem().setTotalPages((int) totalItems/pageSize);
//        response.setGetWarehouseItems(this.convertWarehouse(warehouses,language));
        response.setFilterItem(filterItem);
        log.info("getWereHouses :: End");
        return response;
    }

    @Override
    public DetailWarehouseResponse getDetailWarehouse(int importId, String language) {
        DetailWarehouseResponse response=new DetailWarehouseResponse();
        List<GetExportItems> exportItems=new ArrayList<>();
        VImport vImport=this.importRepo.getById(importId);
        response.setExpireDate(CommonUtil.convertDateToStringddMMyyyy(vImport.getExpiredDate()));
        response.setSupplierName(vImport.getSupplierName());
        response.setProductCode(vImport.getProductCode());
        response.setInPrice(vImport.getInMoney());
        response.setInAmount(vImport.getInAmount());
        VUnit unit=this.unitRepo.getById(vImport.getUnitId());
        List<VExport> vExports=this.exportRepo.getExportsByImportId(vImport.getId());
        double outAmount = 0;
        for (VExport vExport:vExports) {
            GetExportItems detailWarehouseItem=new GetExportItems();
            VUnit vUnit=this.unitRepo.getById(vExport.getUnitId());
            detailWarehouseItem.setUnitName(vUnit.getUnitName());
            detailWarehouseItem.setOutPrice(vExport.getOutPrice());
            detailWarehouseItem.setInPrice(vExport.getInPrice());
            detailWarehouseItem.setExportId(vExport.getId());
            if (unit.getId()==vUnit.getId()) {
                outAmount+=vExport.getOutAmount();
            }else{
                outAmount+=(vExport.getOutAmount()*(vUnit.getAmount()/unit.getAmount()));
            }
            exportItems.add(detailWarehouseItem);
        }

        Optional<Long> outPrice=this.detailBillRepo.findBySumAmount(vImport.getId());
        if (outPrice.isPresent()) {
            response.setOutPrice(BigDecimal.valueOf(outPrice.get()));
        }else{
            response.setOutPrice(BigDecimal.valueOf(0));
        }
        response.setOutAmount(outAmount);
        response.setExportItems(exportItems);
        response.setUnitName(unit.getUnitName());
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"werehouse.found"));
        return response;
    }

    @Override
    public BaseResponse updateWarehouse(String language, GetExportItems request) {
        BaseResponse response=new BaseResponse();
        VExport vExport=this.exportRepo.getById(request.getExportId());
        if (vExport==null) {
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"export.not.found"));
            return response;
        }
        vExport.setOutPrice(request.getOutPrice());
        vExport.setInPrice(request.getInPrice());
        this.exportRepo.save(vExport);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"update.export.success"));
        return response;
    }

    private List<WarehouseItem> convertWarehouse(List<GetWarehousesObject> request, String language){
        List<WarehouseItem> warehouses=new ArrayList<>();
        for(GetWarehousesObject o:request){
            WarehouseItem warehouse=new WarehouseItem();
            warehouse.setId(o.getImportId());
            warehouse.setCreator(o.getFullName());
            warehouse.setStatusCode(o.getStatus());
            warehouse.setCreatedDate(CommonUtil.convertDateToStringddMMyyyy(o.getCreatedDate()));
            warehouse.setInAmount(o.getInAmount());
            warehouse.setStatusName(ProductUtils.convertWereHouse(o.getStatus(),language));
            warehouse.setOutAmount(o.getOutAmount());
            warehouse.setInPrice(o.getInPrice());
            warehouse.setUnit(o.getUnitName());
            warehouse.setInventory(o.getInventory());
            warehouse.setProductCode(o.getProductCode());
            warehouse.setExpireDate(CommonUtil.convertDateToStringddMMyyyy(o.getExpireDate()));
            warehouses.add(warehouse);
        }
        return warehouses;
    }

    private GetLastImportItem converReport(List<Object> lst) {
        GetLastImportItem response=new GetLastImportItem();
        Object[] now = (Object[]) lst.get(0);
        double inAmount = (Double) now[0];
        BigDecimal outAmount = (BigDecimal) now[1];
        BigDecimal inPrice = (BigDecimal) now[2];
        BigDecimal outPrice = (BigDecimal) now[3];
        if (outPrice == null) {
            outPrice = new BigDecimal(0);
        }
        if (inPrice == null) {
            inPrice = new BigDecimal(0);
        }
        BigDecimal profit=outPrice.subtract(inPrice);
        response.setOutAmount(outAmount);
        response.setInAmount(inAmount);
        response.setInPrice(inPrice);
        response.setOutPrice(outPrice);
        response.setProfit(profit);
        if(lst.size()<2){
            return response;
        }
        Object[] past = (Object[]) lst.get(1);
        BigDecimal inPriced = (BigDecimal) past[2];
        BigDecimal outPriced = (BigDecimal) past[3];
        if (inPrice.compareTo(BigDecimal.ZERO) == 0) {
            inPriced = new BigDecimal(0);
            response.setCompareInMoney(BigDecimal.ZERO);
        } else {
            BigDecimal compareInMoney=((inPrice.divide(inPriced,2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100))).subtract(BigDecimal.valueOf(100));
            response.setCompareInMoney(compareInMoney);
        }
        if (outPrice.compareTo(BigDecimal.ZERO) == 0) {
            outPriced= new BigDecimal(0);
            response.setCompareOutMoney(BigDecimal.ZERO);
        } else {
            BigDecimal compareOutMoney=((outPrice.divide(outPriced,2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100))).subtract(BigDecimal.valueOf(100));
            response.setCompareOutMoney(compareOutMoney);
        }
        BigDecimal profited=outPriced.subtract(inPriced);
        if (outPrice.compareTo(BigDecimal.ZERO) == 0){
            response.setCompareProfit(BigDecimal.ZERO);
        }else{
            BigDecimal compareProfit=(profit.divide(profited,2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100)).subtract(BigDecimal.valueOf(100));
            response.setCompareProfit(compareProfit);
        }

        return response;
    }

    private GetProductResult convertWarehouse(GetProductObject product, String language){
        GetProductResult response=new GetProductResult();
        response.setProductId(product.getProductId());
        response.setProductName(product.getProductName());
        response.setSumInAmount(product.getInAmount());
        response.setSumOutAmount(product.getOutAmount());
        response.setInventory(product.getInAmount()-product.getOutAmount());
        response.setStatusCode(product.getStatus());
        response.setStatusName(ProductUtils.getstatusname(product.getStatus(),language));
        response.setImg(this.fileUploadRepo.getURLById(product.getFileId()));
        response.setCountImported(product.getCountWarehouse());
        response.setSumInPrice(product.getInPrice());
        response.setSumOutPrice(product.getOutPrice());
        response.setProfit(product.getOutPrice().subtract(product.getInPrice()));
        return response;
    }

    private List<GetCharWareHouseItem> convertChar(List<GetCharWarehouseObject> lst) {
        List<GetCharWareHouseItem> responses=new ArrayList<>();
        for (GetCharWarehouseObject o:lst){
            GetCharWareHouseItem response = new GetCharWareHouseItem();
            response.setCreateDate(CommonUtil.convertDateToStringddMMyyyy(o.getCreatedDate()));
            response.setOutAmount(o.getOutAmount());
            response.setInAmount(o.getInAmount());
            response.setInPrice(o.getInPrice());
            response.setOutPrice(o.getOutPrice());
            responses.add(response);
        }
        return responses;
    }
}
