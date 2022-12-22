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
        private int id;
        private int productId;
        private String img;
        private String productName;
        private String barcode;
        private BigDecimal inPrice;
        private BigDecimal outPrice;
        private GetTypeProductResult typeProduct;
        private int inAmount;
        private GetUnitResult unit;
        private List<GetUnitResult> units;
        private String qrCode;
        private int supplierId;
        private String creator;
        private String rangeDates;
        List<ExportResult> exports;
}
