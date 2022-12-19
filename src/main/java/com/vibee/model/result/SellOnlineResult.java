package com.vibee.model.result;

import com.vibee.model.item.UnitItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SellOnlineResult {
    private int importId;
    private String productCode;
    private String productName;
    private String productImage;
    private double productQuantity;
    private List<UnitItem> unitItems;

}
