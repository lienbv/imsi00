package com.vibee.model.response.staffstatistic;

import com.vibee.model.item.StaffStatisticItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;


@Setter
@Getter

public class StaffStatisticResponse extends BaseResponse {
    private List<StaffStatisticItem> list;
    private int totalItems;
    private int totalPages;
    private int page;
    private int pageSize;

    private BigDecimal totalPriceOfBills;
    private BigDecimal totalPriceOfDiscount;
    private BigDecimal actualAmount;
    private int countBills;
    private int countProducts;
}
