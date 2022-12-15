package com.vibee.model.response.supplierstatistic;

import com.vibee.model.item.ImportOfSupplierItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class ImportOfSupplierResponse extends BaseResponse {
    private List<ImportOfSupplierItem> importsOfSupplier;

    private int page;
    private int pageSize;
    private int totalItems;
    private int totalPages;
}
