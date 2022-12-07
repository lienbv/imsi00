package com.vibee.model.response.redis;

import com.vibee.entity.VExport;
import com.vibee.entity.VImport;
import com.vibee.entity.VProduct;
import com.vibee.entity.VWarehouse;
import lombok.Data;

import java.util.List;

@Data
public class ListAllRedis {
    private List<VProduct> productResponses;
    private List<VImport> importResponses;
    private List<VExport>exportResponses;
    private List<VWarehouse> warehouseResponses;
}
