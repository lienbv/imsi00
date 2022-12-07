package com.vibee.model.response.supplier;

import lombok.Data;

import java.util.List;

@Data
public class ListSupplier {
    List<SupplierResponse> items;
}
