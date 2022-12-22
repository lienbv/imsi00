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
public class ExportResult {
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int unitId;
    private String unitName;
    private int inventory;
    private int exportId;
    private int amount;
}
