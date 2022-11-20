package com.vibee.model.result;

import com.vibee.model.ObjectResponse.ViewStallObject;
import com.vibee.model.response.category.CategoryItem;
import com.vibee.model.response.export.ExportStallItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ViewStallResult {
    private String productName;
    private int productId;
    private String barCode;
    private String img;
    private int inventory;
    private List<WarehouseStallResult> results;
}
