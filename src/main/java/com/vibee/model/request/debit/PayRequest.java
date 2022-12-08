package com.vibee.model.request.debit;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class PayRequest extends BaseRequest {
    private int debiId;
    private BigDecimal amountPay;
}
