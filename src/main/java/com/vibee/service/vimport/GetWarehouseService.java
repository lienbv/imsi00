package com.vibee.service.vimport;

import com.vibee.model.item.GetExportItems;
import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.warehouse.DetailWarehouseResponse;
import com.vibee.model.response.warehouse.FilterWarehouseResponse;
import com.vibee.model.response.warehouse.GetWarehousesResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface GetWarehouseService {
    GetWarehousesResponse getWarehouses(GetWarehouseRequest request);
    FilterWarehouseResponse filterWarehouses(GetWarehouseRequest request);

    DetailWarehouseResponse getDetailWarehouse(int importId, String language);

    BaseResponse updateWarehouse(String language, GetExportItems request);
}
