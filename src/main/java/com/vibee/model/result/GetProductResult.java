package com.vibee.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductResult {
    private int productId;
    private String productName;
    private String statusName;
    private double sumInAmount;
    private double sumOutAmount;
    private double inventory;
    private int statusCode;
    private String img;
    private long countImported;
    private BigDecimal sumInPrice;
    private BigDecimal sumOutPrice;
    private BigDecimal profit;
}
