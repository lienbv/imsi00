package com.vibee.model.request.bill;

import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.product.SelectedProductResult;
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
public class debtRequest extends BaseRequest {
    private BigDecimal inPrice;
    private String paymentMethod;
    private String transactionType;
    private String cartCode;
    private String fullName;
    private String address;
    private String phoneNumber;
    private BigDecimal totalPriceDebt;
    private String message;
    private List<SelectedProductResult> viewStallResults;
}
