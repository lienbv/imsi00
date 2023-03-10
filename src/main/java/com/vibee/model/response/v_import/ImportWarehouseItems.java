package com.vibee.model.response.v_import;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ImportWarehouseItems {
    private int importId;
    private String productCode;
    private String qrCode;
    private String unitName;
    private int amount;
    private BigDecimal inPrice;
    private Date rangeDate;
    private String productName;
}
