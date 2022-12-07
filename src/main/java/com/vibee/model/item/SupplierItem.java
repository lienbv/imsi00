package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierItem {
    private int id;
    private String nameSup;
    private String creator;
    private Date createdDate;
    private String address;
    private String email;
    private String numberPhone;
    private int status;
    private String statusName;
}
