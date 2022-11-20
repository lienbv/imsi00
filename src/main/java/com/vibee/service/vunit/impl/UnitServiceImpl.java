package com.vibee.service.vunit.impl;

import com.vibee.entity.VUnit;
import com.vibee.model.Status;
import com.vibee.model.item.InfoUnitItem;
import com.vibee.model.response.unit.GetUnitChildReponse;
import com.vibee.repo.VUnitRepo;
import com.vibee.service.vunit.UnitService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
