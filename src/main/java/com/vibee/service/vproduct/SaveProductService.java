package com.vibee.service.vproduct;

import com.vibee.jedis.CreateProduct;
import com.vibee.jedis.Update;
import com.vibee.model.request.product.CreateProductRequest;
import com.vibee.model.request.product.InfoCreateProductResponse;
import com.vibee.model.request.product.UpdateProductRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.CreateProductResponse;
import com.vibee.model.response.product.SelectedProductResponse;
import com.vibee.model.response.product.UpdateProductResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

public interface SaveProductService {
    InfoCreateProductResponse info(String request);

    CreateProductResponse create(CreateProductRequest request);
    CreateProductResponse upload(MultipartFile file, String language);
    SelectedProductResponse selectProduct(String productId,String cartCode, String language);
    BaseResponse deleteCart(String key, String language);
    CreateProduct updateCart(Update request, String cartCode);
    UpdateProductResponse UpdateProduct(UpdateProductRequest request, BindingResult result);

    UpdateProductResponse updateUpload(MultipartFile file,int productId, String language);

}
