package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class DebitUserItems {
    private int id;
    private int amountUserDebit;
    private String fullName;
    private String phoneNumber;
    private BigDecimal total;
    private String statusCode;
    private String address;
}
