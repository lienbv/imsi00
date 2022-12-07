package com.vibee.model.response.redis;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportResponse {
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int unitId;
    private String creator;
    private int importId;
    private int outAmount;
    private String supplierName;
    private int supplierId;
    private int fileId;
}
