package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class CloseToExpirationItem {
    private int idImport;
    private String nameProduct;
    private String amount;
    private Date dateAdded;
    private Date expired;
    private String creator;
    private String supplier;
    private BigDecimal inCome;
    private List<Uitem> list;
}
