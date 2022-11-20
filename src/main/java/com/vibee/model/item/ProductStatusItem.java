package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductStatusItem {
    private int statusCode;
    private String statusName;
}
