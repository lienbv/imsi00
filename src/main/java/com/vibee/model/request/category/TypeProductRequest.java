package com.vibee.model.request.category;

import com.vibee.model.item.FilterItem;
import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class TypeProductRequest extends BaseRequest {
    private int pageNumber;
    private int pageSize;
    private FilterItem filter;
    private String searchText;
}
