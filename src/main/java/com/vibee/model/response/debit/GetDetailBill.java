package com.vibee.model.response.debit;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GetDetailBill {
    private String productName;
    private int amount;
    private int unitId;
    private BigDecimal price;
    private String unitName;
    private BigDecimal inPrice;
}
