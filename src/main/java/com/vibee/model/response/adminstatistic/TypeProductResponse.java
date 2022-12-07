package com.vibee.model.response.adminstatistic;

import com.vibee.entity.VTypeProduct;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TypeProductResponse extends BaseResponse {
    private List<VTypeProduct> typeProducts;
}