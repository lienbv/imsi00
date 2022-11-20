package com.vibee.model.response.category;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TypeProductItemsResponse extends BaseResponse {
    private List<TypeItemsDto> data;
}
