package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface GetProductObject {
    int getProductId();
    int getFileId();
    String getProductName();
    int getCountWarehouse();
    Double getInAmount();
    Double getOutAmount();
    BigDecimal getInPrice();
    BigDecimal getOutPrice();
    int getStatus();
}
