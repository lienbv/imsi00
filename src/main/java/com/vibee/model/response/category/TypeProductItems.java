package com.vibee.model.response.category;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TypeProductItems {
    private String id;
    private String name;
    private String description;
    private int status;
    private String statusName;
    private String amountProduct;
    private String creator;
    private Date createdDate;
    private String parentId;


    public TypeProductItems() {
        this.id = "";
        this.name = "";
        this.parentId = "";
    }
}
