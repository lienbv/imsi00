package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class GetProductItem {
    private String productName;
    private int productCode;
    private String statusName;
    private Double inventory;
    private BigDecimal profit;
    private String img;
    private String supName;
    private int statusCode;
    private int unit;
}
