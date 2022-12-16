package com.vibee.model.response.supplierstatistic;

import com.vibee.model.item.ImportOfSupplierItem;
import com.vibee.model.item.SupplierStatisticItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class SupplierStatisticResponse extends BaseResponse {
    private List<SupplierStatisticItem> list;

    private int page;
    private int pageSize;
    private int totalItems;
    private int totalPages;
}
