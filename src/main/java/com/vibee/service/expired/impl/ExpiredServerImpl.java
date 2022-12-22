package com.vibee.service.expired.impl;

import com.vibee.entity.VExport;
import com.vibee.entity.VImport;
import com.vibee.entity.VUnit;
import com.vibee.model.Status;
import com.vibee.model.item.CloseToExpirationItem;
import com.vibee.model.item.EditPriceExportItem;
import com.vibee.model.item.ExpirationItem;
import com.vibee.model.item.Uitem;
import com.vibee.model.request.expired.EditPriceExportRequest;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.ExpirationResponse;
import com.vibee.model.response.expired.CloseToExpiresResponse;
import com.vibee.repo.*;
import com.vibee.service.expired.ExpiredServer;
import com.vibee.utils.MessageUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Log4j2
@Service
public class ExpiredServerImpl implements ExpiredServer {
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
    public ExpirationResponse getAll(String nameSearch, int page, int record) {
        ExpirationResponse response = new ExpirationResponse();
        Pageable pageable = PageRequest.of(page, record);
        Calendar calendar = Calendar.getInstance();
//        calendar.roll(Calendar.DATE, 14);
//        System.out.println( new Date(calendar.getTimeInMillis() + 1209600000));
        Map<String, Calendar> map = this.getStartAndEndDate(new Date());
        List<VImport> imports = importRepo.getImportsByProductExpiration("%"+nameSearch+"%",pageable);
        List<ExpirationItem> closeToExpirationItems = new ArrayList<>();
        for (VImport vImport : imports) {
            int inventory = 0;

            List<Uitem> uitems = exportRepo.getAmountExportOfImport(vImport.getId());

            if (uitems != null && !uitems.isEmpty()) {
                inventory = this.convertUnitImport(vImport.getInAmount().intValue(), vImport.getUnitId()) - this.convertUnitExport(uitems);
            } else {
                inventory = vImport.getInAmount().intValue();
            }

            ExpirationItem item = new ExpirationItem();
            item.setIdImport(vImport.getId());
            item.setExpired(vImport.getExpiredDate());
            item.setDateAdded(vImport.getCreatedDate());
            item.setNameProduct(vProductRepo.getProduct(vWarehouseRepo.getById(vImport.getWarehouseId()).getProductId()).getProductName());
            item.setList(convertAmountUnit(uitems.get(uitems.size()-1).getIdUnit(), inventory, uitems));
            item.setInCome(vImport.getInMoney());
            item.setCreator(vImport.getCreator());
            item.setAmount(convertMess(item.getList()));
            closeToExpirationItems.add(item);
        }
        response.setPage(page);
        response.setPageSize(record);
//        response.setTotalPages();
        response.setTotalItems(importRepo.getImportsByProductCloseToExpiredAmount("%"+nameSearch+"%", new Date(), new Date(calendar.getTimeInMillis() + 1209600000)));
        response.setList(closeToExpirationItems);
        response.getStatus().setMessage(MessageUtils.get("vi", "msg.success"));
        response.getStatus().setStatus(Status.Success);
        return response;
    }

//    @Override
//    public BaseResponse editPriceExport(EditPriceExportRequest request) {
//        BaseResponse response = new BaseResponse();
//        for (EditPriceExportItem item : request.getList()) {
//            VExport export = exportRepo.getById(item.getIdExport());
//            export.setOutPrice(item.getPrice());
//            exportRepo.save(export);
//        }
//        VImport vImport = importRepo.getById(request.getIdImport());
//        vImport.setStatus(3);
//        importRepo.save(vImport);
//        response.getStatus().setMessage(MessageUtils.get("vi", "msg.success"));
//        response.getStatus().setStatus(Status.Success);
//        return response;
//    }

    public int getIdExport(List<Uitem> uitemsExport, int idUnit) {
        for (Uitem item : uitemsExport) {
            if (idUnit == item.getIdUnit()) {
                return item.getIdExport();
            }
        }
        return 0;
    }

    private BigDecimal getOutPRiceExport(int unitId, List<Uitem> uitemsExport) {
        for (Uitem item : uitemsExport) {
            if (unitId == item.getIdUnit()) {
                return item.getOutPrice();
            }
        }
        return BigDecimal.ZERO;
    }

