package com.vibee.model.item;

import lombok.Data;

@Data
public class UnitItems {
    private int id;
    private String name;
    private  int parentId;
    private int amount;
}
