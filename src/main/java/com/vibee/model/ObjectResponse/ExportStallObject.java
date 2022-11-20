package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface ExportStallObject {
    String getUnitName();
    int getUnitId();
    BigDecimal getOutPrice();
    BigDecimal getOutAmount();
    BigDecimal getAmount();
    int getParentId();
    int getExportId();
    int getWarehouseId();
}
