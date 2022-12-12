package com.vibee.model.response.debit;

import com.vibee.model.item.DebitItems;
import com.vibee.model.item.FilterItem;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;
@Data
public class DebitItemsResponse extends BaseResponse {
    List<DebitItems> items;
    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;
    private FilterItem filter;
}
