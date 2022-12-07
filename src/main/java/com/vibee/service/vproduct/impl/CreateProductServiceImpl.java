package com.vibee.service.vproduct.impl;

import com.vibee.config.redis.RedisAdapter;
import com.vibee.entity.*;
import com.vibee.jedis.CreateProduct;
import com.vibee.jedis.Update;
import com.vibee.model.ObjectResponse.ProductStallObject;
import com.vibee.model.ObjectResponse.SelectExportStallObject;
import com.vibee.model.Status;
import com.vibee.model.item.*;
import com.vibee.model.request.product.CreateProductRequest;
import com.vibee.model.request.product.InfoCreateProductResponse;
import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.SelectedProductResponse;
import com.vibee.model.response.product.SelectedProductResult;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.repo.*;
import com.vibee.service.vproduct.CreateProductService;
import com.vibee.service.vwarehouse.CreateWarehouseService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Log4j2
@Service
public class CreateProductServiceImpl implements CreateProductService {
    private final VTypeProductRepo typeProductRepo;
    private final VSupplierRepo supplierRepo;
    private final VUnitRepo unitRepo;
    private final VUserRepo userRepo;
    private final VProductRepo productRepo;
    private final VImportRepo importRepo;
    private final VExportRepo exportRepo;
    private final VFileUploadRepo fileUploadRepo;
    private final CreateWarehouseService createWarehouseService;
    private final RedisAdapter redisAdapter;
    @Autowired
    public CreateProductServiceImpl(VTypeProductRepo typeProductRepo,
                                    VSupplierRepo supplierRepo,
                                    VUnitRepo unitRepo,
                                    VUserRepo userRepo,
                                    VProductRepo productRepo,
                                    VImportRepo importRepo,
                                    VExportRepo exportRepo,
                                    VFileUploadRepo fileUploadRepo,
                                    CreateWarehouseService createWarehouseService, RedisAdapter redisAdapter){
        this.typeProductRepo=typeProductRepo;
        this.supplierRepo=supplierRepo;
        this.unitRepo=unitRepo;
        this.userRepo=userRepo;
        this.productRepo=productRepo;
        this.importRepo = importRepo;
        this.exportRepo=exportRepo;
        this.fileUploadRepo=fileUploadRepo;
        this.createWarehouseService=createWarehouseService;
        this.redisAdapter = redisAdapter;
    }
    @Override
    public InfoCreateProductResponse info(String request) {
        log.info("infoService :: Start");
        String language=request;
        InfoCreateProductResponse response=new InfoCreateProductResponse();
        List<VTypeProduct> typeProducts=this.typeProductRepo.findAll();
        List<GetTypeProductItem> typeProductItems=this.convertItem(typeProducts);
        List<VSupplier> suppliers=this.supplierRepo.findAll();
        List<GetSupplierItem> supplierItems=this.convertItems(suppliers);
        List<VUnit> units=this.unitRepo.getAllUnitParents();
        List<InfoUnitItem> unitItems=this.convertUnitItems(units);
        response.setUnitItems(unitItems);
        response.setItems(supplierItems);
        response.setTypeProductItems(typeProductItems);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        log.info("infoService :: End");
        return response;
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

    @Override
    public CreateProductResponse create(CreateProductRequest request) {
        log.info("CreateProductService:: START");
        CreateProductResponse response=new CreateProductResponse();
        String language=request.getLanguage();
        int supplierId=request.getSupplierId();
        List<UnitItem> unitItemsRequest=request.getUnits();
        int fileId =request.getFileId();
        int unitId=request.getUnitId();
        double inAmount=request.getAmount();
        BigDecimal inPriceRequest=request.getInPrice();
        String creator= "vibeefirst1910";
        int productType=request.getCategoryId();
        String barCode=request.getBarCode();
        String description=request.getDescription();
        String productName=request.getNameProd();

        VSupplier supplier=this.supplierRepo.getById(supplierId);
        if(supplier==null) {
            log.error("supplier is not already exits, supplierId"+supplierId);
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.supplier.not.already.exits"));
            return response;
        }
        VProduct product=this.convertProduct(creator, supplier, fileId, productType, barCode, description, productName);
        product=this.productRepo.save(product);
        if(product==null) {
            log.error("Create Product is failed");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
            return response;
        }
        CreateWarehouseRequest warehouseRequest=this.convertWarehouse(product.getId(),unitItemsRequest, unitId, inPriceRequest, language, supplierId, inAmount, fileId);
        CreateWarehouseResponse warehouse=this.createWarehouseService.create(warehouseRequest);
        if (warehouse.getStatus().getStatus().equals(Status.Fail)) {
            log.error("Create Detail Product is failed");
            this.productRepo.delete(product.getId());
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.detail.product.create.failed"));
            return response;
        }
        response.setId(product.getId());
        response.getStatus().setMessage(MessageUtils.get(language,"msg.product.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("CreateProductService:: END");
        return response;
    }

    private VProduct convertProduct(String creator, VSupplier supplier, int fileId, int productType, String barCode,String description, String productName){
        VProduct product=new VProduct();
        product.setCreator(creator);
        product.setProductType(productType);
        product.setCreatedDate(new Date());
        product.setSupplierName(supplier.getNameSup());
        product.setFileId(fileId);
        product.setStatus(1);
        product.setId(0);
        product.setBarCode(barCode);
        product.setDescription(description);
        product.setProductName(productName);
        return product;
    }


    private CreateWarehouseRequest convertWarehouse(int productId, List <UnitItem> units, int unitId, BigDecimal inPrice, String language, int supplierId, double inAmount, int fileId){
        CreateWarehouseRequest warehouseRequest=new CreateWarehouseRequest();
        warehouseRequest.setProductId(productId);
        warehouseRequest.setOutPrice(BigDecimal.valueOf(0));
        warehouseRequest.setOutAmount((double) 0);
        warehouseRequest.setInPrice(inPrice);
        warehouseRequest.setUnitId(unitId);
        warehouseRequest.setLanguage(language);
        warehouseRequest.setSupplierId(supplierId);
        warehouseRequest.setInAmount(inAmount);
        warehouseRequest.setFileId(fileId);
        warehouseRequest.setUnitItems(units);
        return warehouseRequest;
    }

    @Override
    public CreateProductResponse upload(MultipartFile file, String language) {
        log.info("UploadProductSerivce :: Start");
        CreateProductResponse response=new CreateProductResponse();
        boolean saveImg= Utiliies .uploadFile(file);
        if(saveImg==false){
            log.error("save file is false");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language, "msg.product.failed"));
            return response;
        }
        VUploadFile uploadFile=new VUploadFile();
        uploadFile.setCreator("");
        uploadFile.setCreatedDate(new Date());
        uploadFile.setFileName(file.getName());
        uploadFile.setSize(BigDecimal.valueOf(file.getSize()));
        uploadFile.setType(file.getContentType());
        uploadFile.setUrl(Utiliies.getFilePath(file.getOriginalFilename()));
        uploadFile=this.fileUploadRepo.save(uploadFile);
        if (uploadFile==null){
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

    @Override
    public SelectedProductResponse selectProduct(String productCode, String cartCode, String language) {
        log.info("selectProductService:: BEGIN");
        SelectedProductResponse response=new SelectedProductResponse();

        Boolean isExistProduct=this.importRepo.isExistProductByProductCode(productCode);
        if (Boolean.FALSE.equals(isExistProduct)){
            log.error("product is not exist");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.product.not.found"));
            return response;
        }
        ProductStallObject productStall=this.productRepo.searchProductByImport(productCode);
        List<SelectExportStallObject> exportStalls=this.exportRepo.getExportsByProduct(productCode);



        List<SelectExportItem> items=new ArrayList<>();
        for (SelectExportStallObject exportStall:exportStalls){
            SelectExportItem item=new SelectExportItem();
            item.setInventory(exportStall.getInventory().intValue());
            item.setOutPrice(exportStall.getOutPrice());
            item.setUnitName(exportStall.getUnitName());
            item.setUnitId(exportStall.getUnitId());
            item.setExportId(exportStall.getExportId());
            items.add(item);
        }
        SelectExportItem item=new SelectExportItem();
        for(int i=0;i<items.size();i++){
            for(int j=i+1;j<items.size();j++){
                if(items.get(i).getAmount()<items.get(i).getAmount()){
                    item=items.get(i);
                    items.get(i).setAmount(items.get(j).getAmount());
                    items.get(i).setInventory(items.get(j).getInventory());
                    items.get(i).setUnitId(items.get(j).getUnitId());
                    items.get(i).setUnitName(items.get(j).getUnitName());
                    items.get(i).setExportId(items.get(j).getExportId());
                    items.get(i).setOutPrice(items.get(j).getOutPrice());
                    items.get(j).setAmount(item.getAmount());
                    items.get(j).setInventory(item.getInventory());
                    items.get(j).setUnitId(item.getUnitId());
                    items.get(j).setUnitName(item.getUnitName());
                    items.get(j).setExportId(item.getExportId());
                    items.get(j).setOutPrice(item.getOutPrice());
                }
            }
        }

        SelectedProductResult result=new SelectedProductResult();
        result.setImportId(productStall.getImportId());
        result.setAmount(1);
        result.setProductName(productStall.getProductName());
        result.setImg(productStall.getImg());
        result.setBarCode(productStall.getBarCode());
        result.setItems(items);
        //lưu vào redis
        CreateProduct createProduct = new CreateProduct();

        createProduct.setImportId(productStall.getImportId());
        createProduct.setAmount(1);
        createProduct.setProductName(productStall.getProductName());
        createProduct.setImg(productStall.getImg());
        createProduct.setBarCode(productStall.getBarCode());
        createProduct.setItems(items);
        createProduct.setProductId(productStall.getProductId());
        createProduct.setKey(cartCode);

        this.redisAdapter.set(cartCode,0, createProduct);

        response.setResult(result);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,""));
        log.info("selectProductService:: END");
        return response;
    }

    @Override
    public BaseResponse deleteCart(String key, String language) {
        BaseResponse response = new BaseResponse();
        System.out.println(this.redisAdapter.exists(key)+"dòng 283");
        this.redisAdapter.delete(key);
        System.out.println(this.redisAdapter.exists(key)+"dòng 285");
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,""));
        return response;
    }

    @Override
    public CreateProduct updateCart(Update request, String cartCode) {

        int importId = request.getImportId();
        String productName = request.getProductName();
        String img=  request.getImg();
        String barCode =  request.getBarCode();
        int amount = request.getAmount();
        int productId= request.getProductId();
        List<SelectExportItem> requestItesm =request.getItems();

        List<SelectExportItem> items=new ArrayList<>();
        List<SelectExportStallObject> exportStalls=this.exportRepo.getExportsByProduct(productId);

        for (SelectExportStallObject exportStall:exportStalls){
            SelectExportItem item=new SelectExportItem();
            item.setInventory(exportStall.getInventory().intValue());
            item.setOutPrice(exportStall.getOutPrice());
            item.setUnitName(exportStall.getUnitName());
            item.setUnitId(exportStall.getUnitId());
            item.setExportId(exportStall.getExportId());
            items.add(item);
        }

        CreateProduct createProduct = this.redisAdapter.get(cartCode, CreateProduct.class);
        requestItesm=items;
        if(createProduct.getKey()==null){
            return null;
        }
        createProduct.setKey(createProduct.getKey());
        createProduct.setImportId(productId);
        createProduct.setAmount(1);
        createProduct.setProductName(productName);
        createProduct.setImg(img);
        createProduct.setBarCode(barCode);
        createProduct.setItems(items);
        createProduct.setProductId(productId);
        createProduct.setKey(cartCode);

        this.redisAdapter.set(createProduct.getKey(),0, createProduct);

        return null;
    }
}
