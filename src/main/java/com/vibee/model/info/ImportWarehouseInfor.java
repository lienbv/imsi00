package com.vibee.model.info;

import com.vibee.model.item.UnitItem;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ImportWarehouseInfor {
    private int id;
    private int productId;
    private String image;
    private String productName;
    private int status;
    private String barcode;
    private int img;
    private BigDecimal inPrice;
    private int typeProductId;
    private int inAmount;
    private int unitId;
    private int supplierId;
    private String description;
    private List<UnitItem> exportsItems;
    private String creator;
    private String supplierName;
    private String rangeDate;
    private String unit;
}
