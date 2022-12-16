package com.vibee.service.vsupplierstatistic;

import com.vibee.model.response.supplierstatistic.ImportOfSupplierResponse;
import com.vibee.model.response.supplierstatistic.SupplierStatisticResponse;

public interface SupplierStatisService {
    public SupplierStatisticResponse getAll(String nameSearch, int page, int record);
    public ImportOfSupplierResponse getImportsOfSupplier(int id, int page, int record);
    public ImportOfSupplierResponse getImportLineChart(int year, int id);
}