package com.vibee.controller.catalog;

import com.vibee.model.response.unit.GetUnitChildReponse;
import com.vibee.service.vunit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/catalog")
@CrossOrigin("*")
public class CatalogController {
    private final UnitService unitService;

    @Autowired
    public CatalogController(UnitService unitService){
        this.unitService=unitService;
    }
    @GetMapping("/unit/getchild/{id}")
    public GetUnitChildReponse getUnitChild(@PathVariable("id") int id, @RequestParam("language") String language){
        return this.unitService.getUnitChild(id,language);
    }
}
