package com.vibee.controller.warehouse;

import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.response.warehouse.GetWarehousesResponse;
import com.vibee.service.vimport.GetWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/warehouse")
@CrossOrigin("*")
public class WarehouseController {
    private final GetWarehouseService getWarehouseService;

    @Autowired
    public WarehouseController(GetWarehouseService getWarehouseService){
        this.getWarehouseService=getWarehouseService;
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
}
