package com.vibee.model.response.export;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExportStallItem {
    private int id;
    private String name;
    private BigDecimal outPrice;
    private Double outAmount;
    private int unitParent;
    private int unitId;
    private String unitName;
    private Double inventory;
}
