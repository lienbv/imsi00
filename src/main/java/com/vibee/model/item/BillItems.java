package com.vibee.model.item;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BillItems {
    private int id;
    private Date createDate;
    private BigDecimal total;
}
