package com.vibee.service.v_typep_product.impl;

import com.vibee.entity.VTypeProduct;
import com.vibee.model.Status;
import com.vibee.model.response.category.GetTypeProductResponse;
import com.vibee.model.result.GetTypeProductResult;
import com.vibee.repo.VTypeProductRepo;
import com.vibee.service.v_typep_product.GetTypeProductService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
public class GetTypeProductServiceImpl implements GetTypeProductService {
    private final VTypeProductRepo typeProductRepo;

    @Autowired
    public GetTypeProductServiceImpl(VTypeProductRepo typeProductRepo){
        this.typeProductRepo=typeProductRepo;
    }
    @Override
    public GetTypeProductResponse getAll(String language) {
        log.info("Get all type product with language: {}", language);
        GetTypeProductResponse response=new GetTypeProductResponse();
        List<GetTypeProductResult> results=new ArrayList<>();
        List<VTypeProduct> typeProducts=this.typeProductRepo.findByStatus(Status.ACTIVE);
        if(typeProducts.isEmpty()){
            log.error("Type product is empty with language: {}", language);
            response.getStatus().setMessage(MessageUtils.get(language,"type.product.not.found"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        for (VTypeProduct typeProduct : typeProducts) {
            GetTypeProductResult result=new GetTypeProductResult();
            result.setId(typeProduct.getId());
            result.setName(typeProduct.getName());
            result.setParentId(typeProduct.getParentId());
            result.setStatusCode(typeProduct.getStatus());
            result.setStatusName(ProductUtils.statusname(typeProduct.getStatus()));
            results.add(result);
        }
        response.setResults(results);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"get.type.product.success"));
        log.info("Get all type product with language: {} success", language);
        return response;
    }
}
