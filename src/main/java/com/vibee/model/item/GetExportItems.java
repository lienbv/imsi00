package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class GetExportItems {
    private String unitName;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int exportId;
}
