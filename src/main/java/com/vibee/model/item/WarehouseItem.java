package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseItem {
    private int id;
    private int statusCode;
    private String statusName;
    private String unit;
    private String createdDate;
    private String creator;
    private double inAmount;
    private long outAmount;
    private BigDecimal inPrice;
    private Double inventory;
    private String productCode;
    private String expireDate;

}
