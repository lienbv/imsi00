package com.vibee.service.vwarehouse.impl;

import com.vibee.entity.VWarehouse;
import com.vibee.model.Status;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.v_import.CreateImportResponse;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.repo.VProductRepo;
import com.vibee.repo.VWarehouseRepo;
import com.vibee.service.vimport.CreateImportProductService;
import com.vibee.service.vwarehouse.CreateWarehouseService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CreateWarehouseServiceImpl implements CreateWarehouseService {
    private final VWarehouseRepo warehouseRepo;
    private final VProductRepo productRepo;

    private final CreateImportProductService createImportProductService;
    @Autowired
    public CreateWarehouseServiceImpl(VWarehouseRepo warehouseRepo,
                                      VProductRepo productRepo,
                                      CreateImportProductService createImportProductService){
        this.warehouseRepo=warehouseRepo;
        this.productRepo=productRepo;
        this.createImportProductService=createImportProductService;
    }
    @Override
    public CreateWarehouseResponse create(CreateWarehouseRequest request) {
        String creator="vibeefirst1910";
        String language=request.getLanguage();
        int unitId= request.getUnitId();
        int supplierId=request.getSupplierId();
        int fileId=request.getFileId();
        double inAmount= request.getInAmount();
        BigDecimal inPrice= request.getInPrice();
        List<UnitItem> unitItems=request.getUnitItems();
        CreateWarehouseResponse response=new CreateWarehouseResponse();
        Boolean product=this.productRepo.existsById(request.getProductId());
        if (Boolean.FALSE.equals(product)){
            log.error("product is not exit");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.error.user.is.not.found"));
            return response;
        }
        VWarehouse warehouse=new VWarehouse();
        warehouse.setCreatedDate(new Date());
        warehouse.setCreator(creator);
        warehouse.setInAmount(inAmount);
        warehouse.setOutAmount(request.getOutAmount());
        warehouse.setProductId(request.getProductId());
        warehouse.setInPrice(inPrice);
        warehouse.setOutPrice(request.getOutPrice());
        warehouse.setUnitId(unitId);
        warehouse=this.warehouseRepo.save(warehouse);
        CreateImportRequest importRequest=this.convertImportRequest(unitId, warehouse.getId(),supplierId,fileId,language,unitItems,inPrice,inAmount);
        CreateImportResponse createImportResponse=this.createImportProductService.create(importRequest);
        if (Status.Fail.equals(createImportResponse.getStatus().getStatus())){
            log.error("product is not exit");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.error.user.is.not.found"));
            return response;
        }
        response.setWarehouseId(warehouse.getId());
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.create.warehouse"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    private CreateImportRequest convertImportRequest(int unitId, int warehouseId, int supplierId, int fileId, String language, List<UnitItem> unitItems, BigDecimal inPrice, Double inAmount){
        CreateImportRequest request = new CreateImportRequest();
        request.setUnitId(unitId);
        request.setWarehouseId(warehouseId);
        request.setSupplierId(supplierId);
        request.setFileId(fileId);
        request.setLanguage(language);
        request.setUnitItems(unitItems);
        request.setInPrice(inPrice);
        request.setInAmount(inAmount);
        return request;
    }
}
