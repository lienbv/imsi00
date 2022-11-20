package com.vibee.model.response.product;

import java.math.BigDecimal;

public interface IgetHomeSellOnline {
    Integer getImportId();
    String getBaseCode();
    String getProductName();
    String getFileName();
    Double getOutAmount();
    BigDecimal getOutPrice();
}
