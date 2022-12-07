package com.vibee.model.response.v_import;

import com.vibee.jedis.ImportInWarehouseRedis;
import lombok.Data;

import java.util.List;

@Data
public class ListImportInWarehouseRedis {
    private List<ImportInWarehouseRedis> data;
}
