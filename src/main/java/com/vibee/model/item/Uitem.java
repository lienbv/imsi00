package com.vibee.model.item;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class Uitem {
    private String nameUnit;
    private int amount;
    private int idUnit;
    private int idExport;
    private BigDecimal outPrice;
    public Uitem() {
    }

    public Uitem(String nameUnit, int amount, int idUnit, int idExport, BigDecimal outPrice) {
        this.nameUnit = nameUnit;
        this.amount = amount;
        this.idUnit = idUnit;
        this.idExport = idExport;
        this.outPrice = outPrice;
    }
}
