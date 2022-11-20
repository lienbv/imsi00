package com.vibee.service.vimport.impl;

import com.vibee.entity.VImport;
import com.vibee.entity.VSupplier;
import com.vibee.model.Status;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_export.CreateExportRequest;
import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.export.CreateExportResponse;
import com.vibee.model.response.v_import.CreateImportResponse;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.repo.VImportRepo;
import com.vibee.repo.VSupplierRepo;
import com.vibee.service.vexport.CreateExportService;
import com.vibee.service.vimport.CreateImportProductService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Service
@Log4j2
public class CreateImportProductServiceImpl implements CreateImportProductService {
    private final VSupplierRepo supplierRepo;
    private final VImportRepo importRepo;
    private final CreateExportService createExportService;

    @Autowired
    public CreateImportProductServiceImpl(VSupplierRepo supplierRepo,
                                          VImportRepo importRepo,
                                          CreateExportService createExportService){
        this.supplierRepo=supplierRepo;
        this.importRepo=importRepo;
        this.createExportService=createExportService;
    }

    @Override
    public CreateImportResponse create(CreateImportRequest request) {
        log.info("CreateProductService:: START");
        CreateImportResponse response=new CreateImportResponse();
        String language=request.getLanguage();
        int supplierId=request.getSupplierId();
        List<UnitItem> unitItemsRequest=request.getUnitItems();
        int fileId =request.getFileId();
        int warehouseId=request.getWarehouseId();
        double inAmount=request.getInAmount();
        BigDecimal inPriceRequest=request.getInPrice();
        String creator= "vibeefirst1910";

        VSupplier supplier=this.supplierRepo.getById(supplierId);
        if(supplier==null) {
            log.error("supplier is not already exits, supplierId"+supplierId);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.supplier.not.already.exits"));
            return response;
        }
        VImport vImport=this.convertImport(creator,supplier,fileId,warehouseId,inAmount,inPriceRequest);
        vImport=this.importRepo.save(vImport);
        if(vImport==null) {
            log.error("Create Product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
            return response;
        }
        CreateExportRequest exportRequest=this.convertExportRequest(vImport.getId(),language,unitItemsRequest);
        CreateExportResponse exportResponse=this.createExportService.create(exportRequest);
        if (exportResponse.getStatus().getStatus().equals(Status.Fail)) {
            log.error("Create Detail Product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.detail.product.create.failed"));
            return response;
        }
        response.setImportId(vImport.getId());
        response.getStatus().setMessage(MessageUtils.get(language,"msg.product.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("CreateProductService:: END");
        return response;
    }

    private VImport convertImport(String creator, VSupplier supplier, int fileId, int warehouseId, double intAmount,BigDecimal inPrice){
        VImport vImport=new VImport();
        vImport.setCreator(creator);
        vImport.setSupplierId(supplier.getId());
        vImport.setCreatedDate(new Date());
        vImport.setSupplierName(supplier.getNameSup());
        vImport.setFileId(fileId);
        vImport.setStatus(1);
        vImport.setId(0);
        vImport.setInAmount(intAmount);
        vImport.setInMoney(inPrice);
        vImport.setWarehouseId(warehouseId);
        return vImport;
    }

    private CreateExportRequest convertExportRequest(int importId, String language, List<UnitItem> unitItems){
        CreateExportRequest request=new CreateExportRequest();
        request.setImportId(importId);
        request.setUnitItems(unitItems);
        request.setLanguage(language);
        return request;
    }
}
