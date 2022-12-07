package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface GetExportsObject {
    String getUnitName();
    int getUnit();
    BigDecimal getInPrice();
    BigDecimal getOutPrice();
}
