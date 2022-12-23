package com.vibee.model.response.adminstatistic;

import com.vibee.model.Status;
import com.vibee.model.item.StatisticBill;
import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class StatisticAdminResponse extends BaseResponse {
    private BigDecimal totalPriceOfDay;
    private BigDecimal interestRateOfDay;
    private String statusTotalPriceOfDay;
    private String statusInterestRateOfDay;

    private float percentTotalPriceOfDay;
    private float percentInterestRateOfDay;

    private Date startDate;
    private Date endDate;
    //private float quantity;
    //private BigDecimal sales;
    private List<StatisticBill> statisticOfDay;

    private long block_product;
    private long sold_out;

    private int sumOrderUnConfimred;
    private int sumOrderPacking;
    private int sumOrderShipping;
    private int sumOrderCancel;

    private int totalCloseToExpired;
    private int totalExpired;

    public StatisticAdminResponse() {
        super();
    }

    public StatisticAdminResponse(long block_product, long sold_out){
        this.block_product = block_product;
        this.sold_out = sold_out;
    }

    public StatisticAdminResponse(Status status, Date startDate, Date endDate, List<StatisticBill> statisticOfDay) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.statisticOfDay = statisticOfDay;
        //this.quantity = quantity;
        //this.sales = sales;
    }

    public StatisticAdminResponse(int sumOrderUnConfimred, int sumOrderPacking, int sumOrderShipping, int sumOrderCancel){
        this.sumOrderUnConfimred = sumOrderUnConfimred;
        this.sumOrderPacking = sumOrderPacking;
        this.sumOrderShipping = sumOrderShipping;
        this.sumOrderCancel = sumOrderCancel;
    }

}
