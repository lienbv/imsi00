package com.vibee.controller.statistic;

import com.vibee.model.response.supplierstatistic.ImportOfSupplierResponse;
import com.vibee.model.response.supplierstatistic.SupplierStatisticResponse;
import com.vibee.service.vsupplierstatistic.SupplierStatisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/vibee/api/v1/supplier-statistic")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class SupplierStatisticController {
    private SupplierStatisService supplierStatisService;

    @Autowired
    public SupplierStatisticController(SupplierStatisService supplierStatisService) {
        this.supplierStatisService = supplierStatisService;
    }

    @GetMapping("/get-all")
    public SupplierStatisticResponse getAll(@RequestParam(value = "name", defaultValue = "") String nameSearch,
                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                            @RequestParam(value = "record", defaultValue = "10") int record) {
        return this.supplierStatisService.getAll(nameSearch, page, record);
    }

    @GetMapping("/import/{supplier-id}")
    public ImportOfSupplierResponse getImportsOfSupplier(@PathVariable("supplier-id") int id,
                                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                                         @RequestParam(value = "record", defaultValue = "10") int record) {
        return this.supplierStatisService.getImportsOfSupplier(id, page, record);
    }

    @GetMapping("import/linechart/{supplier-id}")
    public ImportOfSupplierResponse getImportLineChart(@PathVariable("supplier-id") int id,
                                                       @RequestParam(value = "year", defaultValue = "0") int year) {
        return this.supplierStatisService.getImportLineChart(year, id);
    }
}