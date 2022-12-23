package com.vibee.service.vimport.impl;

import com.aspose.barcode.EncodeTypes;
import com.aspose.barcode.generation.BarcodeGenerator;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vibee.config.redis.RedisAdapter;
import com.vibee.entity.*;
import com.vibee.jedis.ImportInWarehouseRedis;
import com.vibee.model.Status;
import com.vibee.model.info.ImportWarehouseInfor;
import com.vibee.model.item.*;
import com.vibee.model.request.product.InfoCreateProductResponse;
import com.vibee.model.request.v_import.ImportInWarehouse;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.CategoryImportItems;
import com.vibee.model.response.category.ListCategoryImportItems;
import com.vibee.model.response.auth.LoginResponse;
import com.vibee.model.response.category.SelectionTypeProductItems;
import com.vibee.model.response.product.ShowProductByBarcodeResponse;
import com.vibee.model.response.v_import.*;
import com.vibee.repo.*;
import com.vibee.service.vimport.IImportSuppierService;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.DataUtils;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@Log4j2
@Service
public class ImportSupplierServiceImpl implements IImportSuppierService {
    private final VSupplierRepo vSupplierRepo;
    private final VProductRepo vProductRepo;
    private final VTypeProductRepo vTypeProductRepo;
    private final VUnitRepo vUnitRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final VImportRepo vImportRepo;
    private final ImportRedisRepo importRedisRepo;
    private final VWarehouseRepo vWarehouseRepo;
    private final VExportRepo vExportRepo;

    private final HttpServletRequest servletRequest;
    private final RedisAdapter redisAdapter;
    private static final String TOKEN_PREFIX="Bearer ";
    @Autowired
    public ImportSupplierServiceImpl(VSupplierRepo vSupplierRepo, VProductRepo vProductRepo,
                                     VTypeProductRepo vTypeProductRepo, VUnitRepo vUnitRepo, VFileUploadRepo fileUploadRepo,
                                     VImportRepo vImportRepo, ImportRedisRepo importRedisRepo,
                                     VWarehouseRepo vWarehouseRepo, VExportRepo vExportRepo,
                                     HttpServletRequest servletRequest,
                                     RedisAdapter redisAdapter) {
        this.vSupplierRepo = vSupplierRepo;
        this.vProductRepo = vProductRepo;
        this.vTypeProductRepo = vTypeProductRepo;
        this.vUnitRepo = vUnitRepo;
        this.fileUploadRepo = fileUploadRepo;
        this.vImportRepo = vImportRepo;
        this.importRedisRepo = importRedisRepo;
        this.vWarehouseRepo = vWarehouseRepo;
        this.vExportRepo = vExportRepo;
        this.servletRequest = servletRequest;
        this.redisAdapter = redisAdapter;
    }

