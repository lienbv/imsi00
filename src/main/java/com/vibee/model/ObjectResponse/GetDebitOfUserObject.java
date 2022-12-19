package com.vibee.model.ObjectResponse;


import java.math.BigDecimal;

public interface GetDebitOfUserObject {
    Integer getIdUser();

    Integer getIdDebit();

    String getFullName();

    String getPhone();
    Integer getTypeDebt();
    BigDecimal getTotal();
    Integer getStatus();

}
