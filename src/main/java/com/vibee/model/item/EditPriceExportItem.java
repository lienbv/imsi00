package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EditPriceExportItem {
    private BigDecimal price;
    private int idExport;
}
