package com.vibee.model.request.warehouse;

import com.vibee.model.item.UnitItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportWarehouseRequest extends BaseRequest {
    private int productId;
    private List<UnitItem> unitItems;
    private double amount;
    private String image;
}
