package com.vibee.model.response.debit;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class DebitDetailResponse extends BaseResponse {
    private String productName;
    private int amount;
    private int unitId;
    private BigDecimal price;
    private BigDecimal totalDebit;
    private int debitId;
    private String unitName;

}
