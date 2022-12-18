package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class DebitUserItems {
    private int id;
    private String fullName;
    private String phoneNumber;
    private BigDecimal total;
    private String statusCode;
    private Date debtDate;
    private String creator;
    private int idBill;
    private String typeDebt;
    List<PayItems> payItems;
}
