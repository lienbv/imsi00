package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UnitItem implements Serializable {
    private static final long serialVersionUID = 1L;
    private int unitId;
    private String unitName;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int amount;
}
