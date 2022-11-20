package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;
import java.util.Date;

public interface GetWarehousesObject {
    String getFullName();
    int getImportId();
    Date getCreatedDate();
    Double getInventory();
    Double getInAmount();
    long getOutAmount();
    BigDecimal getInPrice();
    BigDecimal getOutPrice();
    int getStatus();
    int getCountWarehouse();
    String getUnitName();

}
