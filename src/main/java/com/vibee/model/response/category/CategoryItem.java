package com.vibee.model.response.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryItem {
    private String categoryName;
    private int categoryCode;
    private List<SubCategoryItem> subCategoryItems;
}
