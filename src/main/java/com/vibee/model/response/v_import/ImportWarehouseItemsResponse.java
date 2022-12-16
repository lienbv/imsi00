package com.vibee.model.response.v_import;

import com.vibee.model.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ImportWarehouseItemsResponse extends BaseResponse {
    List<ImportWarehouseItems> items;
}