    public List<Uitem> convertAmountUnit(int unitId, int inventory, List<Uitem> uitemsExport) {
        VUnit unit = vUnitRepo.getUnitById(unitId);
        List<Uitem> uitems = new ArrayList<>();
        if (unit.getParentId() == 0) {
            Uitem uitemParent  = new Uitem();
            uitemParent.setAmount(inventory);
            uitemParent.setNameUnit(unit.getUnitName());
            uitemParent.setIdUnit(unit.getId());
            uitemParent.setIdExport(this.getIdExport(uitemsExport, unit.getId()));
            uitemParent.setOutPrice(this.getOutPRiceExport(unit.getId(), uitemsExport));
            uitems.add(uitemParent);
        } else  {
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
                    uitemNow.setIdExport(this.getIdExport(uitemsExport, unitNow.getId()));
                    uitemNow.setOutPrice(this.getOutPRiceExport(unitNow.getId(), uitemsExport));
                    int resultUnitNow = inventory/unitNow.getAmount(); // tồn kho chia sl unit để ra số lượng cha của unit
                    int resultAmountUnit = resultUnitNow*unitNow.getAmount(); // sl lượng đơn vị từ cha
                    amountParentId = resultUnitNow;
                    int residual = inventory - resultAmountUnit; // lấy sl phần dư unit hiện tại

                    if (residual == 0) {
                        VUnit unitNext = units.get(i-1);
                        inventory = inventory/(unitNow.getAmount()/unitNext.getAmount());
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
            uitemNext.setIdExport(this.getIdExport(uitemsExport, units.get(0).getId()));
            uitemNext.setOutPrice(this.getOutPRiceExport(units.get(0).getId(), uitemsExport));
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
                mess = mess + "- ";
            }
        }
        return mess;
    }

    public int convertUnitExport(List<Uitem> uitems) {
        int result = 0;
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < uitems.size(); i++) {
            if (uitems.get(i).getIdUnit() != uitems.get(uitems.size() - 1).getIdUnit()) {
                VUnit unitNow = vUnitRepo.findById(uitems.get(i).getIdUnit());
                VUnit unitLess = vUnitRepo.findById(uitems.get(uitems.size()-1).getIdUnit());

                int amount = unitLess.getAmount()/unitNow.getAmount(); // được tổng sl unit con = 1 unit cha
                int value =  uitems.get(i).getAmount()*amount;
                numbers.add(value);
            } else {
                for (int j = 0; j < numbers.size(); j++) {
                    result+=numbers.get(j);
                }
                result+=uitems.get(i).getAmount();
                break;
            }
        }
        return result;
    }

    public int convertUnitImport(int amount, int idUnit) {
        int result = amount;
        VUnit vUnit = vUnitRepo.getUnitById(idUnit);
        if (vUnit.getParentId() == 0) {
            List<VUnit> units = vUnitRepo.getUnitByUnitId(vUnit.getId());
            units.add(vUnit);
            Collections.reverse(units);

            for (int i = 0; i < units.size(); i++) {
                if (units.get(i).getId() != units.get(units.size() - 1).getId()) {
                    VUnit unitNow = vUnitRepo.findById(units.get(i).getId());
                    VUnit unitLess = vUnitRepo.findById(units.get(i+1).getId());

                    int amountValue = unitLess.getAmount()/unitNow.getAmount(); // được tổng sl unit con = 1 unit cha

                    result = result*amountValue;
                } else {
                    break;
                }
            }
        } else {
            List<VUnit> list = vUnitRepo.getChildUnitASCByParentId(idUnit);
            int index = 0;
            for (int i = 0; i < list.size(); i++) {
                if (idUnit != list.get(i).getId()) {
                    index++;
                }
            }

            for (int i = index-1; i < list.size(); i++) {
                if (list.get(i).getId() != list.get(list.size() - 1).getId()) {
                    VUnit unitNow = vUnitRepo.findById(list.get(i).getId());
                    VUnit unitLess = vUnitRepo.findById(list.get(i+1).getId());

                    int amountValue = unitLess.getAmount()/unitNow.getAmount(); // được tổng sl unit con = 1 unit cha

                    result = result*amountValue;
                } else {
                    break;
                }
            }
        }

        return result;
    }

    public Map<String, Calendar> getStartAndEndDate(Date date) {
        Calendar sDate = Calendar.getInstance();
        sDate.setTime(date);
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,0);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);

        Calendar eDate = Calendar.getInstance();
        eDate.setTime(date);
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

        Map<String, Calendar> map = new HashMap<>();
        map.put("startDate", sDate);
        map.put("endDate", eDate);
        return map;
    }
}
