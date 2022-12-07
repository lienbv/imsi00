package com.vibee.model.request.bill;

import com.vibee.model.request.BaseRequest;
import com.vibee.model.result.CreateDetailBillResult;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ViewBillRequest extends BaseRequest {
    private String cartCode;
    private List<CreateDetailBillResult> detailBills;
}
