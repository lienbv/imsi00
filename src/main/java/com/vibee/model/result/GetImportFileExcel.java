package com.vibee.model.result;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetImportFileExcel {
    private String nameProduct;
    private String barcode;
    private String supplier;
    private Date expireDate;
    private int inAmount;
//    private String type;
    private double price;
}
