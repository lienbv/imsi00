package com.vibee.model.response.v_import;

import com.vibee.model.info.ImportWarehouseInfor;
import lombok.Data;

import java.util.List;

@Data
public class ListImportWarehouseInforResponse {
    private List<ImportWarehouseInfor> data;
}
