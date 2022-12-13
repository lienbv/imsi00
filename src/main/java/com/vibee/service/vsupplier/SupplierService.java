package com.vibee.service.vsupplier;

import com.vibee.model.request.v_supplier.CreateSupplierRequest;
import com.vibee.model.request.v_supplier.UpdateSupplierRequste;
import com.vibee.model.response.v_supplier.CreateSupplierResponse;
import com.vibee.model.response.v_supplier.DeleteSuplierResponse;
import com.vibee.model.response.v_supplier.ListSupplierResponse;
import com.vibee.model.response.v_supplier.UpdateSuplierResponse;
import org.springframework.validation.BindingResult;

import javax.validation.Valid;

public interface SupplierService {
    public CreateSupplierResponse createSup(CreateSupplierRequest request, BindingResult bindingResult, String language);
    public UpdateSuplierResponse UpdateSup(@Valid UpdateSupplierRequste request, BindingResult bindingResult, String language);
    public DeleteSuplierResponse lockAnhUnLock(String language, int id);
    public DeleteSuplierResponse delete(String language, int id);
    public ListSupplierResponse displaySupplier(int status, String nameSup, String language, String name, String phoneNumber, String email, String createDate, String address, int pageNumber, int size);
}
