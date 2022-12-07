package com.vibee.controller.manage;

import com.vibee.model.item.FilterItem;
import com.vibee.model.request.warehouse.DeleteWarehouseRequest;
import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.request.warehouse.UpdateWarehouseRequest;
import com.vibee.model.response.warehouse.*;
import com.vibee.service.vimport.DeleteWareHouseService;
import com.vibee.service.vimport.UpdateWarehouseService;
import com.vibee.service.vimport.GetWarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/auth/warehouse")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WereHouseManagerController {

    private final UpdateWarehouseService updateWarehouseService;
    private final GetWarehouseService getWarehouseService;
    private final DeleteWareHouseService deleteWareHouseService;

    @Autowired
    public WereHouseManagerController(UpdateWarehouseService updateWarehouseService,
                                      GetWarehouseService getWarehouseService,
                                      DeleteWareHouseService deleteWareHouseService){
        this.updateWarehouseService=updateWarehouseService;
        this.getWarehouseService=getWarehouseService;
        this.deleteWareHouseService=deleteWareHouseService;
    }

    @GetMapping("/getall/{id}")
    public GetWarehousesResponse getAll(@PathVariable(name = "id") int productCodeReq,
                                        @RequestParam(name= "language") String languageReq
                                        ) {
        GetWarehouseRequest request = new GetWarehouseRequest();
        request.setLanguage(languageReq);
        request.setProductId(productCodeReq);
        return this.getWarehouseService.getWarehouses(request);
    }

    @GetMapping("/filter/{id}")
    public FilterWarehouseResponse filter(@PathVariable(name = "id") int productCodeReq,
                                          @RequestParam(name = "pagenumber") int pageNumberReq,
                                          @RequestParam(name = "pagesize") int pageSizeReq,
                                          @RequestParam(name = "typefilter") String typeFilterReq,
                                          @RequestParam(name = "valuefilter") String valueFilterReq,
                                          @RequestParam(name= "language") String languageReq
    ) {
        GetWarehouseRequest request = new GetWarehouseRequest();
        request.getPageItem().setPage(pageNumberReq);
        request.getPageItem().setPageSize(pageSizeReq);
        request.setFilterItem(new FilterItem(typeFilterReq, valueFilterReq));
        request.setLanguage(languageReq);
        request.setProductId(productCodeReq);
        request.setLanguage(languageReq);
        return this.getWarehouseService.filterWarehouses(request);
    }

    @PostMapping("/update")
    public UpdateWarehouseResponse updateWereHouse(@RequestBody UpdateWarehouseRequest request) {
        return this.updateWarehouseService.updateWarehouse(request);
    }

    @PostMapping("/delete")
    public DeleteWarehouseResponse deleteWereHouse(@RequestBody DeleteWarehouseRequest request) {
        return this.deleteWareHouseService.deleteWarehouse(request);
    }
}
