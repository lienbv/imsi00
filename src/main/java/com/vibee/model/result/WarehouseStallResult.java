package com.vibee.model.result;

import com.vibee.model.response.category.CategoryItem;
import com.vibee.model.response.export.ExportStallItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseStallResult {
    private CategoryItem categoryItem;
    private List<ExportStallItem> exportStallItems;
    private String img;
    private int warehouseId;
}
