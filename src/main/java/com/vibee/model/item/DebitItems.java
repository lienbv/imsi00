package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DebitItems {
    private int id;
    private Date debitDate;
    private String creatorDebtor;
    private String fullName;
    private String phoneNumber;
    private BigDecimal totalAmountOwed;
    private String creatorPayer;
    private String statusCode;
    private int billId;
    private String address;
    private String typeOfDebtor;
    private Date expectedDateOfPaymentOfDebt;

}
