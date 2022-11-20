package com.vibee.model.response.bill;


import java.math.BigDecimal;
import java.util.Date;

public interface GetBillResponse {
     int getIdBill();
     BigDecimal getTong();
     Date getCreatDate();
     String getFullname();
     String getAddress();
     String getCreator();
     String getStatus();
     String getpPyment_methods();
     String getTransaction_type();

}
