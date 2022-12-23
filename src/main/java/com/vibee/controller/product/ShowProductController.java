package com.vibee.controller.product;

import com.vibee.model.ObjectResponse.SelectExportStallObject;
import com.vibee.model.response.product.ShowListProduct;
import com.vibee.service.vproduct.impl.ShowProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${vibee.config}/staff-product/")
@CrossOrigin("*")
public class ShowProductController {
    @Autowired
    ShowProductServiceImpl showProductService;

    @GetMapping("show-list-product")
    public ShowListProduct showProduct(){
        return this.showProductService.showProduct();
    }
    @GetMapping("getUnitId/{id}/{unitId}")
    public SelectExportStallObject getUnit(@PathVariable(name = "id") int id, @PathVariable(name = "unitId") int unitId){
        return this.showProductService.getUnit(id, unitId);
    }
}
