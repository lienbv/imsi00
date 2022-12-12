package com.vibee.model.response.debit;

import com.vibee.model.item.DebitUserItems;
import com.vibee.model.item.FilterItem;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;
@Data
public class DebitOfUserResponse extends BaseResponse {
    List<DebitUserItems> items;
    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;
    private FilterItem filter;
}
