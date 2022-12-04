package com.vibee.controller.supplier;

import com.vibee.model.response.supplier.GetSuppliersResponse;
import com.vibee.service.v_supplier.GetSupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/supplier")
@CrossOrigin("*")
public class SupplierController {
    @Autowired
    private GetSupplierService getSupplierService;

    @GetMapping("/get-all")
    public GetSuppliersResponse getAll(@RequestParam(name= "language") String languageReq) {
        return this.getSupplierService.getAll(languageReq);
    }
}
