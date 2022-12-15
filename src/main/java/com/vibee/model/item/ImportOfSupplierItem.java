package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter

public class ImportOfSupplierItem {
    private String productName;
    private String productCode;
    private Date createdDate;
    private Date expiredDate;
    private String creator;
    private Double inAmount;
    private BigDecimal inMoney;
    private String unitName;
    private BigDecimal totalPurchasePrice;
}
