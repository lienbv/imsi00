package com.vibee.controller.catalog;

import com.vibee.model.response.category.GetTypeProductResponse;
import com.vibee.model.response.export.GetExportsByUnitSelectResponse;
import com.vibee.model.response.unit.GetUnitChildReponse;
import com.vibee.model.response.unit.GetUnitsResponse;
import com.vibee.service.v_typep_product.GetTypeProductService;
import com.vibee.service.vunit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/auth")
@CrossOrigin("*")
public class CatalogController {
    private final UnitService unitService;
    private final GetTypeProductService getTypeProductService;

    @Autowired
    public CatalogController(UnitService unitService,
                             GetTypeProductService getTypeProductService) {
        this.getTypeProductService = getTypeProductService;
        this.unitService=unitService;
    }

    @GetMapping("/unit/getchild/{id}")
    public GetUnitChildReponse getUnitChild(@PathVariable("id") int id, @RequestParam("language") String language){
        return this.unitService.getUnitChild(id,language);
    }

    @GetMapping("/type-product/get-all")
    public GetTypeProductResponse getTypeProducts(@RequestParam("language") String language){
        return this.getTypeProductService.getAll(language);
    }

    @GetMapping("/unit/get-all")
    public GetUnitsResponse getUnits(@RequestParam("language") String language){
        return this.unitService.getUnits(language);
    }

    @GetMapping("/unit/get-units-by-select-unit")
    public GetExportsByUnitSelectResponse getUnits(@RequestParam("language") String language, @RequestParam("id") int unitId){
        return this.unitService.getUnitsByUnitSelected(language,unitId);
    }
}
