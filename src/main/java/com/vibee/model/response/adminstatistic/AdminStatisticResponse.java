package com.vibee.model.response.adminstatistic;

import com.vibee.model.response.BaseResponse;
import lombok.Data;
import org.apache.poi.hpsf.Decimal;

import java.math.BigDecimal;

@Data
public class AdminStatisticResponse extends BaseResponse {
    private BigDecimal totalPriceOfDay;
}
