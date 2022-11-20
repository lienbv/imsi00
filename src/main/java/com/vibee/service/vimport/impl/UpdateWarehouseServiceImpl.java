package com.vibee.service.vimport.impl;

import com.vibee.entity.VUnit;
import com.vibee.model.Status;
import com.vibee.model.item.UnitItem;
import com.vibee.model.request.warehouse.UpdateWarehouseRequest;
import com.vibee.model.response.warehouse.UpdateWarehouseResponse;
import com.vibee.repo.VUnitRepo;
import com.vibee.repo.VImportRepo;
import com.vibee.service.vimport.UpdateWarehouseService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class UpdateWarehouseServiceImpl implements UpdateWarehouseService {
    private final VUnitRepo unitRepo;
    private final VImportRepo importRepo;

    @Autowired
    public UpdateWarehouseServiceImpl(VUnitRepo unitRepo,
                                      VImportRepo importRepo){
        this.unitRepo=unitRepo;
        this.importRepo = importRepo;
    }
    @Override
    public UpdateWarehouseResponse updateWarehouse(UpdateWarehouseRequest request) {
        log.info("updateWereHouse :: Start");
        UpdateWarehouseResponse response = new UpdateWarehouseResponse();
        int wereHouseId = request.getWereHouseId();
        List<UnitItem> unitItems = request.getUnitItems();
        String language = request.getLanguage();
        double amount = request.getAmount();
        if(unitItems.size()==0){
            log.error("updateWereHouse :: Unit not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"unit.not.found"));
            return response;
        }
        if(amount<=0){
            log.error("updateWereHouse :: Amount not found");
            response.getStatus().setStatus(Status.Fail);
            response.getStatus().setMessage(MessageUtils.get(language,"amount.not.found"));
            return response;
        }
//        Warehouse detailProduct=this.importRepo.getById(wereHouseId);
//        if (detailProduct==null){
//            log.error("updateWereHouse :: DetailProduct not found");
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language,"werehouse.not.found"));
//            return response;
//        }
//        detailProduct.setInAmount(amount);
//        Warehouse dp=this.importRepo.save(detailProduct);
//        if(dp==null){
//            log.error("updateWereHouse :: DetailProduct not found");
//            response.getStatus().setStatus(Status.Fail);
//            response.getStatus().setMessage(MessageUtils.get(language,"msg.error.update.werehouse"));
//            return response;
//        }
        List<VUnit> units=this.convertUnit(unitItems);
        this.unitRepo.saveAll(units);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage(MessageUtils.get(language,"werehouse.found"));
//        response.setWereHouseId(dp.getId());
        log.info("updateWereHouse :: End");
        return response;
    }

    private List<VUnit> convertUnit(List<UnitItem> items){
        List<VUnit> units=new ArrayList<>();
        for(UnitItem item:items){
            VUnit unit=new VUnit();
            unit.setUnitName(item.getUnitName());
            unit.setId(0);
            units.add(unit);
        }
        return units;
    }
}
