package com.vibee.model.request.v_import;

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
public class CreateImportRequest extends BaseRequest {
    private int warehouseId;
    private List<UnitItem> unitItems;
    private int supplierId;
    private int fileId;
    private Double inAmount;
    private BigDecimal inPrice;
    private int unitId;
}
