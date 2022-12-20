package com.vibee.service.closetoexpired.impl;

import com.vibee.entity.VImport;
import com.vibee.entity.VUnit;
import com.vibee.entity.VWarehouse;
import com.vibee.model.Status;
import com.vibee.model.item.CloseToExpirationItem;
import com.vibee.model.item.Uitem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.expired.CloseToExpiresResponse;
import com.vibee.repo.*;
import com.vibee.service.closetoexpired.CloseToExpiredService;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.PolyUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

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

    @Autowired
    private VExportRepo exportRepo;

    @Override
    public CloseToExpiresResponse getAll(String nameSearch, int page, int record) {
        CloseToExpiresResponse response = new CloseToExpiresResponse();
        Pageable pageable = PageRequest.of(page, record);
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.DATE, 7);
        List<VImport> imports = importRepo.getImportsByProductCloseToExpired("%"+nameSearch+"%", new Date(), calendar.getTime(), pageable);
        List<CloseToExpirationItem> closeToExpirationItems = new ArrayList<>();
        for (VImport vImport : imports) {
            int inventory = vImport.getInAmount().intValue() - exportRepo.getSUMAmountByIdImport(vImport.getId()).orElse(0).intValue();
            CloseToExpirationItem item = new CloseToExpirationItem();
            item.setIdImport(vImport.getId());
            item.setExpired(vImport.getExpiredDate());
            item.setDateAdded(vImport.getCreatedDate());
            item.setNameProduct(vProductRepo.getProduct(vWarehouseRepo.getById(vImport.getWarehouseId()).getProductId()).getProductName());
            item.setList(convertAmountUnit(vImport.getUnitId(), inventory));
            item.setInCome(vImport.getInMoney());
            item.setAmount(convertMess(item.getList()));
            closeToExpirationItems.add(item);
        }
        response.setCloseToExpirationItems(closeToExpirationItems);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.success"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

    @Override
    public BaseResponse payment(int idUnit, int amount, int idImport) {

        return null;
    }

    public List<Uitem> convertAmountUnit(int unitId, int inventory) {
        VUnit unit = vUnitRepo.getUnitById(unitId);
        List<Uitem> uitems = new ArrayList<>();
        if (unit.getParentId() == 0) {
            Uitem uitemParent  = new Uitem();
            uitemParent.setAmount(inventory);
            uitemParent.setNameUnit(unit.getUnitName());
            uitemParent.setIdUnit(unit.getId());
            uitems.add(uitemParent);
        } else  {
//            VUnit unitItem = vUnitRepo.findById(unit.getParentId());
            List<VUnit>  units = vUnitRepo.getAllUnitASCByParentId(unit.getParentId());
            int index = 0;
            for (VUnit vUnit : units) {
                if (vUnit.getId() == unit.getId()) {
                    break;
                }
                index+=1;
            }

            int amountParentId = 0;
            for (int i = index; i >= 0; i--){
                VUnit unitNow = units.get(i);
                if (unitNow.getParentId() != 0) {
                    Uitem uitemNow  = new Uitem();
                    uitemNow.setNameUnit(unitNow.getUnitName());
                    uitemNow.setIdUnit(unitNow.getId());
                    int resultUnitNow = inventory/unitNow.getAmount(); // tồn kho chia sl unit để ra số lượng cha của unit
                    int resultAmountUnit = resultUnitNow*unitNow.getAmount(); // sl lượng đơn vị từ cha
                    amountParentId = resultUnitNow;
                    int residual = inventory - resultAmountUnit; // lấy sl phần dư unit hiện tại

                    if (residual == 0) {
                        continue;
                    } else {
                        uitemNow.setAmount(residual);
                        uitems.add(uitemNow);
                    }

                    if (units.get(i-1).getParentId() == 0) {
                        break;
                    } else {
                        int amount = unitNow.getAmount()/units.get(i-1).getAmount();
                        inventory = units.get(i-1).getAmount()*resultUnitNow;
                    }
                }
            }
            Uitem uitemNext  = new Uitem();
            uitemNext.setNameUnit(units.get(0).getUnitName());
            uitemNext.setAmount(amountParentId);
            uitemNext.setIdUnit(units.get(0).getId());
            uitems.add(uitemNext);
        }
        Collections.reverse(uitems);
        return uitems;
    }

    public String convertMess(List<Uitem> uitems){
        String mess = "";
        for (int i = 0; i < uitems.size(); i++) {
            mess = mess + uitems.get(i).getAmount()+" "+uitems.get(i).getNameUnit()+" ";
            if (i != uitems.size()-1) {
                mess = mess + "lẻ ";
            }
        }
        return mess;
    }

//    public List<Uitem>
}
