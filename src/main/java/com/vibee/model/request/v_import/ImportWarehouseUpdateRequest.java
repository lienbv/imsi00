package com.vibee.model.request.v_import;

import com.vibee.model.item.UnitItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImportWarehouseUpdateRequest {
    private String nameProd;
    private String unit;
    private String description;
    private int amount;
    private int categoryId;
    private int supplierId;
    private String barCode;
    private int unitId;
    private List<UnitItem> units;
    private BigDecimal inPrice;
    private int fileId;
    private String rangeDates;
    private String supplierName;

}
