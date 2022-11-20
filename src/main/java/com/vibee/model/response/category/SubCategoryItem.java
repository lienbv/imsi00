package com.vibee.model.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryItem {
    private String categoryName;
    private String categoryCode;
    private int parent;
}
