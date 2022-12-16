package com.vibee.model.response.supplierstatistic;

import com.vibee.model.item.ImportOfSupplierItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter

public class ImportOfSupplierResponse extends BaseResponse {
    private List<ImportOfSupplierItem> importsOfSupplier;
    private List<String> productName;
    private String supplier;
    private int totalOfEntries;
    private BigDecimal totalOfPays;
    private List<Integer> lineChart = new ArrayList<>();

    private int page;
    private int pageSize;
    private int totalItems;
    private int totalPages;
}
