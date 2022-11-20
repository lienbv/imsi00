package com.vibee.model.request.warehouse;

import com.vibee.model.item.UnitItem;
import com.vibee.model.request.BaseRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseRequest extends BaseRequest {
    private int productId;
    private BigDecimal outPrice;
    private BigDecimal inPrice;
    private Double inAmount;
    private Double outAmount;
    private int unitId;
    private int supplierId;
    private int fileId;
    private List<UnitItem> unitItems;
}
