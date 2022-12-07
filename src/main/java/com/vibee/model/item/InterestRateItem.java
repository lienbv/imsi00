package com.vibee.model.item;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InterestRateItem extends BaseResponse {
    private BigDecimal totalPriceCurrent;
    private BigDecimal totalPriceYesterDay;
    private int percent;
}