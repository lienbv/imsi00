package com.vibee.model.response.v_customeraccount;

import com.vibee.model.item.CustomerAccountItem;
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
public class CustomerAccountsResponse extends BaseResponse {
    List<CustomerAccountItem> customers;
    private int totalItems;
    private int totalPages;
}

