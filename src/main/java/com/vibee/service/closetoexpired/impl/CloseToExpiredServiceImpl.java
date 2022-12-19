package com.vibee.service.closetoexpired.impl;

import com.vibee.entity.VImport;
import com.vibee.entity.VUnit;
import com.vibee.entity.VWarehouse;
import com.vibee.model.item.CloseToExpirationItem;
import com.vibee.model.item.Uitem;
import com.vibee.model.response.expired.CloseToExpiresResponse;
import com.vibee.repo.VImportRepo;
import com.vibee.repo.VProductRepo;
import com.vibee.repo.VUnitRepo;
import com.vibee.repo.VWarehouseRepo;
import com.vibee.service.closetoexpired.CloseToExpiredService;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
public class CloseToExpiredServiceImpl implements CloseToExpiredService {

    @Autowired
    private VImportRepo importRepo;

    @Autowired
    private VWarehouseRepo vWarehouseRepo;

    @Autowired
    private VProductRepo vProductRepo;

    @Autowired
    private VUnitRepo vUnitRepo;

    @Override
    public CloseToExpiresResponse getAll(String nameSearch, int page, int record) {
        CloseToExpiresResponse response = new CloseToExpiresResponse();
        Pageable pageable = PageRequest.of(page, record);
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DATE, 7);
        List<VImport> imports = importRepo.getImportsByProduct("%"+nameSearch+"%", new Date(), calendar.getTime(), pageable);
        List<CloseToExpirationItem> closeToExpirationItems = new ArrayList<>();
        for (VImport vImport : imports) {
            CloseToExpirationItem item = new CloseToExpirationItem();
            item.setExpired(vImport.getExpiredDate());
            item.setDateAdded(vImport.getCreatedDate());
            item.setNameProduct(vProductRepo.getProductById(vWarehouseRepo.getById(vImport.getWarehouseId()).getProductId()).getProductName());
            VUnit unit = vUnitRepo.getById(vImport.getUnitId());
            if (unit.getParentId() == 0) {
                List<Uitem> uitems = new ArrayList<>();
                List<VUnit>  units = vUnitRepo.getAllUnitDESCByParentId(unit.getId());
                for (VUnit vUnit : units) {
                    Uitem uitem  = new Uitem();
                    uitem.setAmount(vUnit.getAmount());
                    uitem.setNameUnit(vUnit.getUnitName());
                }

            } else  {
                VUnit parent = vUnitRepo.findById(unit.getParentId());
                List<Uitem> uitems = new ArrayList<>();
                List<VUnit>  units = vUnitRepo.getAllUnitDESCByParentId(parent.getParentId());
                for (VUnit vUnit : units) {
                    Uitem uitem  = new Uitem();
                    uitem.setAmount(vUnit.getAmount());
                    uitem.setNameUnit(vUnit.getUnitName());
                }
            }
        }
        return null;
    }
}
