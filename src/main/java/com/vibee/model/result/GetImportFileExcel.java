package com.vibee.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.hpsf.Decimal;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetImportFileExcel {
    private String nameProduct;
    private String barcode;
    private String supplier;
    private Date expireDate;
    private int inAmount;
    private String type;
    private double price;
}
