package com.vibee.service.vwarehouse.impl;

import com.vibee.config.redis.RedisAdapter;
import com.vibee.entity.VUnit;
import com.vibee.entity.VWarehouse;
import com.vibee.model.ObjectResponse.GetExportsObject;
import com.vibee.model.Status;
import com.vibee.model.info.ImportWarehouseInfor;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.v_import.CreateImportResponse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.model.response.warehouse.ImportWarehouseResponse;
import com.vibee.model.result.ExportResult;
import com.vibee.model.result.GetImportFileExcel;
import com.vibee.model.result.GetUnitResult;
import com.vibee.model.result.ImportWarehouseResult;
import com.vibee.repo.*;
import com.vibee.service.excel.ImportExcel;
import com.vibee.service.vimport.CreateImportProductService;
import com.vibee.service.vimport.IImportSuppierService;
import com.vibee.service.vimport.impl.ImportSupplierServiceImpl;
import com.vibee.service.vwarehouse.CreateWarehouseService;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
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
    private final VImportRepo importRepo;
    private final VUnitRepo unitRepo;
    private final RedisAdapter redisAdapter;
    private final IImportSuppierService importSupplierService;

    @Autowired
    public CreateWarehouseServiceImpl(VWarehouseRepo warehouseRepo,
                                      VProductRepo productRepo,
                                      CreateImportProductService createImportProductService,
                                      ImportExcel importExcel,
                                      VExportRepo exportRepo,
                                      VImportRepo importRepo,
                                      VUnitRepo unitRepo,
                                      RedisAdapter redisAdapter, IImportSuppierService importSupplierService) {
        this.warehouseRepo=warehouseRepo;
        this.productRepo=productRepo;
        this.createImportProductService=createImportProductService;
        this.importExcel=importExcel;
        this.exportRepo=exportRepo;
        this.importRepo=importRepo;
        this.unitRepo=unitRepo;
        this.redisAdapter=redisAdapter;
        this.importSupplierService = importSupplierService;
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
        int count=0;
        for (GetImportFileExcel product:products){
            ImportWarehouseResult result=new ImportWarehouseResult();
            List<ExportResult> exportResults=new ArrayList<>();
            result.setBarcode(product.getBarcode());
            result.setInAmount(product.getInAmount());
            result.setInPrice(BigDecimal.valueOf(product.getPrice()));
            result.setSupplierId(supplierCode);
            result.setProductName(product.getNameProduct());
            result.setRangeDates(CommonUtil.convertDateToStringddMMyyyy(product.getExpireDate()));
            result.setId(count);
            count++;
            boolean isExist=this.productRepo.existsByBarcode(product.getBarcode());
            if (Boolean.TRUE.equals(isExist)){
                List<GetExportsObject> exports=this.exportRepo.getExportsByBarCode(product.getBarcode());
                VUnit unit=this.unitRepo.getUnitByBarCode(product.getBarcode());
                for (GetExportsObject export:exports){
                    ExportResult exportResult=new ExportResult();
                    exportResult.setInPrice(export.getInPrice());
                    exportResult.setOutPrice(export.getOutPrice());
                    exportResult.setUnitName(export.getUnitName());
                    exportResult.setUnitId(export.getUnit());
                    exportResults.add(exportResult);
                }
                GetUnitResult unitResult = new GetUnitResult();
                unitResult.setId(unit.getId());
                unitResult.setName(unit.getUnitName());
                unitResult.setStatusCode(unit.getStatus());
                unitResult.setDescription(unit.getDescription());
                unitResult.setParentId(unit.getParentId());
                unitResult.setStatusName(ProductUtils.statusname(unit.getStatus()));
                result.setUnit(unitResult);
                result.setExports(exportResults);
            }
            importWarehouseResults.add(result);
        }
        response.setProducts(importWarehouseResults);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.import.file"));
        response.setSupplierCode(supplierCode);
        return response;
    }

    @Override
    public ImportWarehouseItemsResponse save(int supplierCode, String language) {
        log.info("CreateWarehouseServiceImpl.save start importCode:{}",supplierCode);
        ImportWarehouseItemsResponse response=new ImportWarehouseItemsResponse();
        String key=String.format("import_%s",supplierCode);
        boolean isExist=this.redisAdapter.exists(key);
        if (Boolean.FALSE.equals(isExist)){
            log.error("key is not exist in redis key:{}",key);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.key.is.not.exist"));
            return response;
        }
        List<ImportWarehouseResult> importWarehouseResults= null;
        try {
            importWarehouseResults = this.redisAdapter.gets(key, ImportWarehouseResult.class);
        } catch (IOException e) {
            log.error("get data from redis fail detail error:{}",e);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.get.data.from.redis"));
            return response;
        }
        if (importWarehouseResults.isEmpty()){
            log.error("list is empty");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.list.is.empty"));
            return response;
        }
        //call service save to db
        List<ImportWarehouseInfor> warehousesInfo=new ArrayList<>();
        for (ImportWarehouseResult result:importWarehouseResults){
            ImportWarehouseInfor warehouseInfo=new ImportWarehouseInfor();
            warehouseInfo.setBarcode(result.getBarcode());
            warehouseInfo.setInAmount(result.getInAmount());
            warehouseInfo.setInPrice(result.getInPrice());
            warehouseInfo.setSupplierId(result.getSupplierId());
            warehouseInfo.setProductName(result.getProductName());
            warehouseInfo.setRangeDate(result.getRangeDates());
            List<UnitItem> unitItems=new ArrayList<>();
            for (ExportResult exportResult:result.getExports()){
                UnitItem unitItem=new UnitItem();
                unitItem.setInPrice(exportResult.getInPrice());
                unitItem.setOutPrice(exportResult.getOutPrice());
                unitItem.setUnitName(exportResult.getUnitName());
                unitItem.setUnitId(exportResult.getUnitId());
                unitItems.add(unitItem);
            }
            warehouseInfo.setExportsItems(unitItems);
            warehouseInfo.setUnitId(result.getUnit().getId());
            warehouseInfo.setStatus(1);
            warehouseInfo.setTypeProductId(result.getTypeProduct().getId());
            warehousesInfo.add(warehouseInfo);
        }
        //call service save to db
        response=this.importSupplierService.done(warehousesInfo);

        //delete key in redis
        this.redisAdapter.delete(key);

        //set response and return
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.save"));
        return response;
    }

    @Override
    public ImportWarehouseResponse getWarehouseBySupplier(int supplierCode, String language) {
        log.info("CreateWarehouseServiceImpl.getWarehouseBySupplier start importCode:{}",supplierCode);
        ImportWarehouseResponse response=new ImportWarehouseResponse();
        String key=String.format("import_%s",supplierCode);
        boolean isExist=this.redisAdapter.exists(key);
        if (Boolean.FALSE.equals(isExist)){
            log.error("key is not exist in redis key:{}",key);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.key.is.not.exist"));
            return response;
        }
        List<ImportWarehouseResult> importWarehouseResults= null;
        try {
            importWarehouseResults = this.redisAdapter.gets(key, ImportWarehouseResult.class);
        } catch (IOException e) {
            log.error("get list from redis fail detail error:{}",e);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.get.list.from.redis"));
            return response;
        }
        if (importWarehouseResults.isEmpty()){
            log.error("list is empty");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.list.is.empty"));
            return response;
        }
        response.setProducts(importWarehouseResults);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.get.list"));
        return response;
    }

    @Override
    public BaseResponse saveRedis(ImportWarehouseResponse request, String language) {
        log.info("CreateWarehouseServiceImpl.saveRedis start request:{}",request);
        BaseResponse response=new BaseResponse();
        String key=String.format("import_%s",request.getSupplierCode());
        boolean isExist=this.redisAdapter.exists(key);
        if (Boolean.TRUE.equals(isExist)){
            log.error("key is exist so delete redis by key:{}",key);
            this.redisAdapter.delete(key);
        }
        boolean isAfter=this.redisAdapter.exists(key);
        if (Boolean.FALSE.equals(isAfter)){
            this.redisAdapter.set(key,60*60*24, request.getProducts());
        }
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success.save.redis"));
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
