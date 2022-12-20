package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ExpirationItem {
    private int idImport;
    private String productCode;
    private String nameProduct;
    private String amount;
    private Date dateAdded;
    private Date expired;
    private BigDecimal inCome;
}
