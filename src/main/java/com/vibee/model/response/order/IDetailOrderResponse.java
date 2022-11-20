package com.vibee.model.response.order;

import java.math.BigDecimal;
import java.util.Date;

public interface IDetailOrderResponse {
    int getId_order();
    Date getSetTime();
    String getCreator();
    String getFullname();
    String getAddress();
    Date getVerified_date();
    String getName_product();
    int getQuatity_of_user();
    BigDecimal getPrice();
    BigDecimal getTotal();
}
