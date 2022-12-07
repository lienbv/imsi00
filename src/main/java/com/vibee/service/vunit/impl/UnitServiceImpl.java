package com.vibee.service.vunit.impl;

import com.vibee.entity.VUnit;
import com.vibee.model.Status;
import com.vibee.model.item.InfoUnitItem;
import com.vibee.model.item.UnitItemEdit;
import com.vibee.model.item.UnitsItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.export.GetExportsByUnitSelectResponse;
import com.vibee.model.response.unit.GetUnitChildReponse;
import com.vibee.model.result.ExportResult;
import com.vibee.model.result.GetUnitResult;
import com.vibee.repo.VUnitRepo;
import com.vibee.service.vunit.UnitService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.vibee.model.request.v_unit.UnitDeleteParentRequest;
import com.vibee.model.request.v_unit.UnitRequest;
import com.vibee.model.response.unit.GetUnitsResponse;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Log4j2
public class UnitServiceImpl implements UnitService {
    private final VUnitRepo unitRepo;

    @Autowired
    public UnitServiceImpl(VUnitRepo unitRepo){
        this.unitRepo=unitRepo;
    }

    @Override
    public GetUnitChildReponse getUnitChild(int paretnId, String language) {
        log.info("UnitService - GetUnitChild :: START");
        GetUnitChildReponse reponse=new GetUnitChildReponse();
        List<VUnit> units = this.unitRepo.getAllUnitByParentId(paretnId);
        if (units==null||units.size()==0){
            log.error("unit is not found :: id="+paretnId);
            reponse.getStatus().setStatus(Status.Fail);
            reponse.getStatus().setMessage(MessageUtils.get(language,"error.unit.not.found"));
            return reponse;
        }
        List<InfoUnitItem> unitItems=this.convertUnitItems(units);
        reponse.setUnitItems(unitItems);
        reponse.getStatus().setStatus(Status.Success);
        reponse.getStatus().setMessage(MessageUtils.get(language,""));
        log.info("UnitService - GetUnitChild :: END");
        return reponse;
    }

    private List<InfoUnitItem> convertUnitItems(List<VUnit> units){
        List<InfoUnitItem> unitItems=new ArrayList<>();
        for(VUnit unit:units){
            InfoUnitItem item=new InfoUnitItem();
            item.setParentId(unit.getParentId());
            item.setUnitId(unit.getId());
            item.setAmount(unit.getAmount());
            item.setDescription(unit.getDescription());
            item.setUnitName(unit.getUnitName());
            unitItems.add(item);
        }
        return unitItems;
    }

