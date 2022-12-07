package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetCharWareHouseItem {
    private String createDate;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private double inAmount;
    private double outAmount;
}
