package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SupplierStatisticItem {
    private int id;
    private String nameSup;
    private Date createdDate;
    private String address;
    private String email;
    private String numberPhone;
    private int status;
    private String statusName;
    private int numberOfEntry;
}
