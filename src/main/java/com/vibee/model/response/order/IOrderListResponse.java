package com.vibee.model.response.order;

import java.math.BigDecimal;
import java.util.Date;

public interface IOrderListResponse {
    int getId();
    String getAddress();
    Date getCreated_date();
    String getCreator();
    String getFullName();
    BigDecimal getPrice_total();
    String getStatusName();
    String getPhoneNumber();
}
