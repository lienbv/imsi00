package com.vibee.model.response.debit;

import com.vibee.model.request.BaseRequest;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetDetailBill extends BaseRequest {
    private String productName;
    private int amount;
    private int unitId;
    private BigDecimal price;
    private String unitName;
    private BigDecimal inPrice;
}
