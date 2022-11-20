package com.vibee.model.response.product;

import com.vibee.model.ObjectResponse.ProductStallObject;
import com.vibee.model.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SearchViewStallResponse extends BaseResponse {
    private List<ProductStallObject> results;
}
