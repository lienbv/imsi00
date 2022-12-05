package com.vibee.model.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportWarehouseResult {
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
        List<ExportResult> export;
}
