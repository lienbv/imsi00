package com.vibee.model.response.v_import;

import com.vibee.model.item.GetTypeProductItem;
import com.vibee.model.item.UnitItem;
import com.vibee.model.response.category.SelectionTypeProductItems;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Data
public class EditImportWarehouse {
    private String id;
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
    private String categoryName;
    private List<SelectionTypeProductItems> category;
}
