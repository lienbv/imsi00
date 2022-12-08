package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ExportBill {
    private String nameProduct;
    private BigDecimal price;
    private int amount;
    private BigDecimal total;

    public ExportBill(String nameProduct, BigDecimal price, int amount, BigDecimal total) {
        this.nameProduct = nameProduct;
        this.price = price;
        this.amount = amount;
        this.total = total;
    }
}
