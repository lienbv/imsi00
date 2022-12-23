package com.vibee.model.item;

import com.vibee.model.ObjectResponse.SelectExportStallObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ShowProductItems {
    private int id;
    private String productName;
    private String description;
    private String barcode;
    private String supplierName;
    private String qrCode;
    private int amount;
    private Date expired;
    private String img;
    private BigDecimal price;
    private int importID;
    private int files;
    private int fileImport;
    List<SelectExportStallObject> unit;
}
