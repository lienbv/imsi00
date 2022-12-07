package com.vibee.model.response.redis;

import com.vibee.model.item.ExportItems;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ImportWarehouseResponse {
    private String id;
    private int productId;
    private String img;
    private String productName;
    private String barcode;
    private BigDecimal inPrice;
    private BigDecimal outPrice;
    private int typeProductId;
    private int inAmount;
    private int unitId;
    private String qrCode;
    private int supplierId;
    private String creator;
    private Date rangeDates;
    List<ExportItems> export;
}
