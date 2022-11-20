package com.vibee.model.ObjectResponse;

import java.math.BigDecimal;

public interface GetReportEstObject {
    BigDecimal getInPrice();
    BigDecimal getOutPrice();
    Double getOutAmount();
    Double getInAmount();
    int getUnitId();
}
