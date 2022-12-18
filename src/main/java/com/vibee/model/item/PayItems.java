package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayItems {
    private int id;
    private int idDebt;
    private BigDecimal inPrice;
    private BigDecimal price;
    private int numberOfPayOuts;
    private Date datePayment;
    private String creator;
    private String statusCode;
}
