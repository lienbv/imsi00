package com.vibee.model.response.unit;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UnitItems {
    private String label;
    private int key;
    private String parentId;
    private List<UnitItems> children;

    public UnitItems() {
        this.children = new ArrayList<>();
        this.label="";
        this.parentId="";
    }

    public void addChildren(UnitItems children){
        if(!this.children.contains(children))
            this.children.add(children);
    }
}
