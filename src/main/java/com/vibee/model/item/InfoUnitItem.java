package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class InfoUnitItem {
    private String unitName;
    private int unitId;
    private String description;
    private int parentId;
    private int amount;
}
