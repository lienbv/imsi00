package com.vibee.model.response.redis;

import lombok.Data;

import java.util.Date;

@Data
public class ImportResponse {
    private int warehouseId;
    private int status;
    private Date createdDate;
}
