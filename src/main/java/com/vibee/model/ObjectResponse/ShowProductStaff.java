package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;
import java.util.Date;

public interface ShowProductStaff {

    Integer getId();
    String  getProductName();
    String getDescription();
    String getBarcode();
    String getSupplierName();
    String getQrCode();
    int getAmount();
    Date getExpired();
    String getImg();
    BigDecimal getPrice();
    int getImportID();
    int getFiles();
    int getFileImport();


}
