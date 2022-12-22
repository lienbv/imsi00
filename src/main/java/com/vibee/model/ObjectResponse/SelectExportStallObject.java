package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface SelectExportStallObject {
    int getExportId();
    int getUnitId();
    String getUnitName();
    BigDecimal getOutPrice();
    Double getInventory();
    int getAmount();
    int getUnitAmount();
}
