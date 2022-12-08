package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DebitDetailItems {

    private String productName;

    private BigDecimal priceOfDetailDebit;

    private int unitId;

    private int amount;
}
