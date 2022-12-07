package com.vibee.service.vproduct;

import com.vibee.jedis.CreateProduct;
import com.vibee.jedis.Update;
import com.vibee.model.request.product.CreateProductRequest;
import com.vibee.model.request.product.InfoCreateProductResponse;
import com.vibee.model.request.v_import.CreateImportRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.SelectedProductResponse;
import com.vibee.model.response.product.SelectedProductResult;
import com.vibee.model.response.v_import.CreateImportResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CreateProductService {
    InfoCreateProductResponse info(String request);

    CreateProductResponse create(CreateProductRequest request);
    CreateProductResponse upload(MultipartFile file, String language);
    SelectedProductResponse selectProduct(String productId,String cartCode, String language);
    BaseResponse deleteCart(String key, String language);
    CreateProduct updateCart(Update request, String cartCode);

}
