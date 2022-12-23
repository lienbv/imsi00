package com.vibee.model.ObjectResponse;

import java.util.Date;

public interface ShowProductObjectItems {
    Integer getProductId();
    String getBarCode();
    int getFileId();
    String getProductCode();
    String getNameProduct();
    String getSupplierName();
    Date getExpired();

}
