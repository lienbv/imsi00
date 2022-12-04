package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter

public class StaffStatisticItem {
    private int index;
    private int idBill;
    private Date createdDate;
    private String creator;
    private BigDecimal price;
    private String statusName;
    private int count;
}
