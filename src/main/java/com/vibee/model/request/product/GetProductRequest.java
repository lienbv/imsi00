package com.vibee.model.request.product;

import com.vibee.model.item.FilterItem;
import com.vibee.model.request.BaseRequest;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetProductRequest extends BaseRequest {
    private int pageNumber;
    private int pageSize;
    private FilterItem filter;
    private String searchText;
    private String typeSearch;
}
