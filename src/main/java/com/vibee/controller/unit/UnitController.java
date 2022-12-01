package com.vibee.controller.unit;

import com.vibee.entity.VUnit;
import com.vibee.model.request.v_unit.UnitDeleteParentRequest;
import com.vibee.model.request.v_unit.UnitRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.unit.GetUnitsResponse;
import com.vibee.service.vunit.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vibee/api/v1/admins/unit")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UnitController {
    private final UnitService unitService;

    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    //display table
    @GetMapping("")
    public GetUnitsResponse getAll(
            @RequestParam(name = "nameSearch", defaultValue = "") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "record", defaultValue = "10") int record) {
        return unitService.getAll(name, page, record);
    }

    @PostMapping("")
    public VUnit save(@RequestBody UnitRequest request) {
        return unitService.save(request);
    }

    @PutMapping("")
    public VUnit update(@RequestBody UnitRequest request) {
        return unitService.update(request);
    }

    @DeleteMapping("/{id}")
    public BaseResponse delete(@PathVariable("id") int id) {
        return unitService.delete(id);
    }

    @PostMapping("/delete")
    public BaseResponse deleteParentUnit(@RequestBody UnitDeleteParentRequest request) {
        return unitService.deleteUnitParent(request);
    }

}
