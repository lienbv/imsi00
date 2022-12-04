package com.vibee.controller.supplier;

import com.vibee.model.request.v_supplier.CreateSupplierRequest;
import com.vibee.model.request.v_supplier.UpdateSupplierRequste;
import com.vibee.model.response.v_supplier.CreateSupplierResponse;
import com.vibee.model.response.v_supplier.DeleteSuplierResponse;
import com.vibee.model.response.v_supplier.ListSupplierResponse;
import com.vibee.model.response.v_supplier.UpdateSuplierResponse;
import com.vibee.repo.VSupplierRepo;
import com.vibee.service.vsupplier.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class SupplierController {
    private SupplierService supService;
    private VSupplierRepo supRepo;
    private final GetSupplierService getSupplierService;
    @Autowired
    public SupplierController(
            SupplierService supService,
            VSupplierRepo supRepo) {
        this.supRepo = supRepo;
        this.supService = supService;
    }

    @PostMapping(value = "/vibee/api/v1/admins/supplier/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public CreateSupplierResponse createSup(@Valid @RequestBody CreateSupplierRequest request,
                                            BindingResult bindingResult, @RequestParam(value = "language", defaultValue = "vi") String language) {
        return supService.createSup(request, bindingResult, language);
    }

    @PostMapping(value = "/vibee/api/v1/admins/supplier/update")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public UpdateSuplierResponse updateSup(@Valid @RequestBody UpdateSupplierRequste request,
                                           BindingResult bindingResult, @RequestParam(value = "language", defaultValue = "vi") String language) {
        return supService.UpdateSup(request, bindingResult,language);
    }

    @GetMapping(value = "/vibee/api/v1/admins/supplier/delete")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public DeleteSuplierResponse deleteSup(@RequestParam(value = "language", defaultValue = "") String language,
                                           @RequestParam(value = "id") int id) {
        DeleteSuplierResponse response = supService.deleteSup(language, id);
        return response;
    }
    @GetMapping(value = "/vibee/api/v1/admins/supplier/display")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public ListSupplierResponse DisplaySupplier(@RequestParam(value = "status", defaultValue = "1") int status,
                                                @RequestParam(value = "nameSearch", defaultValue = "") String nameSup,
                                                @RequestParam(value = "language", defaultValue = "vi") String language,
                                                @RequestParam(value = "nameSup", defaultValue = "") String name,
                                                @RequestParam(value = "phoneNumber", defaultValue = "") String phoneNumber,
                                                @RequestParam(value = "email", defaultValue = "") String email,
                                                @RequestParam(value = "createdDate", defaultValue = "") String createDate,
                                                @RequestParam(value = "address", defaultValue = "") String address,
                                                @RequestParam(value="pageNumber", defaultValue = "0") int pageNumber,
                                                @RequestParam(value="size", defaultValue = "10") int size) {
        return supService.displaySupplier(status, nameSup, language, name, phoneNumber, email, createDate, address, pageNumber, size);


    @GetMapping("/get-all")
    public GetSuppliersResponse getAll(@RequestParam(name= "language") String languageReq) {
        return this.getSupplierService.getAll(languageReq);
    }
}
