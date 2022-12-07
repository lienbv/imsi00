package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;
import java.util.Date;

public interface GetCharWarehouseObject {
    double getInAmount();
    double getOutAmount();
    Date getCreateDate();
    BigDecimal getInPrice();
    BigDecimal getOutPrice();
}
