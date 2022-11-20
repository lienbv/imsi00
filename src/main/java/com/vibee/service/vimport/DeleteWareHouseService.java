package com.vibee.service.vimport;

import com.vibee.model.request.warehouse.DeleteWarehouseRequest;
import com.vibee.model.response.warehouse.DeleteWarehouseResponse;

public interface DeleteWareHouseService {
    DeleteWarehouseResponse deleteWarehouse(DeleteWarehouseRequest request);
}
