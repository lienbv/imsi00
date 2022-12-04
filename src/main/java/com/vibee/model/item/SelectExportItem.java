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
public class SelectExportItem {
    private int exportId;
    private int unitId;
    private String unitName;
    private int inventory;
    private BigDecimal outPrice;
    private int amount;
}
