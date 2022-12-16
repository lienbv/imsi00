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
import com.vibee.model.response.v_import.EditImportWarehouse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.model.response.v_import.ListImportInWarehouseRedis;
import com.vibee.model.response.v_import.ListImportWarehouseInforResponse;
import com.vibee.repo.*;
import com.vibee.service.vimport.IImportSuppierService;
import com.vibee.utils.DataUtils;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Autowired
    public ImportSupplierServiceImpl(VSupplierRepo vSupplierRepo, VProductRepo vProductRepo,
                                     VTypeProductRepo vTypeProductRepo, VUnitRepo vUnitRepo, VFileUploadRepo fileUploadRepo,
                                     VImportRepo vImportRepo, ImportRedisRepo importRedisRepo,
                                     VWarehouseRepo vWarehouseRepo, VExportRepo vExportRepo) {
        this.vSupplierRepo = vSupplierRepo;
        this.vProductRepo = vProductRepo;
        this.vTypeProductRepo = vTypeProductRepo;
        this.vUnitRepo = vUnitRepo;
        this.fileUploadRepo = fileUploadRepo;
        this.vImportRepo = vImportRepo;
        this.importRedisRepo = importRedisRepo;
        this.vWarehouseRepo = vWarehouseRepo;
        this.vExportRepo = vExportRepo;
    }
    @Override
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
            importInWarehouse.setRangeDates(DataUtils.modifyDateLayout(rangeDates));
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
    @Override
    public BaseResponse update(ImportInWarehouse request, int key, String redisId){
        BaseResponse response = new BaseResponse();

        ImportInWarehouseRedis importInWarehouse = this.importRedisRepo.get(String.valueOf(key), redisId);

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
//        String date = null;
//        try {
//            date = new SimpleDateFormat("dd/MM/yyyy")
//                    .format(importInWarehouse.getRangeDates());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
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
    public EditImportWarehouse edit(int key, String redisId, String language){
        List<ImportWarehouseInfor> data = new ArrayList<>();
        ImportInWarehouseRedis importInWarehouseRedis = this.importRedisRepo.get(String.valueOf(key), redisId);

        EditImportWarehouse infor = new EditImportWarehouse();
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

        for (ImportInWarehouseRedis importInWarehouseRedis: allList){

            ImportWarehouseInfor infor = new ImportWarehouseInfor();
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
    public ImportWarehouseItemsResponse done(List<ImportWarehouseInfor> data) {
        ImportWarehouseItemsResponse response = new ImportWarehouseItemsResponse();

        for (ImportWarehouseInfor infor: data) {
            VWarehouse vWarehouseNew = new VWarehouse();
            VProduct vProduct = this.vProductRepo.findByBarCodeAndStatus(infor.getBarcode(), 1);
            VUnit vUnit = this.vUnitRepo.getByIdChild(infor.getUnitId(), infor.getUnitId());
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
            uploadFile.setFileName(qrCode);
            uploadFile.setSize(BigDecimal.valueOf(file.length()));
            uploadFile.setType(contendType);
            uploadFile.setUrl("D:\\be-vibee-dev-new_new\\be-vibee-dev-new_new\\be-vibee-dev-new1\\be-vibee-dev-new\\be-vibee-dev\\be-vibee-dev\\src\\main\\resources" + path);

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
                response.setProductCode(vProduct.getBarCode());

                VWarehouse  vWarehouse = this.vWarehouseRepo.getNumberOfEntries(vProduct.getId());
                VImport vImport1 = new VImport();
                if(vWarehouse !=null){
                    vWarehouse.setId(vWarehouse.getId());
                    vWarehouse.setProductId(vWarehouse.getProductId());
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setCreator(infor.getCreator());
                    Double inPrice = infor.getInPrice().doubleValue();
                    Double oldPrice = vWarehouse.getInPrice().doubleValue();
                    vWarehouse.setInPrice(BigDecimal.valueOf(inPrice + oldPrice));
                    vWarehouse.setCreatedDate(vWarehouse.getCreatedDate());
                    vWarehouse.setUnitId(infor.getUnitId());
                    vWarehouse.setInAmount(Math.floor(infor.getInAmount() * vUnit.getAmount()) + vWarehouse.getInAmount());
                    vWarehouse.setModifiedBy(infor.getCreator());
                    vWarehouse.setModifiedDate(new Date());
                    vWarehouse.setNumberOfEntries(vWarehouse.getNumberOfEntries() + 1);
                    vWarehouse = this.vWarehouseRepo.save(vWarehouse);

                    vImport1  = this.vImportRepo.getVImportBy(vWarehouse.getId());
                    vImport.setNumberOfEntries(vImport1.getNumberOfEntries()+1);


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
                    vWarehouse.setNumberOfEntries(1);

                    vWarehouse = this.vWarehouseRepo.save(vWarehouse);
                    vImport.setNumberOfEntries(1);

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
                vImport.setUnitId(infor.getUnitId());
                vImport.setFileId(uploadFile.getId());
                vImport.setUrlUpload(uploadFile.getFileName());

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
                vImport.setNumberOfEntries(vImport.getNumberOfEntries()+1);
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
                response.setProductCode(vProductNew.getBarCode());
                response.setAmount(vWarehouseNew.getInAmount().intValue());
                response.setQrCode(vImport.getProductCode());
                response.setInPrice(vWarehouseNew.getInPrice());
            }

            this.importRedisRepo.deleteAll(String.valueOf(infor.getSupplierId()));
        }
        return response;
    }
    @Override
    public BaseResponse deleteById(int key, String redisId, String language){
        this.importRedisRepo.delete(String.valueOf(key), redisId);
        BaseResponse response = new BaseResponse();
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

    public List<UnitItem> getAllSelectUnitByIdParent(int parent, String language) {

        List<UnitItem> response = new ArrayList<>();
        List<VUnit> vUnits = this.vUnitRepo.findByParentIdOrIdAndStatus(parent, parent, 1);

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
