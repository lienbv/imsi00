package com.vibee.model.request.bill;

import com.vibee.model.request.BaseRequest;
import com.vibee.model.result.TransactionBillResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionBillRequest extends BaseRequest {
    private BigDecimal inPrice;
    private String paymentMethod;
    private String transactionType;
    private String cartCode;
    private List<TransactionBillResult> results;
}
