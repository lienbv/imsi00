package com.vibee.service.vwarehouse.impl;

import com.vibee.entity.VWarehouse;
import com.vibee.model.ObjectResponse.GetExportsObject;
import com.vibee.model.Status;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.v_import.CreateImportResponse;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.model.response.warehouse.ImportWarehouseResponse;
import com.vibee.model.result.ExportResult;
import com.vibee.model.result.GetImportFileExcel;
import com.vibee.model.result.ImportWarehouseResult;
import com.vibee.repo.VExportRepo;
import com.vibee.repo.VProductRepo;
import com.vibee.repo.VWarehouseRepo;
import com.vibee.service.excel.ImportExcel;
import com.vibee.service.vimport.CreateImportProductService;
import com.vibee.service.vwarehouse.CreateWarehouseService;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CreateWarehouseServiceImpl implements CreateWarehouseService {
    private final VWarehouseRepo warehouseRepo;
    private final VProductRepo productRepo;

    private final CreateImportProductService createImportProductService;
    private final ImportExcel importExcel;
    private final VExportRepo exportRepo;
    @Autowired
    public CreateWarehouseServiceImpl(VWarehouseRepo warehouseRepo,
                                      VProductRepo productRepo,
                                      CreateImportProductService createImportProductService,
                                      ImportExcel importExcel,
                                      VExportRepo exportRepo){
        this.warehouseRepo=warehouseRepo;
        this.productRepo=productRepo;
        this.createImportProductService=createImportProductService;
        this.importExcel=importExcel;
        this.exportRepo=exportRepo;
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

    @Override
    public ImportWarehouseResponse importFile(int supplierCode, String language, MultipartFile file) {
        log.info("CreateWarehouseServiceImpl.importFile start importCode:{}",supplierCode);
        ImportWarehouseResponse response=new ImportWarehouseResponse();
        List<ImportWarehouseResult> importWarehouseResults=new ArrayList<>();
        boolean saveImg= Utiliies.uploadFile(file);
        if (Boolean.FALSE.equals(saveImg)){
            log.error("save file fail");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.upload.file"));
            return response;
        }
        String fileName=file.getOriginalFilename();
        String filePath=Utiliies.getFilePath(fileName);
        List<GetImportFileExcel> products=new ArrayList<>();
        try {
            products=this.importExcel.readExcel(filePath);
        } catch (IOException e) {
            log.error("read file fail detail error:{}",e);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.upload.file"));
            return response;
        } catch (ParseException e) {
            log.error("read file fail detail error:{}",e);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.upload.file"));
            return response;
        }
        if (products.isEmpty()){
            log.error("file is empty");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.file.is.empty"));
            return response;
        }

        for (GetImportFileExcel product:products){
            ImportWarehouseResult result=new ImportWarehouseResult();
            List<ExportResult> exportResults=new ArrayList<>();
            result.setBarcode(product.getBarcode());
            result.setInAmount(product.getInAmount());
            result.setInPrice(BigDecimal.valueOf(product.getPrice()));
            result.setSupplierId(supplierCode);
            result.setProductName(product.getNameProduct());
            result.setRangeDates(product.getExpireDate());
            boolean isExist=this.productRepo.existsByBarcode(product.getBarcode());
            if (Boolean.TRUE.equals(isExist)){
                List<GetExportsObject> exports=this.exportRepo.getExportsByBarCode(product.getBarcode());
                for (GetExportsObject export:exports){
                    ExportResult exportResult=new ExportResult();
                    exportResult.setInPrice(export.getInPrice());
                    exportResult.setOutPrice(export.getOutPrice());
                    exportResult.setUnitName(export.getUnitName());
                    exportResult.setUnit(export.getUnit());
                    exportResults.add(exportResult);
                }
                result.setExport(exportResults);
            }
            importWarehouseResults.add(result);
        }
        response.setProducts(importWarehouseResults);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.import.file"));
        response.setSupplierCode(supplierCode);
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
