package com.vibee.model.response.product;

import com.vibee.model.item.GetTypeProductItem;
import com.vibee.model.item.ProductStatusItem;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class InfoUpdateProductResponse extends BaseResponse {
    private List<GetTypeProductItem> typeProductItems;
    private int idProd;
    private String nameProd;
    private String statusName;
    private int statusCode;
    private String img;
    private String barCode;
    private String description;
    private int categoryId;
    private String categoryName;
    private List<ProductStatusItem> statusItems;
}
