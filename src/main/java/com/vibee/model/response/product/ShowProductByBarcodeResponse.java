package com.vibee.model.response.product;

import com.vibee.model.response.category.SelectionTypeProductItems;
import lombok.Data;
import java.util.Date;

@Data
public class ShowProductByBarcodeResponse {
    private int id;
    private String productName;
    private Date createdDate;
    private int statusCode;
    private String barCode;
    private String description;
    private int fileId;
    private String creator;
    private String supplierName;
    private SelectionTypeProductItems category;
}
