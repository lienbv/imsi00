package com.vibee.model.response.warehouse;

import com.vibee.model.response.BaseResponse;
import com.vibee.model.result.ImportWarehouseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ImportWarehouseResponse extends BaseResponse {
    private int supplierCode;
    private List<ImportWarehouseResult> products;
}
