package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface DetailBillOfDetailDebit {
    Integer getIdBill();
    Integer getIdDetailBill();
    Integer getIdProduct();
    String getProductName();
    BigDecimal getPriceDetailBill();
    int getAmountDetailBill();
    int getUnitId();
    int getIdImport();
}
