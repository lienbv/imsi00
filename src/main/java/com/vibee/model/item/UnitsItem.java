package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitsItem {
    private int id;
    private String unitName;
    private Date createdDate;
    private String creator;
    private int parent;
    private String description;
    private int amount;
    private List<UnitsItem> list = new ArrayList<>();
}
