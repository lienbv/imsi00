package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface GetImportProductObject {
    int getProductId();
    String getImg();
    String getProductName();
    int getCountWarehouse();
    Double getInAmount();
    BigDecimal getInPrice();
    int getStatus();
}
