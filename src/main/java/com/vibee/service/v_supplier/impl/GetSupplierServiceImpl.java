package com.vibee.service.v_supplier.impl;

import com.vibee.entity.VSupplier;
import com.vibee.model.Status;
import com.vibee.model.response.supplier.GetSuppliersResponse;
import com.vibee.model.result.GetSuppliersResult;
import com.vibee.repo.VSupplierRepo;
import com.vibee.service.v_supplier.GetSupplierService;
import com.vibee.utils.CommonUtil;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
@Log4j2
public class GetSupplierServiceImpl implements GetSupplierService {

    @Autowired
    private VSupplierRepo supplierRepo;
    @Override
    public GetSuppliersResponse getAll(String language) {
        log.info("GetSupplierServiceImpl getAll start with language: " + language);
        GetSuppliersResponse response=new GetSuppliersResponse();
        List<GetSuppliersResult> results=new ArrayList<>();
        List<VSupplier> suppliers=supplierRepo.getSuppliers(Status.ACTIVE);
        if (suppliers.isEmpty()) {
            log.error("GetSupplierServiceImpl getAll suppliers is empty");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage("Not found");
            return response;
        }
        for (VSupplier supplier:suppliers){
            GetSuppliersResult result=new GetSuppliersResult();
            result.setSupplierId(supplier.getId());
            result.setSupplierName(supplier.getNameSup());
            result.setSupplierAddress(supplier.getAddress());
            result.setSupplierPhone(supplier.getNumberPhone());
            result.setSupplierEmail(supplier.getEmail());
            result.setCreator(supplier.getCreator());
            result.setCreatedDate(Utiliies.formatDateTime(supplier.getCreatedDate()));
            result.setStatusCode(supplier.getStatus());
            result.setStatusName(Utiliies.convertStatusSupplier(supplier.getStatus(),language));
            results.add(result);
        }
        response.setSuppliers(results);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("Success");
        log.info("GetSupplierServiceImpl getAll end with response: " + response);
        return response;
    }
}
