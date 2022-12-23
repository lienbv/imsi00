package com.vibee.service.vwarehouse;

import com.vibee.model.request.warehouse.CreateWarehouseRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;
import com.vibee.model.response.warehouse.CreateWarehouseResponse;
import com.vibee.model.response.warehouse.DetailWarehouseResponse;
import com.vibee.model.response.warehouse.ImportWarehouseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CreateWarehouseService {
    CreateWarehouseResponse create(CreateWarehouseRequest request);

    ImportWarehouseResponse importFile(int supplierCode, String language,MultipartFile file);
    ImportWarehouseItemsResponse save(int supplierCode, String language);

    ImportWarehouseResponse getWarehouseBySupplier(int supplierCode, String language);

    BaseResponse saveRedis(ImportWarehouseResponse request, String language);


}