    @Override
    public BaseResponse importWarehouseOfSupplier(ImportInWarehouse request) {
        BaseResponse response = new BaseResponse();
        ImportInWarehouseRedis importInWarehouse = new ImportInWarehouseRedis();

        String id= request.getId();
        String creator = this.getUserName();
        String barcode = request.getBarCode();
        BigDecimal inPrice = request.getInPrice();
        int typeProductId = request.getCategoryId();
        int inAmount = request.getAmount();
        int unitId = request.getUnitId();
        String language = request.getLanguage();
        String supplierName = request.getSupplierName();
        String productName = request.getNameProd();
        int supplierId = request.getSupplierId();
        String description = request.getDescription();
        String rangeDates = request.getRangeDates();
        String unit = request.getUnit();
        int fileProduct = request.getFileId();
        String idRedis = DataUtils.generateIdRedis(5, 5);
        id = idRedis;
        List<UnitItem> unitItemsRequest = request.getUnits();

        if(inPrice == null|| inPrice==BigDecimal.valueOf(0)){
            importInWarehouse.setInPrice(BigDecimal.valueOf(0));
        }
        importInWarehouse.setUnit(unit);
        importInWarehouse.setId(id);
        importInWarehouse.setSupplierId(supplierId);
        importInWarehouse.setBarcode(barcode);
        importInWarehouse.setInPrice(inPrice);
        importInWarehouse.setTypeProductId(typeProductId);
        importInWarehouse.setInAmount(inAmount);
        importInWarehouse.setUnitId(unitId);
        importInWarehouse.setCreator(creator);
        importInWarehouse.setDescription(description);
        importInWarehouse.setExportsItems(unitItemsRequest);

        try {
            importInWarehouse.setRangeDates(DataUtils.modifyDateLayout(rangeDates));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        importInWarehouse.setProductName(productName);
        importInWarehouse.setSupplierName(supplierName);
        importInWarehouse.setProductFile(fileProduct);
        importInWarehouse.setStatus(1);

        this.importRedisRepo.create(String.valueOf(supplierId), importInWarehouse);

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.create.success"));
        return response;

    }

    @Override
    public BaseResponse update(ImportInWarehouse request, int key, String redisId) {
        BaseResponse response = new BaseResponse();

        String id = request.getId();
        id = redisId;
        ImportInWarehouseRedis importInWarehouse = this.importRedisRepo.get(String.valueOf(key), id);

        String creator = "lienpt";
        String barcode = request.getBarCode();
        BigDecimal inPrice = request.getInPrice();
        int typeProductId = request.getCategoryId();
        int inAmount = request.getAmount();
        int unitId = request.getUnitId();
        String language = request.getLanguage();
        String supplierName = request.getSupplierName();
        String productName = request.getNameProd();
        int supplierId = request.getSupplierId();
        String description = request.getDescription();
        String rangeDates = request.getRangeDates();
        String unit = request.getUnit();


        List<UnitItem> unitItemsRequest = request.getUnits();

        int fileProduct = request.getFileId();

        importInWarehouse.setUnit(unit);
        importInWarehouse.setId(importInWarehouse.getId());
        importInWarehouse.setSupplierId(supplierId);
        importInWarehouse.setBarcode(barcode);
        importInWarehouse.setInPrice(inPrice);
        importInWarehouse.setTypeProductId(typeProductId);
        importInWarehouse.setInAmount(inAmount);
        importInWarehouse.setUnitId(unitId);
        importInWarehouse.setCreator(creator);
        importInWarehouse.setDescription(description);

        try {
            importInWarehouse.setRangeDates(DataUtils.modifyDateLayoutUpdate(rangeDates));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        importInWarehouse.setExportsItems(unitItemsRequest);
        importInWarehouse.setProductName(productName);
        importInWarehouse.setSupplierName(supplierName);
        importInWarehouse.setProductFile(fileProduct);
        importInWarehouse.setStatus(1);

        this.importRedisRepo.update(String.valueOf(supplierId), redisId, importInWarehouse);

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.upload.success"));
        return response;
    }

    @Override
    public EditImportWarehouse edit(int key, String redisId, String language) {
        ImportInWarehouseRedis importInWarehouseRedis = this.importRedisRepo.get(String.valueOf(key), redisId);
        System.out.println(importInWarehouseRedis +" dòng 546");

        EditImportWarehouse infor = new EditImportWarehouse();
        VTypeProduct vTypeProduct = this.vTypeProductRepo.findById(importInWarehouseRedis.getTypeProductId());
        VUnit vUnit = this.vUnitRepo.findById(importInWarehouseRedis.getUnitId());
        if(vTypeProduct == null){
            log.error("Type product is null");
            return null;
        }
        if(vUnit == null){
            log.error("Unit product is null");
            return null;
        }

        infor.setId(redisId);
        infor.setNameProd(importInWarehouseRedis.getProductName());
        infor.setBarCode(importInWarehouseRedis.getBarcode());
        infor.setDescription(importInWarehouseRedis.getDescription());
        infor.setUnits(importInWarehouseRedis.getExportsItems());
        infor.setCategoryId(importInWarehouseRedis.getTypeProductId());
        infor.setInPrice(importInWarehouseRedis.getInPrice());
        infor.setDescription(importInWarehouseRedis.getDescription());
        infor.setUnitId(importInWarehouseRedis.getUnitId());
        infor.setSupplierId(importInWarehouseRedis.getSupplierId());
        infor.setFileId(importInWarehouseRedis.getProductFile());
        infor.setSupplierName(importInWarehouseRedis.getSupplierName());
        infor.setRangeDates(importInWarehouseRedis.getRangeDates());
        infor.setUnit(importInWarehouseRedis.getUnit());
        infor.setCategoryName(vTypeProduct.getName());
        infor.setAmount(importInWarehouseRedis.getInAmount());
        infor.setAmountUnit(vUnit.getAmount());
        infor.setDescription(vUnit.getDescription());
        infor.setUnits(importInWarehouseRedis.getExportsItems());

        return infor;
    }

    public ListImportInWarehouseRedis getAllRedis(int key) {
        ListImportInWarehouseRedis response = new ListImportInWarehouseRedis();
        List<ImportInWarehouseRedis> allList = this.importRedisRepo.getAllList(String.valueOf(key));
        response.setData(allList);
        return response;
    }

    @Override
    public List<ImportWarehouseInfor> getAllProductImportOfSupplier(int key) {
        ListImportWarehouseInforResponse response = new ListImportWarehouseInforResponse();
        List<ImportWarehouseInfor> data = new ArrayList<>();
        List<ImportInWarehouseRedis> allList = this.importRedisRepo.getAllList(String.valueOf(key));

        for (ImportInWarehouseRedis importInWarehouseRedis : allList) {

            ImportWarehouseInfor infor = new ImportWarehouseInfor();
            infor.setImage(importInWarehouseRedis.getImg());
            infor.setId(importInWarehouseRedis.getId());
            infor.setProductName(importInWarehouseRedis.getProductName());
            infor.setBarcode(importInWarehouseRedis.getBarcode());
            infor.setProductId(importInWarehouseRedis.getProductId());
            infor.setDescription(importInWarehouseRedis.getDescription());
            infor.setExportsItems(importInWarehouseRedis.getExportsItems());
            infor.setImage(importInWarehouseRedis.getImg());
            infor.setStatus(importInWarehouseRedis.getStatus());
            infor.setTypeProductId(importInWarehouseRedis.getTypeProductId());
            infor.setInPrice(importInWarehouseRedis.getInPrice());
            infor.setDescription(importInWarehouseRedis.getDescription());
            infor.setUnitId(importInWarehouseRedis.getUnitId());
            infor.setSupplierId(importInWarehouseRedis.getSupplierId());
            infor.setImg(importInWarehouseRedis.getProductFile());
            infor.setCreator(importInWarehouseRedis.getCreator());
            infor.setSupplierName(importInWarehouseRedis.getSupplierName());
            infor.setRangeDate(importInWarehouseRedis.getRangeDates());
            infor.setUnit(importInWarehouseRedis.getUnit());
            infor.setInAmount(importInWarehouseRedis.getInAmount());

            data.add(infor);
        }
        return data;
    }

    @Override
    public ImportWarehouseItemsResponse done(List<ImportWarehouseInfor> data, String language) {
        ImportWarehouseItemsResponse response = new ImportWarehouseItemsResponse();
        List<ImportWarehouseItems> listAll = new ArrayList<>();
        for (ImportWarehouseInfor infor : data) {

            ImportWarehouseItems item = new ImportWarehouseItems();

            VWarehouse vWarehouseNew = new VWarehouse();

            VUnit vUnit = this.vUnitRepo.getByIdChild(infor.getUnitId(), infor.getUnitId());
            VImport vImport = new VImport();

            String qrCode = DataUtils.generateBarcode(1);
            String path = "D:\\vibee_ new\\vibee (1)\\vibee\\src\\assets\\image\\"+qrCode + "qrCode.png";
            File file = new File(path);
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<>();
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            String contendType = "";
            try {
                createQR(qrCode, path, charset, hashMap, 400, 400);
                contendType = Files.probeContentType(file.toPath());
            } catch (WriterException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            VUploadFile uploadFile = new VUploadFile();
            uploadFile.setCreator(this.getUserName());
            uploadFile.setCreatedDate(new Date());
            uploadFile.setFileName(qrCode);
            uploadFile.setSize(BigDecimal.valueOf(file.length()));
            uploadFile.setType(contendType);
            uploadFile.setModifiedDate(new Date());
            uploadFile.setUrl(path);
            uploadFile = this.fileUploadRepo.save(uploadFile);

            VProduct vProduct = this.vProductRepo.findByBarCodeAndStatus(infor.getBarcode(), 1);

            if (vProduct != null) {
                vProduct.setBarCode(vProduct.getBarCode());
                vProduct.setId(vProduct.getId());
                vProduct.setProductName(infor.getProductName());
                vProduct.setProductType(infor.getTypeProductId());
                vProduct.setCreatedDate(vProduct.getCreatedDate());
                vProduct.setStatus(1);
                vProduct.setDescription(infor.getDescription());
                vProduct.setFileId(infor.getImg());
                vProduct.setCreator(infor.getCreator());
                vProduct.setSupplierName(infor.getSupplierName());
                vProduct.setFileId(infor.getImg());
                vProduct.setStatus(1);
                vProduct.setProductType(infor.getTypeProductId());
                vProduct = this.vProductRepo.save(vProduct);

                VWarehouse vWarehouse = this.vWarehouseRepo.findByProductId(vProduct.getId());

                if (vWarehouse != null) {

                    vWarehouse.setId(vWarehouse.getId());
                    vWarehouse.setProductId(vWarehouse.getProductId());
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setCreator(infor.getCreator());

                    Double inPrice = infor.getInPrice().doubleValue();
                    Double oldPrice = vWarehouse.getInPrice().doubleValue();

                    vWarehouse.setInPrice(BigDecimal.valueOf(inPrice + oldPrice));
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setUnitId(vUnit.getId());
                    vWarehouse.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()) + vWarehouse.getInAmount());
                    vWarehouse.setModifiedBy(infor.getCreator());
                    vWarehouse.setModifiedDate(new Date());
                    vWarehouse.setNumberOfEntries(vWarehouse.getNumberOfEntries() + 1);
                    vWarehouse = this.vWarehouseRepo.save(vWarehouse);

                    VImport vImport1 = this.vImportRepo.getVImportBy(vWarehouse.getId());

                    vImport.setWarehouseId(vWarehouse.getId());
                    vImport.setCreatedDate(new Date());
                    vImport.setStatus(1);
                    vImport.setCreator(infor.getCreator());
                    vImport.setInAmount((double) infor.getInAmount());
                    vImport.setInMoney(infor.getInPrice());
                    vImport.setUpdater(0);
                    vImport.setUpdatedDate(new Date());
                    vImport.setSupplierId(infor.getSupplierId());
                    vImport.setSupplierName(infor.getSupplierName());
                    vImport.setUnitId(infor.getUnitId());
                    vImport.setFileId(vProduct.getFileId());
                    vImport.setUrlUpload(uploadFile.getId());
                    vImport.setNumberOfEntries(1);

                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy")
                                .parse(infor.getRangeDate());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    vImport.setExpiredDate(date);
                    vImport.setProductCode(qrCode);
                    vImport.setFileId(uploadFile.getId());
                    vImport.setNumberOfEntries(vImport1.getNumberOfEntries() + 1);

                    this.vImportRepo.save(vImport);

                } else {
                    VWarehouse vWarehouseNew1 = new VWarehouse();
                    vWarehouseNew1.setProductId(vProduct.getId());
                    vWarehouseNew1.setCreator(infor.getCreator());
                    Double inPrice = infor.getInPrice().doubleValue();
                    if(infor.getInPrice()==BigDecimal.valueOf(0) || infor.getInPrice()== null){
                        vWarehouseNew1.setInPrice(BigDecimal.valueOf(0));
                    }
                    vWarehouseNew1.setInPrice(BigDecimal.valueOf(inPrice));
                    vWarehouseNew1.setCreatedDate(new Date());
                    vWarehouseNew1.setUnitId(infor.getUnitId());
                    vWarehouseNew1.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()));
                    vWarehouseNew1.setModifiedBy(infor.getCreator());
                    vWarehouseNew1.setModifiedDate(new Date());
                    vWarehouseNew1.setNumberOfEntries(1);
                    vWarehouseNew1 = this.vWarehouseRepo.save(vWarehouseNew1);

                    vImport.setWarehouseId(vWarehouseNew1.getId());
                    vImport.setCreatedDate(new Date());
                    vImport.setStatus(1);
                    vImport.setCreator(infor.getCreator());
                    vImport.setInAmount((double) infor.getInAmount());
                    vImport.setInMoney(infor.getInPrice());
                    vImport.setUpdater(0);
                    vImport.setUpdatedDate(new Date());
                    vImport.setSupplierId(infor.getSupplierId());
                    vImport.setSupplierName(infor.getSupplierName());
                    vImport.setUnitId(vUnit.getId());
                    vImport.setFileId(vProduct.getFileId());
                    vImport.setUrlUpload(uploadFile.getId());
                    vImport.setNumberOfEntries(1);

                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy")
                                .parse(infor.getRangeDate());
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    vImport.setExpiredDate(date);
                    vImport.setProductCode(qrCode);
                    vImport.setFileId(uploadFile.getId());

                    this.vImportRepo.save(vImport);
                }

                for (UnitItem exportItems : infor.getExportsItems()) {

                    VExport vExport = new VExport();
                    if(exportItems.getOutPrice()== BigDecimal.valueOf(0) || exportItems.getOutPrice() == null){
                        vExport.setOutPrice(BigDecimal.valueOf(0));
                    }
                    vExport.setUnitId(exportItems.getUnitId());
                    vExport.setImportId(vImport.getId());
                    vExport.setOutPrice(exportItems.getOutPrice());
                    vExport.setInPrice(exportItems.getInPrice());
                    vExport.setCreator(infor.getCreator());
                    vExport.setOutAmount(0);
                    vExport.setStatus(1);
                    vExport.setCreatedDate(new Date());
                    this.vExportRepo.save(vExport);

                }
            } else {

                VProduct vProductNew = new VProduct();

                vProductNew.setBarCode(infor.getBarcode());
                vProductNew.setProductName(infor.getProductName());
                vProductNew.setProductType(infor.getTypeProductId());
                vProductNew.setCreatedDate(new Date());
                vProductNew.setStatus(1);
                vProductNew.setDescription(infor.getDescription());
                vProductNew.setFileId(infor.getImg());
                vProductNew.setCreator(infor.getCreator());
                vProductNew.setSupplierName(infor.getSupplierName());

                vProductNew = this.vProductRepo.save(vProductNew);

                vWarehouseNew.setCreator(infor.getCreator());
                Double inPrice = infor.getInPrice().doubleValue();
                if(infor.getInPrice()==BigDecimal.valueOf(0) || infor.getInPrice()== null){
                    vWarehouseNew.setInPrice(BigDecimal.valueOf(0));
                }
                vWarehouseNew.setInPrice(BigDecimal.valueOf(inPrice));
                vWarehouseNew.setCreatedDate(new Date());
                vWarehouseNew.setUnitId(vUnit.getId());
                vWarehouseNew.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()));
                vWarehouseNew.setModifiedBy(infor.getCreator());
                vWarehouseNew.setModifiedDate(new Date());
                vWarehouseNew.setNumberOfEntries(1);
                vWarehouseNew.setProductId(vProductNew.getId());
                vWarehouseNew.setOutAmount(Double.valueOf(0));
                vWarehouseNew.setOutPrice(BigDecimal.valueOf(0));

