package com.vibee.model.response.v_supplier;

import com.vibee.model.item.SupplierItem;
import com.vibee.model.response.BaseResponse;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListSupplierResponse extends BaseResponse {
    private List<SupplierItem> supplierItems;
    private int totalItems;
    private int totalPages;
    private int supplierActive;
}
