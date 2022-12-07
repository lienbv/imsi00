package com.vibee.model.response.product;

import com.vibee.model.item.SelectExportItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedProductResult {
    private int importId;
    private String productName;
    private String img;
    private String barCode;
    private int amount;
    private String productCode;
    private List<SelectExportItem> items;
    // List<responseSelectedProduct>
}
