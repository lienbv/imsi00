package com.vibee.controller.v_import;

import com.vibee.model.info.ImportWarehouseInfor;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.v_import.ImportInWarehouse;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.category.SelectionTypeProductItems;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.ShowProductByBarcodeResponse;
import com.vibee.model.response.v_import.EditImportWarehouse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.service.vemployee.ITypeProductService;
import com.vibee.service.vimport.IImportSuppierService;
import com.vibee.service.vproduct.SaveProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/vibee/api/v1/auth/import-warehouse")
@CrossOrigin("*")
public class ImportController {
    private final IImportSuppierService importSupplierService;
    private final ITypeProductService typeProductService;
    private final SaveProductService createProductService;

    public ImportController(IImportSuppierService importSupplierService, ITypeProductService typeProductService, SaveProductService createProductService) {
        this.importSupplierService = importSupplierService;
        this.typeProductService = typeProductService;
        this.createProductService = createProductService;
    }

    @GetMapping(value = "type-product")
    public List<SelectionTypeProductItems> getAllSelectType(){
        return this.typeProductService.getAllSelected();
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

    @GetMapping("/deleteAll-importWarehouse/{key}")
    public  BaseResponse deleteAll(@PathVariable(name = "key") int key,  @RequestParam(name = "language") String language){
        return this.importSupplierService.deleteAll(key, language);
    }
    @GetMapping("/deleteById-importWarehouse/{key}/{redisId}")
    public  BaseResponse deleteById(@PathVariable(name = "key") int key, @PathVariable(name = "redisId") String redisId, @RequestParam(name = "language") String language){
        return this.importSupplierService.deleteById(key, redisId, language);
    }

    @GetMapping("/getAllRedis-importWarehouse/{key}")
    public List<ImportWarehouseInfor> getAllListRedis(@PathVariable(name = "key") int key){
        return this.importSupplierService.getAllProductImportOfSupplier(key);
    }
    @PostMapping("/done-import")
    public ImportWarehouseItemsResponse doneImport(@RequestBody List<ImportWarehouseInfor> data ){
        return this.importSupplierService.done(data);
    }
    @PostMapping("/update-import/{key}/{redisId}")
    public BaseResponse update(@RequestBody ImportInWarehouse request,@PathVariable(name = "key") int key, @PathVariable(name = "redisId") String redisId){
        return this.importSupplierService.update(request, key, redisId);
    }
    @GetMapping("/edit-import/{key}/{redisId}")
    public EditImportWarehouse edit(@PathVariable(name = "key") int key, @PathVariable(name = "redisId") String redisId, @RequestParam(name = "language") String language){
        return this.importSupplierService.edit(key, redisId, language);
    }
    @GetMapping("/unitId-parent/{parent}")
    public List<UnitItem> getAllSelectUnitByIdParent(@PathVariable(name = "parent") int parent, @RequestParam(name = "language") String language){
        return this.importSupplierService.getAllSelectUnitByIdParent(parent, language);
    }
}
