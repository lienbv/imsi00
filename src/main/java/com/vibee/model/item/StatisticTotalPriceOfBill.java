package com.vibee.model.item;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class StatisticTotalPriceOfBill {
    private BigDecimal totalPriceOfBill; //tổng tiền hóa đơn đó
//    private BigDecimal discountBill; //chiết khấu
//    private BigDecimal tax; //thuế
//    private BigDecimal total; // tiền đã trừ đi chiết khấu và thuế => tiền khách phải trả
    private BigDecimal payingCustomer;
    private BigDecimal refunds; //tiền thừa trả lại khách
    private String paymentType;

    public void paymentTypeOfCustomer(boolean type) {
        if (type == true) {
            this.paymentType = "tiền mặt";
        } else {
            this.paymentType = "ví điện tử";
        }
    }
}
