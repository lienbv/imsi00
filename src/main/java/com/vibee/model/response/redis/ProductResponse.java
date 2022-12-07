package com.vibee.model.response.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProductResponse implements Serializable {
    private int id;
    private String productName;
    private int productType;
    private Date createdDate;
    private int status;
    private String barCode;
    private String description;
    private int fileId;
    private String creator;
    private String supplierName;
    private int inAmount;
    private int inPrice;
    private String productCode;
    private int unitId;
    private int supplierId;

}
