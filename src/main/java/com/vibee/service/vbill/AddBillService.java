package com.vibee.service.vbill;

import com.vibee.model.request.bill.TransactionBillRequest;
import com.vibee.model.request.bill.ViewBillRequest;
import com.vibee.model.response.BaseResponse;

public interface AddBillService {
    BaseResponse add(TransactionBillRequest request);
    BaseResponse saveRedis(TransactionBillRequest request);
}
