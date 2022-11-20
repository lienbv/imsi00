package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetLastImportItem {
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private double inAmount;
    private BigDecimal outAmount;
    private BigDecimal profit;
    private BigDecimal  compareInMoney;
    private BigDecimal compareOutMoney;
    private BigDecimal compareProfit;
}
