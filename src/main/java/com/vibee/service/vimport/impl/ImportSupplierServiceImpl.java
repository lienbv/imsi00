package com.vibee.service.vimport.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.vibee.entity.*;
import com.vibee.jedis.ImportInWarehouseRedis;
import com.vibee.model.ObjectResponse.ImportInWarehouseObject;
import com.vibee.model.Status;
import com.vibee.model.info.ImportWarehouseInfor;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_import.ImportInWarehouse;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.SelectionTypeProductItems;
import com.vibee.model.response.category.SelectionTypeProductItemsResponse;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.ShowProductByBarcodeResponse;
import com.vibee.model.response.redis.*;
import com.vibee.model.response.supplier.ListSupplier;
import com.vibee.model.response.supplier.SupplierResponse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.model.response.v_import.ListImportInWarehouseRedis;
import com.vibee.model.response.v_import.ListImportWarehouseInforResponse;
import com.vibee.repo.*;
import com.vibee.service.vemployee.ITypeProductService;
import com.vibee.service.vproduct.CreateProductService;
import com.vibee.service.vuploadfile.UploadFileService;
import com.vibee.utils.DataUtils;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class ImportSupplierServiceImpl {
    private final VSupplierRepo vSupplierRepo;
    private final ExportRedisRepo exportRedisRepo;
    private final ImportInWarehouseRedisRepo importInWarehouseRedisRepo;
    private final VProductRepo vProductRepo;
    private final VTypeProductRepo vTypeProductRepo;
    private final VUnitRepo vUnitRepo;
    private final ITypeProductService iTypeProductService;
    private final VFileUploadRepo fileUploadRepo;
    private final UploadFileService uploadFileService;
    private final CreateProductService createProductService;
    private final UnitRedisRepo unitRedisRepo;
    private final VImportRepo vImportRepo;
    private final ImportRedisRepo importRedisRepo;
    private final VWarehouseRepo vWarehouseRepo;
    private final VExportRepo vExportRepo;
    @Value("${vibee.url}")
    private static String url;

    @Autowired
    public ImportSupplierServiceImpl(VSupplierRepo vSupplierRepo, ExportRedisRepo exportRedisRepo,
                                     ImportInWarehouseRedisRepo importInWarehouseRedisRepo, VProductRepo vProductRepo,
                                     VTypeProductRepo vTypeProductRepo, VUnitRepo vUnitRepo,
                                     ITypeProductService iTypeProductService, VFileUploadRepo fileUploadRepo,
                                     UploadFileService uploadFileService, CreateProductService createProductService,
                                     UnitRedisRepo unitRedisRepo, VImportRepo vImportRepo, ImportRedisRepo importRedisRepo,
                                     VWarehouseRepo vWarehouseRepo, VExportRepo vExportRepo) {
        this.vSupplierRepo = vSupplierRepo;
        this.exportRedisRepo = exportRedisRepo;
        this.importInWarehouseRedisRepo = importInWarehouseRedisRepo;
        this.vProductRepo = vProductRepo;
        this.vTypeProductRepo = vTypeProductRepo;
        this.vUnitRepo = vUnitRepo;
        this.iTypeProductService = iTypeProductService;
        this.fileUploadRepo = fileUploadRepo;
        this.uploadFileService = uploadFileService;
        this.createProductService = createProductService;
        this.unitRedisRepo = unitRedisRepo;
        this.vImportRepo = vImportRepo;
        this.importRedisRepo = importRedisRepo;
        this.vWarehouseRepo = vWarehouseRepo;
        this.vExportRepo = vExportRepo;
    }

    public BaseResponse importWarehouseOfSupplier(ImportInWarehouse request) {
        BaseResponse response = new BaseResponse();
        ImportInWarehouseRedis importInWarehouse = new ImportInWarehouseRedis();

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

        String idRedis = DataUtils.generateIdRedis(5, 5);
        List<UnitItem> unitItemsRequest=request.getUnits();

        int fileProduct = request.getFileId();

        importInWarehouse.setUnit(unit);
        importInWarehouse.setId(idRedis);
        importInWarehouse.setSupplierId(supplierId);
        importInWarehouse.setBarcode(barcode);
        importInWarehouse.setInPrice(inPrice);
        importInWarehouse.setTypeProductId(typeProductId);
        importInWarehouse.setInAmount(inAmount);
        importInWarehouse.setUnitId(unitId);
        importInWarehouse.setCreator(creator);
        importInWarehouse.setDescription(description);
        try {
            importInWarehouse.setRangeDates(this.modifyDateLayout(rangeDates));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        importInWarehouse.setExportsItems(unitItemsRequest);
        importInWarehouse.setProductName(productName);
        importInWarehouse.setSupplierName(supplierName);
        importInWarehouse.setProductFile(fileProduct);
        importInWarehouse.setStatus(1);

        this.importRedisRepo.create(String.valueOf(supplierId), importInWarehouse);

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.create.success"));
        return response;

    }
    private String modifyDateLayout(String inputDate) throws ParseException{

        Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).parse(inputDate);
        return new SimpleDateFormat("dd/MM/yyyy").format(date);
    }

    public BaseResponse update(ImportInWarehouse request, String redisId, String key){
        BaseResponse response = new BaseResponse();

        ImportInWarehouseRedis importInWarehouse = this.importRedisRepo.get(key, redisId);

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

        String idRedis = DataUtils.generateIdRedis(5, 5);
        List<UnitItem> unitItemsRequest=request.getUnits();

        int fileProduct = request.getFileId();

        importInWarehouse.setUnit(unit);
        importInWarehouse.setId(idRedis);
        importInWarehouse.setSupplierId(supplierId);
        importInWarehouse.setBarcode(barcode);
        importInWarehouse.setInPrice(inPrice);
        importInWarehouse.setTypeProductId(typeProductId);
        importInWarehouse.setInAmount(inAmount);
        importInWarehouse.setUnitId(unitId);
        importInWarehouse.setCreator(creator);
        importInWarehouse.setDescription(description);
        try {
            importInWarehouse.setRangeDates(this.modifyDateLayout(rangeDates));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        importInWarehouse.setExportsItems(unitItemsRequest);
        importInWarehouse.setProductName(productName);
        importInWarehouse.setSupplierName(supplierName);
        importInWarehouse.setProductFile(fileProduct);
        importInWarehouse.setStatus(1);

        this.importRedisRepo.update(String.valueOf(supplierId), importInWarehouse);

        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.upload.success"));
        return response;
    }

    public BaseResponse getAll() {
        BaseResponse response = new BaseResponse();
        List<VSupplier> findAll = this.vSupplierRepo.findByStatus(1);
        List<ImportInWarehouseRedis> allList = new ArrayList<>() ;

        List<ImportInWarehouseRedis> keysList = new ArrayList<>();
        for (VSupplier vSupplier: findAll){
            allList = this.importRedisRepo.getAllList(String.valueOf(vSupplier.getId()));
            Iterator<ImportInWarehouseRedis> it = allList.iterator();

            while (it.hasNext()) {
                ImportInWarehouseRedis data = it.next();
                keysList.add(data);
            }
        }

        List<VProduct> vProductList = new ArrayList<>();
        List<VWarehouse> vWarehouses = new ArrayList<>();
        List<VImport> vImports = new ArrayList<>();
        List<VExport> exports = new ArrayList<>();

        VProduct vProduct = new VProduct();

        ListAllRedis listAllRedis = new ListAllRedis();

        for (ImportInWarehouseRedis importInWarehouseRedis : keysList) {

            ImportInWarehouseRedis getBarcode = this.importRedisRepo.get(String.valueOf(importInWarehouseRedis.getSupplierId()), importInWarehouseRedis.getId());

            System.out.println(getBarcode+" 229");

            vProduct = this.vProductRepo.findByBarCodeAndStatusOrStatus(getBarcode.getBarcode(), 1, 2);

            int maxId = this.vUnitRepo.getMaxIdByParenId(importInWarehouseRedis.getUnitId(), 1);
            VUnit vUnit = this.vUnitRepo.findById(maxId);

            VImport vImport = new VImport();
            if (vProduct != null) {

                vProduct.setBarCode(vProduct.getBarCode());
                vProduct.setId(vProduct.getId());
                vProduct.setProductName(importInWarehouseRedis.getProductName());
                vProduct.setProductType(importInWarehouseRedis.getTypeProductId());
                vProduct.setCreatedDate(new Date());
                vProduct.setStatus(1);
                vProduct.setDescription(importInWarehouseRedis.getDescription());
                vProduct.setFileId(importInWarehouseRedis.getProductFile());
                vProduct.setCreator(importInWarehouseRedis.getCreator());
                vProduct.setSupplierName(importInWarehouseRedis.getSupplierName());

                vProductList.add(vProduct);
                listAllRedis.setProductResponses(vProductList);
                vProduct = this.vProductRepo.save(vProduct);

                VWarehouse vWarehouse = this.vWarehouseRepo.findByProductId(vProduct.getId());

                vWarehouse.setProductId(vProduct.getId());
                vWarehouse.setCreator(importInWarehouseRedis.getCreator());
                Double inPrice = importInWarehouseRedis.getInPrice().doubleValue();
                Double oldPrice = vWarehouse.getInPrice().doubleValue();
                vWarehouse.setInPrice(BigDecimal.valueOf(inPrice + oldPrice));
                vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                vWarehouse.setUnitId(importInWarehouseRedis.getUnitId());
                vWarehouse.setInAmount(Math.floor(importInWarehouseRedis.getInAmount() * vUnit.getAmount()) + vWarehouse.getInAmount());
                vWarehouse.setModifiedBy(importInWarehouseRedis.getCreator());

                vWarehouse = this.vWarehouseRepo.save(vWarehouse);


                vImport.setWarehouseId(vWarehouse.getId());
                vImport.setCreatedDate(new Date());
                vImport.setStatus(1);
                vImport.setCreator(importInWarehouseRedis.getCreator());
                vImport.setInAmount((double) importInWarehouseRedis.getInAmount());
                vImport.setInMoney(importInWarehouseRedis.getInPrice());
                vImport.setUpdater(0);
                vImport.setUpdatedDate(new Date());
                vImport.setSupplierId(importInWarehouseRedis.getSupplierId());
                vImport.setSupplierName(importInWarehouseRedis.getSupplierName());
                vImport.setFileId(importInWarehouseRedis.getImportFile());
                vImport.setExpiredDate(new Date());
                vImport.setProductCode(importInWarehouseRedis.getQrCode());

                vImports.add(vImport);
                listAllRedis.setImportResponses(vImports);
                this.vImportRepo.save(vImport);
//                VImport vImportWarehouse = this.vImportRepo.findByWarehouseIdAndRangeDate(vImport.getWarehouseId(), importInWarehouseRedis.getRangeDates());
//
//                if (vImportWarehouse != null) {
//                    if (vWarehouse.getCreatedDate() == vImport.getCreatedDate()) {
//                        vWarehouse.setNumberOfEntries(vWarehouse.getNumberOfEntries() + 1);
//                    } else {
//                        vWarehouse.setNumberOfEntries(1);
//                    }
//
//                } else {
//                    vWarehouse.setNumberOfEntries(1);
//                }
//                 this.vWarehouseRepo.save(vWarehouse);
                vWarehouses.add(vWarehouse);
                listAllRedis.setWarehouseResponses(vWarehouses);

//                for (ExportItems exportItems : importInWarehouseRedis.getExportsItems()) {
//                    VExport vExport = new VExport();
//                    vExport.setUnitId(exportItems.getUnit());
//                    vExport.setImportId(vImport.getId());
//                    vExport.setOutPrice(exportItems.getOutPrice());
//                    vExport.setInPrice(exportItems.getInPrice());
//                    vExport.setCreator(importInWarehouseRedis.getCreator());
//                    vExport.setOutAmount(0);
//                    vExport.setStatus(1);
//
//                    vExport = this.vExportRepo.save(vExport);
//
//                    exports.add(vExport);
//                    listAllRedis.setExportResponses(exports);
//                }

            } else {
                VProduct vProductNew = new VProduct();

                vProductNew.setBarCode(importInWarehouseRedis.getBarcode());
                vProductNew.setProductName(importInWarehouseRedis.getProductName());
                vProductNew.setProductType(importInWarehouseRedis.getTypeProductId());
                vProductNew.setCreatedDate(new Date());
                vProductNew.setStatus(1);
                vProductNew.setDescription(importInWarehouseRedis.getDescription());
                vProductNew.setFileId(importInWarehouseRedis.getProductFile());
                vProductNew.setCreator(importInWarehouseRedis.getCreator());
                vProductNew.setSupplierName(importInWarehouseRedis.getSupplierName());

                listAllRedis.setProductResponses(vProductList);
                vProductList.add(vProductNew);
                vProductNew = this.vProductRepo.save(vProductNew);

                VWarehouse vWarehouseNew = this.vWarehouseRepo.findByProductId(vProductNew.getId());

                vWarehouseNew.setCreator(importInWarehouseRedis.getCreator());
                Double inPrice = importInWarehouseRedis.getInPrice().doubleValue();
                vWarehouseNew.setInPrice(BigDecimal.valueOf(inPrice));
                vWarehouseNew.setCreatedDate(vWarehouseNew.getCreatedDate());
                vWarehouseNew.setUnitId(importInWarehouseRedis.getUnitId());
                vWarehouseNew.setInAmount(Math.floor(importInWarehouseRedis.getInAmount() * vUnit.getAmount()));
                vWarehouseNew.setModifiedBy(importInWarehouseRedis.getCreator());
                vWarehouseNew.setModifiedDate(new Date());
                vWarehouseNew.setNumberOfEntries(1);

                vWarehouseNew = this.vWarehouseRepo.save(vWarehouseNew);

                vImport.setWarehouseId(vWarehouseNew.getId());
                vImport.setCreatedDate(new Date());
                vImport.setStatus(1);
                vImport.setCreator(importInWarehouseRedis.getCreator());
                vImport.setInAmount((double) importInWarehouseRedis.getInAmount());
                vImport.setInMoney(importInWarehouseRedis.getInPrice());
                vImport.setUpdater(0);
                vImport.setUpdatedDate(new Date());
                vImport.setSupplierId(importInWarehouseRedis.getSupplierId());
                vImport.setSupplierName(importInWarehouseRedis.getSupplierName());
                vImport.setFileId(importInWarehouseRedis.getImportFile());
                vImport.setExpiredDate(new Date());
                vImport.setProductCode(importInWarehouseRedis.getQrCode());
//                vImport.setRangeDate(importInWarehouseRedis.getRangeDates());

                vImport = this.vImportRepo.save(vImport);

//                for (ExportItems exportItems : importInWarehouseRedis.getExportsItems()) {
//                    VExport vExport = new VExport();
//                    vExport.setUnitId(exportItems.getUnit());
//                    vExport.setImportId(vImport.getId());
//                    vExport.setOutPrice(exportItems.getOutPrice());
//                    vExport.setInPrice(exportItems.getInPrice());
//                    vExport.setCreator(importInWarehouseRedis.getCreator());
//                    vExport.setOutAmount(0);
//                    vExport.setStatus(1);
//
//                    vExport = this.vExportRepo.save(vExport);
//
//                    exports.add(vExport);
//                    listAllRedis.setExportResponses(exports);
//                }
            }

        }
        List<ImportInWarehouseRedis> deleteList = new ArrayList<>();
        List<VSupplier> findAllList = this.vSupplierRepo.findAll();
        List<ImportInWarehouseRedis> keysDeleteList = new ArrayList<>();

        for (VSupplier vSupplier: findAll){
            this.importRedisRepo.deleteAll(String.valueOf(vSupplier.getId()));
        }


        return response;
    }

    public ListImportInWarehouseRedis getAllRedis(int key) {
        ListImportInWarehouseRedis response = new ListImportInWarehouseRedis();
        List<ImportInWarehouseRedis> allList = this.importRedisRepo.getAllList(String.valueOf(key));
        response.setData(allList);
        return response;
    }
    public ListImportWarehouseInforResponse getAllProductImportOfSupplier(int key) {
        ListImportWarehouseInforResponse response = new ListImportWarehouseInforResponse();
        List<ImportWarehouseInfor> data = new ArrayList<>();
        List<ImportInWarehouseRedis> allList = this.importRedisRepo.getAllList(String.valueOf(key));

        for (ImportInWarehouseRedis importInWarehouseRedis: allList){

            ImportWarehouseInfor infor = new ImportWarehouseInfor();
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
            data.add(infor);
        }

        response.setData(data);
        return response;
    }
    public ImportWarehouseItemsResponse done(ListImportWarehouseInforResponse data) {
        ImportWarehouseItemsResponse response = new ImportWarehouseItemsResponse();

        for (ImportWarehouseInfor infor: data.getData()) {
            VWarehouse vWarehouseNew = new VWarehouse();
            VProduct vProduct = this.vProductRepo.findByBarCodeAndStatusOrStatus(infor.getBarcode(), 1, 2);
            int maxId = this.vUnitRepo.getMaxIdByParenId(infor.getUnitId(), 1);
            VUnit vUnit = this.vUnitRepo.findById(maxId);
            VImport vImport = new VImport();


            String qrCode = DataUtils.generateBarcode(14);
            String path = qrCode + "qrCode.png";
            File file = new File(path);
            String charset = "UTF-8";
            Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<>();
            hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            String contendType = "";
            try {
                createQR(qrCode, path, charset, hashMap, 200, 200);
                contendType = Files.probeContentType(file.toPath());
            } catch (WriterException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            VUploadFile uploadFile = new VUploadFile();
            uploadFile.setCreator("");
            uploadFile.setCreatedDate(new Date());
            uploadFile.setFileName(file.getName());
            uploadFile.setSize(BigDecimal.valueOf(file.length()));
            uploadFile.setType(contendType);
            uploadFile.setUrl(url + path);

            if (vProduct != null) {

                vProduct.setBarCode(vProduct.getBarCode());
                vProduct.setId(vProduct.getId());
                vProduct.setProductName(infor.getProductName());
                vProduct.setProductType(infor.getTypeProductId());
                vProduct.setCreatedDate(new Date());
                vProduct.setStatus(1);
                vProduct.setDescription(infor.getDescription());
                vProduct.setFileId(infor.getProductId());
                vProduct.setCreator(infor.getCreator());
                vProduct.setSupplierName(infor.getSupplierName());
                vProduct.setFileId(infor.getImg());
                vProduct.setStatus(1);
                vProduct.setProductType(infor.getTypeProductId());
                vProduct = this.vProductRepo.save(vProduct);

                VWarehouse  vWarehouse = this.vWarehouseRepo.findByProductId(vProduct.getId());
                if(vWarehouse !=null){
                    vWarehouse.setId(vWarehouse.getId());
                    vWarehouse.setProductId(vWarehouse.getProductId());
                    vWarehouse.setCreator(infor.getCreator());
                    Double inPrice = infor.getInPrice().doubleValue();
                    Double oldPrice = vWarehouse.getInPrice().doubleValue();
                    vWarehouse.setInPrice(BigDecimal.valueOf(inPrice + oldPrice));
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setUnitId(infor.getUnitId());
                    vWarehouse.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()) + vWarehouse.getInAmount());
                    vWarehouse.setModifiedBy(infor.getCreator());
                    vWarehouse.setNumberOfEntries(vWarehouse.getNumberOfEntries() + 1);
                    vWarehouse = this.vWarehouseRepo.save(vWarehouse);

                }else {
                    vWarehouse = new VWarehouse();
                    vWarehouse.setProductId(vProduct.getId());
                    vWarehouse.setCreator(infor.getCreator());
                    Double inPrice = infor.getInPrice().doubleValue();
                    vWarehouse.setInPrice(BigDecimal.valueOf(inPrice));
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setUnitId(infor.getUnitId());
                    vWarehouse.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()));
                    vWarehouse.setModifiedBy(infor.getCreator());
                    vWarehouse.setNumberOfEntries(vWarehouse.getNumberOfEntries() + 1);

                    vWarehouse = this.vWarehouseRepo.save(vWarehouse);
                }

                uploadFile = this.fileUploadRepo.save(uploadFile);

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

                for (UnitItem exportItems : infor.getExportsItems()) {
                    VExport vExport = new VExport();
                    vExport.setUnitId(exportItems.getUnitId());
                    vExport.setImportId(vImport.getId());
                    vExport.setOutPrice(exportItems.getOutPrice());
                    vExport.setInPrice(exportItems.getInPrice());
                    vExport.setCreator(infor.getCreator());
                    vExport.setOutAmount(0);
                    vExport.setStatus(1);
                    this.vExportRepo.save(vExport);

                }
                response.setImportId(vImport.getId());
                response.setRangeDate(vImport.getExpiredDate());
                response.setUnitName(infor.getUnit());
                response.setProductCode(vProduct.getBarCode());
                response.setAmount(vWarehouse.getInAmount().intValue());
                response.setQrCode(vImport.getProductCode());
                response.setInPrice(vWarehouse.getInPrice());

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
                vWarehouseNew.setInPrice(BigDecimal.valueOf(inPrice));
                vWarehouseNew.setCreatedDate(vWarehouseNew.getCreatedDate());
                vWarehouseNew.setUnitId(infor.getUnitId());
                vWarehouseNew.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()));
                vWarehouseNew.setModifiedBy(infor.getCreator());
                vWarehouseNew.setModifiedDate(new Date());
                vWarehouseNew.setNumberOfEntries(1);

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
                vImport.setFileId(infor.getImg());
                vImport.setExpiredDate(new Date());
                vImport.setProductCode(uploadFile.getFileName());
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/mm/yyyy")
                            .parse(infor.getRangeDate());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                vImport.setExpiredDate(date);

                vImport = this.vImportRepo.save(vImport);

                for (UnitItem itens : infor.getExportsItems()) {
                    VExport vExport = new VExport();
                    vExport.setUnitId(itens.getUnitId());
                    vExport.setImportId(vImport.getId());
                    vExport.setOutPrice(itens.getOutPrice());
                    vExport.setInPrice(itens.getInPrice());
                    vExport.setCreator(infor.getCreator());
                    vExport.setOutAmount(0);
                    vExport.setStatus(1);
                    this.vExportRepo.save(vExport);
                }
                response.setImportId(vImport.getId());
                response.setRangeDate(vImport.getExpiredDate());
                response.setUnitName(infor.getUnit());
                response.setProductCode(vProduct.getBarCode());
                response.setAmount(vWarehouseNew.getInAmount().intValue());
                response.setQrCode(vImport.getProductCode());
                response.setInPrice(vWarehouseNew.getInPrice());
            }

            this.importRedisRepo.deleteAll(String.valueOf(infor.getSupplierId()));
        }
        return response;
    }
    public void getAllRedisAll() {
        List<ImportInWarehouseRedis> keysList  = this.importRedisRepo.getAllList(String.valueOf(1));

            System.out.println(keysList+" dòng 377");

    }

    public BaseResponse deleteById(int key, String redisId, String language){
        this.importRedisRepo.delete(String.valueOf(key), redisId);
        BaseResponse response = new BaseResponse();
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
        return response;
    }

    public BaseResponse deleteAll(int key, String language) {

        BaseResponse response = new BaseResponse();
        this.importRedisRepo.deleteAll(String.valueOf(key));
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));

        return response;
    }

    public ShowProductByBarcodeResponse showProductByBarcode(String barcode, String language) {
        ShowProductByBarcodeResponse response = new ShowProductByBarcodeResponse();
        SelectionTypeProductItems responseType = new SelectionTypeProductItems();
        VProduct vProduct = this.vProductRepo.findByBarCodeAndStatusOrStatus(barcode, 1, 2);
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
        response.setCreator("");
        response.setStatusCode(vProduct.getStatus());
        response.setBarCode(vProduct.getBarCode());
        response.setCategory(responseType);
        response.setDescription(vProduct.getDescription());
        response.setFileId(vProduct.getFileId());

        return response;
    }

    public SupplierResponse getNameAllSupplier(int id) {
        VSupplier vSupplier = this.vSupplierRepo.findByIdAndStatus(id, 1);
        SupplierResponse response = new SupplierResponse();
        response.setId(vSupplier.getId());
        response.setName(vSupplier.getNameSup());
        return response;
    }

    public ListSupplier getAllSupplier() {
        ListSupplier responseList = new ListSupplier();
        List<VSupplier> findByStatus = this.vSupplierRepo.findByStatus(1);
        List<SupplierResponse> list = new ArrayList<>();

        for (VSupplier vSupplier : findByStatus) {
            SupplierResponse response = new SupplierResponse();
            response.setId(vSupplier.getId());
            response.setName(vSupplier.getNameSup());
            list.add(response);
        }
        responseList.setItems(list);
        return responseList;
    }

    public SelectionTypeProductItemsResponse getAllSelect(String language) {

        SelectionTypeProductItemsResponse response = new SelectionTypeProductItemsResponse();
        List<SelectionTypeProductItems> list = new ArrayList<>();
        List<VUnit> vUnits = this.vUnitRepo.findByParentIdAndStatus(0, 1);

        for (VUnit vUnit : vUnits) {
            SelectionTypeProductItems selectionTypeProductItems = new SelectionTypeProductItems();
            selectionTypeProductItems.setId(vUnit.getId());
            selectionTypeProductItems.setName(vUnit.getUnitName());
            selectionTypeProductItems.setParentId(vUnit.getParentId());
            list.add(selectionTypeProductItems);
        }
        response.setData(list);
        return response;
    }

    public SelectionTypeProductItemsResponse getAllSelectUnitByIdParent(int parent, String language) {

        SelectionTypeProductItemsResponse response = new SelectionTypeProductItemsResponse();
        List<SelectionTypeProductItems> list = new ArrayList<>();
        List<VUnit> vUnits = this.vUnitRepo.findByParentIdAndStatus(parent, 1);

        for (VUnit vUnit : vUnits) {
            SelectionTypeProductItems selectionTypeProductItems = new SelectionTypeProductItems();
            selectionTypeProductItems.setId(vUnit.getId());
            selectionTypeProductItems.setName(vUnit.getUnitName());
            selectionTypeProductItems.setParentId(vUnit.getParentId());
            list.add(selectionTypeProductItems);
        }
        response.setData(list);
        return response;
    }

    public CreateProductResponse upload(MultipartFile file, String language) {
        log.info("UploadProductSerivce :: Start");
        CreateProductResponse response = new CreateProductResponse();
        boolean saveImg = Utiliies.uploadFile(file);
        if (saveImg == false) {
            log.error("save file is false");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
            return response;
        }
        VUploadFile uploadFile = new VUploadFile();
        uploadFile.setCreator("");
        uploadFile.setCreatedDate(new Date());
        uploadFile.setFileName(file.getName());
        uploadFile.setSize(BigDecimal.valueOf(file.getSize()));
        uploadFile.setType(file.getContentType());
        uploadFile.setUrl(url + file.getOriginalFilename());
        uploadFile = this.fileUploadRepo.save(uploadFile);
        if (uploadFile == null) {
            log.error("upload file is false");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
            return response;
        }

        response.setId(uploadFile.getId());
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.upload.success"));
        return response;
    }


    public UnitItemsResponse findByIdUnit(int id) {
        UnitItemsResponse response = new UnitItemsResponse();
        VUnit vUnit = this.vUnitRepo.findById(id);
        response.setAmount(vUnit.getAmount());
        response.setId(vUnit.getId());
        response.setName(vUnit.getUnitName());
        response.setParentId(vUnit.getParentId());
        return response;
    }

    public static void createQR(String data, String path, String charset, Map hashMap, int height, int width) throws WriterException, IOException {

        BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(charset), charset), BarcodeFormat.QR_CODE, width, height);

        MatrixToImageWriter.writeToFile(matrix, path.substring(path.lastIndexOf('.') + 1), new File(path));
    }
}
