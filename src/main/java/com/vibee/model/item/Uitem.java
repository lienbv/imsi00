package com.vibee.model.item;

import lombok.*;

@Getter
@Setter
public class Uitem {
    private String nameUnit;
    private int amount;
    private int idUnit;
    private int idExport;
    public Uitem() {
    }

    public Uitem(String nameUnit, int amount, int idUnit, int idExport) {
        this.nameUnit = nameUnit;
        this.amount = amount;
        this.idUnit = idUnit;
        this.idExport = idExport;
    }
}
