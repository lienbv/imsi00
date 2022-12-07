package com.vibee.controller.v_import;

import com.vibee.jedis.ImportInWarehouseRedis;
import com.vibee.model.request.v_import.ImportInWarehouse;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.SelectionTypeProductItems;
import com.vibee.model.response.category.SelectionTypeProductItemsResponse;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.ShowProductByBarcodeResponse;
import com.vibee.model.response.redis.ImportWarehouseResponse;
import com.vibee.model.response.redis.ListAllRedis;
import com.vibee.model.response.redis.UnitItemsResponse;
import com.vibee.model.response.supplier.ListSupplier;
import com.vibee.model.response.supplier.SupplierResponse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.model.response.v_import.ListImportInWarehouseRedis;
import com.vibee.model.response.v_import.ListImportWarehouseInforResponse;
import com.vibee.service.vemployee.ITypeProductService;
import com.vibee.service.vimport.impl.ImportSupplierServiceImpl;
import com.vibee.service.vproduct.CreateProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin("*")
public class ImportController {
    private final ImportSupplierServiceImpl importSupplierService;
    private final ITypeProductService typeProductService;
    private final CreateProductService createProductService;

    public ImportController(ImportSupplierServiceImpl importSupplierService, ITypeProductService typeProductService, CreateProductService createProductService) {
        this.importSupplierService = importSupplierService;
        this.typeProductService = typeProductService;
        this.createProductService = createProductService;
    }
    @GetMapping("/unit")
    public SelectionTypeProductItemsResponse getAllSelect(@RequestParam(name = "language") String language){
        return this.importSupplierService.getAllSelect(language);
    }
    @GetMapping("/supplier")
    public ListSupplier getAllSupplier(){
        return this.importSupplierService.getAllSupplier();
    }
    @GetMapping("/supplier/name")
    public SupplierResponse getAllSupplier(@RequestParam(name = "id") int id){
        return this.importSupplierService.getNameAllSupplier(id);
    }
    @GetMapping(value = "type-product")
    public List<SelectionTypeProductItems> getAllSelectType(){
        return this.typeProductService.getAllSelected();
    }
    @GetMapping("/unit/{id}")
    public SelectionTypeProductItemsResponse getAllUnitById(@PathVariable(name = "id") int id, @RequestParam(name = "language") String language){
        return this.importSupplierService.getAllSelectUnitByIdParent(id, language);
    }
    @GetMapping(value = "barcode-product/{barcode}")
    public ShowProductByBarcodeResponse showProductByBarcode(@PathVariable(name = "barcode")String barcode, @RequestParam(name = "language") String language){
        return this.importSupplierService.showProductByBarcode(barcode, language);
    }
    @PostMapping("/upload")
    public CreateProductResponse createProduct(@RequestParam("file") MultipartFile file, @RequestParam("language") String language){
        return this.createProductService.upload(file,language);
    }
    @PostMapping("/create-importWarehouse")
    public BaseResponse importWarehouseOfSupplier(@RequestBody ImportInWarehouse request){
        return this.importSupplierService.importWarehouseOfSupplier(request);
    }
    @GetMapping("/getAll-importWarehouse")
    public BaseResponse getAll(){
        return this.importSupplierService.getAll();
    }

    @GetMapping("/deleteAll-importWarehouse/{key}")
    public  BaseResponse deleteAll(@PathVariable(name = "key") int key,  @RequestParam(name = "language") String language){
        return this.importSupplierService.deleteAll(key, language);
    }
    @GetMapping("/deleteById-importWarehouse/{key}")
    public  BaseResponse deleteById(@PathVariable(name = "key") int key, @PathVariable(name = "key") String redisId, @RequestParam(name = "language") String language){
        return this.importSupplierService.deleteById(key, redisId, language);
    }

    @GetMapping("/findByUnitId/{id}")
    public UnitItemsResponse findByIdUnit(@PathVariable(name = "id") int id){
        return this.importSupplierService.findByIdUnit(id);
    }
    @GetMapping("/getAllRedis-importWarehouse/{key}")
    public ListImportWarehouseInforResponse getAllListRedis(@PathVariable(name = "key") int key){
        return this.importSupplierService.getAllProductImportOfSupplier(key);
    }
    @PostMapping("/done-import")
    public ImportWarehouseItemsResponse doneImport(@RequestBody ListImportWarehouseInforResponse data ){
        return this.importSupplierService.done(data);
    }
    @GetMapping("/getAllRedisAll-importWarehouse")
    public  void getAllListRedisAll(){
     importSupplierService.getAllRedisAll();
    }
}
