package com.vibee.service.vproduct;

import com.vibee.model.request.product.DeleteProductRequest;
import com.vibee.model.request.product.LockRequest;
import com.vibee.model.response.product.DeleteProductResponse;
import com.vibee.model.response.product.LockResponse;
import org.springframework.validation.BindingResult;

public interface CloseProductService {
    LockResponse lock(LockRequest request);
    LockResponse unLock(LockRequest Request);
    DeleteProductResponse DeleteProduct(DeleteProductRequest request, BindingResult result);
}
