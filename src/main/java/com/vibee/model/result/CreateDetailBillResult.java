package com.vibee.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDetailBillResult {
    private int productId;
    private String productName;
    private String barCode;
    private int importId;
    private String img;
    private int amount;
    private String productCode;
    private int unitId;
    private BigDecimal outPrice;
    private int exportId;
}
