package com.vibee.service.vimport;

import com.vibee.model.request.warehouse.GetWarehouseRequest;
import com.vibee.model.response.warehouse.FilterWarehouseResponse;
import com.vibee.model.response.warehouse.GetWarehousesResponse;

public interface GetWarehouseService {
    GetWarehousesResponse getWarehouses(GetWarehouseRequest request);
    FilterWarehouseResponse filterWarehouses(GetWarehouseRequest request);
}
