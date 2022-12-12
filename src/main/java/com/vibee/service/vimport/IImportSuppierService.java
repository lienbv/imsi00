package com.vibee.service.vimport;

import com.vibee.model.info.ImportWarehouseInfor;
import com.vibee.model.request.v_import.ImportInWarehouse;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.product.ShowProductByBarcodeResponse;
import com.vibee.model.response.v_import.EditImportWarehouse;
import com.vibee.model.response.v_import.ImportWarehouseItemsResponse;

import java.util.List;

public interface IImportSuppierService {
    BaseResponse importWarehouseOfSupplier(ImportInWarehouse request);
    BaseResponse update(ImportInWarehouse request, int key, String redisId);
    List<ImportWarehouseInfor> getAllProductImportOfSupplier(int key);
    ImportWarehouseItemsResponse done(List<ImportWarehouseInfor> data);
    BaseResponse deleteById(int key, String redisId, String language);
    BaseResponse deleteAll(int key, String language);
    ShowProductByBarcodeResponse showProductByBarcode(String barcode, String language);
    EditImportWarehouse edit(int key, String redisId, String language);
}