                vWarehouseNew = this.vWarehouseRepo.save(vWarehouseNew);

                vImport.setWarehouseId(vWarehouseNew.getId());
                vImport.setCreatedDate(new Date());
                vImport.setStatus(1);
                vImport.setCreator(infor.getCreator());
                vImport.setInAmount((double) infor.getInAmount());
                vImport.setInMoney(infor.getInPrice());
                vImport.setUpdater(0);
                vImport.setUpdatedDate(new Date());
                vImport.setSupplierId(infor.getSupplierId());
                vImport.setSupplierName(infor.getSupplierName());
                vImport.setFileId(vProductNew.getFileId());
                vImport.setExpiredDate(new Date());
                vImport.setProductCode(uploadFile.getFileName());
                vImport.setNumberOfEntries(1);
                vImport.setUrlUpload(uploadFile.getId());
                vImport.setUnitId(infor.getUnitId());
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/mm/yyyy")
                            .parse(infor.getRangeDate());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                vImport.setExpiredDate(date);

                vImport = this.vImportRepo.save(vImport);

                for (UnitItem exportItems : infor.getExportsItems()) {
                    if(exportItems.getOutPrice()== BigDecimal.valueOf(0)){
                        return null;
                    }
                    VExport vExport = new VExport();
                    if(exportItems.getOutPrice()== BigDecimal.valueOf(0) || exportItems.getOutPrice() == null){
                        vExport.setOutPrice(BigDecimal.valueOf(0));
                    }
                    vExport.setUnitId(exportItems.getUnitId());
                    vExport.setImportId(vImport.getId());
                    vExport.setOutPrice(exportItems.getOutPrice());
                    vExport.setInPrice(exportItems.getInPrice());
                    vExport.setCreator(infor.getCreator());
                    vExport.setOutAmount(0);
                    vExport.setStatus(1);
                    vExport.setCreatedDate(new Date());
                    this.vExportRepo.save(vExport);

                }

            }
            item.setImportId(vImport.getId());
            item.setAmount(infor.getInAmount());
            item.setInPrice(infor.getInPrice());
            item.setProductName(infor.getProductName());
            item.setRangeDate(new Date(infor.getRangeDate()));
            item.setProductCode(vImport.getProductCode());
            VUnit vUnitId = this.vUnitRepo.findById(vImport.getUnitId());
            item.setUnitName(vUnitId.getUnitName());
            item.setQrCode(uploadFile.getUrl());
            listAll.add(item);

        }
        response.setItems(listAll);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.done-import.success"));
        return response;
    }

    @Override
    public BaseResponse deleteById(int key, String redisId, String language) {
        BaseResponse response = new BaseResponse();
        this.importRedisRepo.delete(String.valueOf(key), redisId);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
        return response;
    }

    @Override
    public BaseResponse deleteAll(int key, String language) {

        BaseResponse response = new BaseResponse();
        this.importRedisRepo.deleteAll(String.valueOf(key));
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));

        return response;
    }

    @Override
    public ShowProductByBarcodeResponse showProductByBarcode(String barcode, String language) {
        ShowProductByBarcodeResponse response = new ShowProductByBarcodeResponse();
        SelectionTypeProductItems responseType = new SelectionTypeProductItems();
        VProduct vProduct = this.vProductRepo.findByBarCodeAndStatus(barcode, 1);
        if (vProduct == null) {
            return response;
        }
        VTypeProduct vTypeProduct = this.vTypeProductRepo.findById(vProduct.getProductType());

        responseType.setId(vTypeProduct.getId());
        responseType.setName(vTypeProduct.getName());
        responseType.setParentId(vTypeProduct.getParentId());

        response.setId(vProduct.getId());
        response.setProductName(vProduct.getProductName());
        response.setSupplierName(response.getSupplierName());
        response.setCreator(this.getUserName());
        response.setStatusCode(vProduct.getStatus());
        response.setBarCode(vProduct.getBarCode());
        response.setCategory(responseType);
        response.setDescription(vProduct.getDescription());
        response.setFileId(vProduct.getFileId());

        return response;
    }
    @Override
    public InfoCreateProductResponse info(String request) {
        log.info("infoService :: Start");
        String language=request;
        InfoCreateProductResponse response=new InfoCreateProductResponse();
        List<VTypeProduct> typeProducts=this.vTypeProductRepo.findByStatus(1);
        List<GetTypeProductItem> typeProductItems=this.convertItem(typeProducts);
        List<VSupplier> suppliers=this.vSupplierRepo.findByStatus(1);
        List<GetSupplierItem> supplierItems=this.convertItems(suppliers);
        List<VUnit> units=this.vUnitRepo.findByStatus(1);
        List<InfoUnitItem> unitItems=this.convertUnitItems(units);
        response.setUnitItems(unitItems);
        response.setItems(supplierItems);
        response.setTypeProductItems(typeProductItems);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        log.info("infoService :: End");
        return response;
    }

    public List<UnitItem> getAllSelectUnitByIdParent(int id, String language) {

        List<UnitItem> response = new ArrayList<>();
        VUnit unit = this.vUnitRepo.findByIdAndStatus(id, 1);
        List<VUnit> vUnits = new ArrayList<>();

        if(unit.getParentId()==0){
            vUnits = this.vUnitRepo.getVUnit(unit.getId(), id, 0);

            for (VUnit vUnit : vUnits) {
                UnitItem unitItem = new UnitItem();
                unitItem.setUnitName(vUnit.getUnitName());
                unitItem.setUnitId(vUnit.getId());
                unitItem.setAmount(vUnit.getAmount());
                unitItem.setInPrice(BigDecimal.valueOf(0));
                unitItem.setOutPrice(BigDecimal.valueOf(0));
                response.add(unitItem);
            }
            return response;
        }else {
            vUnits = this.vUnitRepo.getVUnit(unit.getParentId(), id, unit.getParentId());

            for (VUnit vUnit : vUnits) {
                UnitItem unitItem = new UnitItem();
                if(unit.getAmount() <= vUnit.getAmount()) {
                    unitItem.setUnitName(vUnit.getUnitName());
                    unitItem.setUnitId(vUnit.getId());
                    unitItem.setAmount(vUnit.getAmount());
                    unitItem.setInPrice(BigDecimal.valueOf(0));
                    unitItem.setOutPrice(BigDecimal.valueOf(0));
                    response.add(unitItem);
                }
            }

            return response;
        }


    }
    public List<UnitItem> getAllPrice(int id, BigDecimal inPrice, int amount, String language) {

        List<UnitItem> response = new ArrayList<>();
        VUnit unit = this.vUnitRepo.findByIdAndStatus(id, 1);
        List<VUnit> vUnits = new ArrayList<>();
        vUnits = this.vUnitRepo.getVUnit(unit.getParentId(), id, unit.getParentId());

        if(unit.getParentId()==0){
            vUnits = this.vUnitRepo.getVUnit(unit.getId(), id, 0);

            for (VUnit vUnit : vUnits) {
                Double inprice = (inPrice.doubleValue()/ amount);
                Double inPriceChild = inprice/ vUnit.getAmount();
                UnitItem unitItem = new UnitItem();
                unitItem.setUnitName(vUnit.getUnitName());
                unitItem.setUnitId(vUnit.getId());
                unitItem.setAmount(vUnit.getAmount());
                unitItem.setInPrice(BigDecimal.valueOf(inPriceChild));
                unitItem.setOutPrice(BigDecimal.valueOf(0));
                response.add(unitItem);
            }
            return response;
        }else {
            vUnits = this.vUnitRepo.getVUnit(unit.getParentId(), id, unit.getParentId());

            for (VUnit vUnit : vUnits) {
                Double inprice = (inPrice.doubleValue()/ unit.getAmount());
                Double inPriceChild = inprice/ vUnit.getAmount();
                UnitItem unitItem = new UnitItem();
                if(unit.getAmount() <= vUnit.getAmount()) {
                    unitItem.setUnitName(vUnit.getUnitName());
                    unitItem.setUnitId(vUnit.getId());
                    unitItem.setAmount(vUnit.getAmount());
                    unitItem.setInPrice(BigDecimal.valueOf(inPriceChild));
                    unitItem.setOutPrice(BigDecimal.valueOf(0));
                    response.add(unitItem);
                }
            }

            return response;
        }

    }


    public ListCategoryImportItems getCategory(){
        List<VTypeProduct> findByStatus = this.vTypeProductRepo.findByStatus(1);
        List<CategoryImportItems> items= new ArrayList<>();
        ListCategoryImportItems response = new ListCategoryImportItems();
        for (VTypeProduct vTypeProduct: findByStatus){
            CategoryImportItems categoryImportItems = new CategoryImportItems();
            categoryImportItems.setName(vTypeProduct.getName());
            categoryImportItems.setId(vTypeProduct.getId());
            categoryImportItems.setDescription(vTypeProduct.getDescription());
            items.add(categoryImportItems);
        }
        response.setItems(items);
        return response;
    }

    public static void createQR(String data, String path, String charset, Map hashMap, int height, int width) throws WriterException, IOException {

        BarcodeGenerator generator = new BarcodeGenerator(EncodeTypes.QR, data);
// set resolution
        generator.getParameters().setResolution(400);
// generate barcode
        generator.save(path);
//
//        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);
//
//        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }

    private String getUserName(){
        String token=servletRequest.getHeader("Authorization");
        if (CommonUtil.isEmptyOrNull(token)) {
            return null;
        }
        String key = "expireToken::" + token.substring(TOKEN_PREFIX.length()).hashCode();
        if (Boolean.FALSE.equals(this.redisAdapter.exists(key))) {
            return null;
        }
        LoginResponse loginResponse=this.redisAdapter.get(key, LoginResponse.class);
        String username = loginResponse.getUsername();
        return username;
    }

    private List<GetSupplierItem> convertItems(List<VSupplier> suppliers){
        List<GetSupplierItem> items=new ArrayList<GetSupplierItem>();
        for(VSupplier supplier:suppliers) {
            GetSupplierItem item=new GetSupplierItem();
            item.setId(supplier.getId());
            item.setName(supplier.getNameSup());
            items.add(item);
        }
        return items;
    }

    private List<GetTypeProductItem> convertItem(List<VTypeProduct> typeProducts){

        List<GetTypeProductItem> items=new ArrayList<GetTypeProductItem>();
        for(VTypeProduct product:typeProducts) {
            GetTypeProductItem item=new GetTypeProductItem();
            item.setId(product.getId());
            item.setName(product.getName());
            items.add(item);
        }
        return items;
    }

    private List<InfoUnitItem> convertUnitItems(List<VUnit> units){
        List<InfoUnitItem> unitItems=new ArrayList<>();
        for(VUnit unit:units){
            InfoUnitItem item=new InfoUnitItem();
            item.setParentId(unit.getParentId());
            item.setUnitId(unit.getId());
            item.setAmount(unit.getAmount());
            item.setDescription(unit.getDescription());
            item.setUnitName(unit.getUnitName());
            unitItems.add(item);
        }
        return unitItems;
    }

}
