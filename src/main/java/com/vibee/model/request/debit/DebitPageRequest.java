package com.vibee.model.request.debit;

import com.vibee.model.item.FilterItem;
import com.vibee.model.request.BaseRequest;
import lombok.Data;

@Data
public class DebitPageRequest extends BaseRequest {
    private int pageNumber;
    private int pageSize;
    private FilterItem filter;
    private String searchText;
}
