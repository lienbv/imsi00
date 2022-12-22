package com.vibee.model.result;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductStallResult {
    private String productCode;
    private String productName;
    private String barCode;
    private int importId;
    private String img;
    private int productId;
}
