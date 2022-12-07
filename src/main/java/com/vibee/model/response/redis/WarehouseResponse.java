package com.vibee.model.response.redis;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WarehouseResponse {
    private int productId;
    private int inAmount;
    private String creator;
    private int supplierId;
    private BigDecimal inPrice;
    private int unitId;
    private int numberOfEntries;

}
