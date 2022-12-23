package com.vibee.model.info;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportInfor {
   private int exportId;
    private int unitId;
    private String unitName;
    private BigDecimal outPrice;
    private Double inventory;
    private int amount;
    private int unitAmount;
}
