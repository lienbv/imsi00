package com.vibee.service.vwarehouse;

import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;

public interface CreateWarehouseService {
    CreateWarehouseResponse create(CreateWarehouseRequest request);
}
