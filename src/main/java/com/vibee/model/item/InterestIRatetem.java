package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InterestIRatetem {
    private int id;
    private BigDecimal price;

    public InterestIRatetem(int id, BigDecimal price) {
        this.id = id;
        this.price = price;
    }
}
