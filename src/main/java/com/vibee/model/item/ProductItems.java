package com.vibee.model.item;

import com.vibee.model.response.category.CategoryImportItems;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductItems {
    private int id;
    private String productName;
    private String barcode;
    private String qrCode;
    private int amount;
    private Date expiry;
    private String img;
    private BigDecimal price;
    private List<UnitItem> units;
    private List<CategoryImportItems> items;

}
