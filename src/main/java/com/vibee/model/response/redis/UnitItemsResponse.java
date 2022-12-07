package com.vibee.model.response.redis;

import lombok.Data;

@Data
public class UnitItemsResponse {
    private int id;
    private String name;
    private  int parentId;
    private int amount;
}
