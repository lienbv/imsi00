package com.vibee.model.ObjectResponse;


import java.math.BigDecimal;

public interface GetDebitOfUserObject {
    Integer getAmountUserDebit();

    Integer getIdUser();

    Integer getIdDebit();

    String getAddress();

    String getFullName();

    String getPhone();

    BigDecimal getTotal();
    Integer getStatus();

}
