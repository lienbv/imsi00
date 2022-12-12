package com.vibee.model.response.debit;

import com.vibee.model.ObjectResponse.DetailBillOfDetailDebit;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class DetailBillOfDetailDebitResponse extends BaseResponse {
    List<DetailBillOfDetailDebit> items;
}
