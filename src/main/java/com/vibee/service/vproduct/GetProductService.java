package com.vibee.service.vproduct;

import com.vibee.model.request.product.GetProductRequest;
import com.vibee.model.request.product.ViewStallRequest;
import com.vibee.model.response.product.*;

public interface GetProductService {
    SearchViewStallResponse viewStall(ViewStallRequest language);

    GetProductResponse viewManage(GetProductRequest request);

    DetailProductResponse detail(int productId, String language);
    GetHomeSellOnlineResponse selectProduct(String language);

    InfoUpdateProductResponse infoUpdate(int idProd,String language);

}
