package com.vibee.model.request.warehouse;

import com.vibee.model.item.UnitItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateWarehouseRequest extends BaseRequest {
    private int wereHouseId;
    private double amount;
    private String image;
    private List<UnitItem> unitItems;
    private int statusCode;
}
