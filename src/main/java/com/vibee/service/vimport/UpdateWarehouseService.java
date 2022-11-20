package com.vibee.service.vimport;

import com.vibee.model.request.warehouse.UpdateWarehouseRequest;
import com.vibee.model.response.warehouse.UpdateWarehouseResponse;

public interface UpdateWarehouseService {
    UpdateWarehouseResponse updateWarehouse(UpdateWarehouseRequest request);
}
