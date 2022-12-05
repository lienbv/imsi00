package com.vibee.controller.warehouse;

import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.warehouse.GetWarehousesResponse;
import com.vibee.model.response.warehouse.ImportWarehouseResponse;
import com.vibee.service.vimport.GetWarehouseService;
import com.vibee.service.vwarehouse.CreateWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/vibee/api/v1/warehouse")
@CrossOrigin("*")
public class WarehouseController {
    private final GetWarehouseService getWarehouseService;
    private final CreateWarehouseService createWarehouseService;

    @Autowired
    public WarehouseController(GetWarehouseService getWarehouseService,
                               CreateWarehouseService createWarehouseService){
        this.getWarehouseService=getWarehouseService;
        this.createWarehouseService=createWarehouseService;
    }
    @GetMapping("/view-manage/{id}")
    public GetWarehousesResponse getAll(@PathVariable(name = "id") int productCodeReq,
                                        @RequestParam(name = "language") String languageReq) {
        GetWarehouseRequest request = new GetWarehouseRequest();
        request.setLanguage(languageReq);
        request.setProductId(productCodeReq);
        return this.getWarehouseService.getWarehouses(request);
    }

    @PostMapping("/batch/create")
    public GetWarehousesResponse createBatch(@PathVariable(name = "id") int productCodeReq,
                                        @RequestParam(name = "language") String languageReq) {
        GetWarehouseRequest request = new GetWarehouseRequest();
        request.setLanguage(languageReq);
        request.setProductId(productCodeReq);
        return this.getWarehouseService.getWarehouses(request);
    }

    @PostMapping("/import-file")
    public ImportWarehouseResponse createProduct(@RequestParam("file") MultipartFile file, @RequestParam("language") String language, @RequestParam("supplierCode") int supplierCode) {
        return this.createWarehouseService.importFile(supplierCode,language,file);
    }
}
