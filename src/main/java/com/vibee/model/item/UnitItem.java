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
public class UnitItem {
    private int unitId;
    private String unitName;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int amount;
}
