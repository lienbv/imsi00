package com.vibee.service.v_typep_product;

import com.vibee.model.response.category.GetTypeProductResponse;

public interface GetTypeProductService {
    GetTypeProductResponse getAll(String language);
}