    @Override
    public GetUnitsResponse getAll(String name, int page, int record) {
        log.info("UnitService-getAll :: Start");
        GetUnitsResponse response = new GetUnitsResponse();
        Pageable pageable = PageRequest.of(page, record);
        List<UnitsItem> responseUnits = new ArrayList<>();
        List<VUnit> units = new ArrayList<>();
        if (name.equals("")) {
            units = unitRepo.getAllUnitParents(pageable).getContent();
            int sizeItem = unitRepo.getAllUnitParents().size();
            response.setTotalItems(sizeItem);
            response.setTotalPages(Math.round(sizeItem/record));
        } else {
            units = unitRepo.findByUnitName("%"+name+"%", pageable).getContent();
            int sizeItem = unitRepo.findByUnitName("%"+name+"%").size();
            response.setTotalItems(sizeItem);
            response.setTotalPages(Math.round(sizeItem/record));
        }
        for (VUnit unit : units) {
            UnitsItem unitsItem = setUnitsItem(unit);
            List<UnitsItem> responseUnitChild = new ArrayList<>();
            List<VUnit> unitChildren = unitRepo.getUnitsByParentId(unit.getId());
            for (VUnit unitChild : unitChildren) {
                UnitsItem  unitsItemChild = setUnitsItem(unitChild);
                responseUnitChild.add(unitsItemChild);
            }
            unitsItem.setList(responseUnitChild);
            responseUnits.add(unitsItem);
        }
        response.setList(responseUnits);
        response.setPage(page);
        response.setPageSize(record);

        response.getStatus().setMessage(MessageUtils.get("","msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("UnitService-getAll :: End");
        return response;
    }

    private UnitsItem setUnitsItem(VUnit unit) {
        UnitsItem unitsItem = new UnitsItem();
        unitsItem.setId(unit.getId());
        unitsItem.setCreator(unit.getCreator());
        unitsItem.setUnitName(unit.getUnitName());
        unitsItem.setDescription(unit.getDescription());
        unitsItem.setAmount(unit.getAmount());
        unitsItem.setCreatedDate(unit.getCreatedDate());
        unitsItem.setParent(unit.getParentId());
        return unitsItem;
    }

    @Override
    public VUnit save(UnitRequest request) {
        log.info("UnitService-save :: Start");
        VUnit unit = new VUnit();
        unit.setUnitName(request.getUnitName());
        unit.setAmount(request.getAmount());
        unit.setDescription(request.getDescription());
        unit.setParentId(request.getParentId());
        unit.setCreatedDate(new Date());
        //unit.setCreator();
        VUnit save = unitRepo.save(unit);
        if (save.getParentId() == 0) {
            if(request.getChildId() != 0) {
                VUnit childUnit = unitRepo.findById(request.getChildId());
                List<VUnit> unitChildrenOld = unitRepo.getAllUnitByParentId(childUnit.getId());
                for (VUnit item : unitChildrenOld) {
                    item.setParentId(save.getId());
                    unitRepo.save(item);
                }
                childUnit.setParentId(save.getId());
                unitRepo.save(childUnit);
            }
        }
        log.info("UnitService-save :: End");
        return unit;
    }

    @Override
    public VUnit update(UnitRequest request) {
        log.info("UnitService-update :: Start");
        VUnit unitOld = unitRepo.findById(request.getId());
        VUnit unit = new VUnit();
        unit.setId(request.getId());
        unit.setUnitName(request.getUnitName());
        unit.setAmount(request.getAmount());
        unit.setDescription(request.getDescription());
        unit.setParentId(unitOld.getParentId());
        unit.setCreatedDate(new Date());
        //unit.setCreator();

        VUnit save = unitRepo.save(unit);
        log.info("UnitService-update :: End");
        return unit;
    }

    @Override
    public BaseResponse delete(int id) {
        log.info("UnitService-delete :: Start");
        VUnit unit = unitRepo.findById(id);
        unitRepo.delete(unit);
        log.info("UnitService-delete :: End");
        return null;
    }

    @Override
    public BaseResponse deleteUnitParent(UnitDeleteParentRequest request) {
        log.info("UnitService-deleteUnitParent :: Start");
        BaseResponse response = new BaseResponse();
        VUnit unitParent = unitRepo.findById(request.getIdParent());
        unitParent.setParentId(0);
        unitParent.setAmount(1);
        unitRepo.save(unitParent);
        for (int i = 0; i < request.getListEdit().length; i++) {
            UnitItemEdit itemEdit = request.getListEdit()[i];
            VUnit item = unitRepo.findById(itemEdit.getId());
            item.setAmount(itemEdit.getAmount());
            item.setParentId(request.getIdParent());
            unitRepo.save(item);
        }
        unitRepo.deleteById(request.getIdDelete());
        response.getStatus().setMessage(MessageUtils.get("","msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("UnitService-deleteUnitParent :: End");
        return response;
    }

    @Override
    public GetUnitsResponse getUnits(String language) {
        log.info("UnitService-getUnits :: Start");
        GetUnitsResponse response = new GetUnitsResponse();
        List<GetUnitResult> results= new ArrayList<>();
        List<VUnit> units = unitRepo.getAllUnits();
        if (units.isEmpty()){
            log.error("UnitService-getUnits :: End :: Unit is empty");
            response.getStatus().setMessage(MessageUtils.get(language,"msg.notFound"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        for (VUnit unit : units) {
            GetUnitResult result = new GetUnitResult();
            result.setId(unit.getId());
            result.setName(unit.getUnitName());
            result.setStatusCode(unit.getStatus());
            result.setDescription(unit.getDescription());
            result.setParentId(unit.getParentId());
            result.setStatusName(ProductUtils.statusname(unit.getStatus()));
            results.add(result);
        }
        response.setResults(results);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success"));
        log.info("UnitService-getUnits :: End");
        return response;
    }

    @Override
    public GetExportsByUnitSelectResponse getUnitsByUnitSelected(String language, int unitId) {
        log.info("UnitService-getUnitsByUnitSelected :: Start :: unitId = {}", unitId);
        GetExportsByUnitSelectResponse response = new GetExportsByUnitSelectResponse();
        List<ExportResult> results= new ArrayList<>();
        List<VUnit> units = unitRepo.getAllUnitByParentId(unitId);
        if (units.isEmpty()){
            log.error("UnitService-getUnits :: End :: Unit is empty");
            response.getStatus().setMessage(MessageUtils.get(language,"msg.notFound"));
            response.getStatus().setStatus(Status.Fail);
            return response;
        }
        for (VUnit unit : units) {
            ExportResult result = new ExportResult();
            result.setUnitId(unit.getId());
            result.setUnitName(unit.getUnitName());
            results.add(result);
        }
        response.setResults(results);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"msg.success"));
        log.info("UnitService-getUnitsByUnitSelected :: End :: unitId = {}", unitId);
        return response;
    }
}
