package com.vibee.model.item;

import lombok.Data;

@Data
public class ProductItems {
    private int id;
    private String productName;
    private String barcode;
    private String qrCode;
    private int amount;

}
