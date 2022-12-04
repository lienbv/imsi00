package com.vibee.service.vstaffstatistic.impl;

import com.vibee.entity.VBill;
import com.vibee.model.Status;
import com.vibee.model.item.StaffStatisticItem;
import com.vibee.model.response.BaseResponse;
import com.vibee.model.response.staffstatistic.StaffStatisticResponse;
import com.vibee.repo.VBillRepo;
import com.vibee.repo.VDetailBillRepo;
import com.vibee.repo.VUserRepo;
import com.vibee.service.vstaffstatistic.StaffStatisticService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
public class StaffStatisticServiceImpl implements StaffStatisticService {

    private VBillRepo billRepo;
    private VUserRepo userRepo;
    private VDetailBillRepo  detailBillRepo;

    @Autowired
    private StaffStatisticServiceImpl(VBillRepo billRepo, VUserRepo userRepo, VDetailBillRepo  detailBillRepo) {
        this.billRepo = billRepo;
        this.userRepo = userRepo;
        this.detailBillRepo = detailBillRepo;
    }

    private final static String ASC = "asc";
    private final static String DESC = "desc";

    private static Map<Integer, String> typeFilter = new HashMap<Integer, String>();

    static {
        typeFilter.put(0, "id");
        typeFilter.put(1, "status");
        typeFilter.put(2, "created");
        typeFilter.put(3, "price");
        typeFilter.put(4, "creator");
    }

    @Override
    public StaffStatisticResponse displaỵ(String language, int numberPage, int sizePage,
                                           String idBill, String status, String createdDate,
                                           String price, String creator, String date) {
        log.info("StaffStatisticResponse :: Start");
        List<String> filters = new ArrayList<String>();
        filters.add(idBill);
        filters.add(status);
        filters.add(createdDate);
        filters.add(price);
        filters.add(creator);

        Calendar sDate = Calendar.getInstance();
        sDate.setTime(stringToDate(date));
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,0);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);
        Calendar eDate = Calendar.getInstance();
        eDate.setTime(stringToDate(date));
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

//		if (startDate.equals("") && endDate.equals("")) {
//			sDate.roll(Calendar.DATE, -7);
//		}
        System.out.println(sDate.getTime() +"---"+ eDate.getTime()+"-date: "+date);
        StaffStatisticResponse response = new StaffStatisticResponse();
        Pageable page = PageRequest.of(numberPage, sizePage);
        List<VBill> bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime(),page).getContent();
        List<VBill> billAll = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime());

        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).equalsIgnoreCase(ASC)) {
                page =  PageRequest.of(numberPage, sizePage, Sort.by(Sort.Direction.ASC, typeFilter.get(i)));
                bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime(),page).getContent();
                break;
            } else if (filters.get(i).equalsIgnoreCase(DESC)) {
                page =  PageRequest.of(numberPage, sizePage, Sort.by(Sort.Direction.DESC, typeFilter.get(i)));
                bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime(),page).getContent();
                break;
            }
        }


        List<StaffStatisticItem> statisticItem = new ArrayList<StaffStatisticItem>();
        int index = 0;
        for (VBill bill : bills) {
            StaffStatisticItem item = new StaffStatisticItem();
            item.setIndex(++index);
            item.setIdBill(bill.getId());
            item.setCreatedDate(bill.getCreatedDate());
            //item.setCreator(userRepo.getById(bill.getCreator()).getFullname());
            item.setPrice(bill.getPrice());
            item.setStatusName(Utiliies.convertStatusStatistic(bill.getStatus()));
            item.setCount(Integer.parseInt(detailBillRepo.findBySumQuantity(1, bill.getId()).orElse(0L)+""));
            statisticItem.add(item);
        }

        int totalCountOfProduct = 0;

        for (VBill bill : billAll) {
            totalCountOfProduct += Integer.parseInt(detailBillRepo.findBySumQuantity(1, bill.getId()).orElse(0L)+"");
        }

        int totalItems = billRepo.findAllBillByDate(sDate.getTime(), eDate.getTime()).size();

        response.setPage(numberPage);
        response.setPageSize(sizePage);
        response.setTotalItems(totalItems);
        response.setTotalPages(Math.round(totalItems/sizePage));
        response.setList(statisticItem);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.success"));
        response.getStatus().setStatus(Status.Success);
        BigDecimal totalPriceOfBills = new BigDecimal(billRepo.findByTotalPriceOfBills(sDate.getTime(), eDate.getTime()).orElse(0L));
        response.setTotalPriceOfBills(totalPriceOfBills);
        response.setCountBills(totalItems);
        response.setCountProducts(totalCountOfProduct);

        log.info("StaffStatisticResponse :: End");
        return response;
    }

    public Date stringToDate(String string) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(string);
            return date;
        } catch (Exception e) {
            return new Date();
        }
    }

    @Override
    public BaseResponse export(String language, int numberPage, int sizePage,
                               String idBill, String status, String createdDate,
                               String price, String creator, String date) {
        log.info("exportExcel :: Start");
        BaseResponse response = new BaseResponse();
        List<StaffStatisticItem> statistic = this.getDate(language,
                numberPage,
                sizePage,
                idBill,
                status,
                createdDate,
                price,
                creator,
                date).getList();

        String nowTittle = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss").format(Calendar.getInstance().getTime());
        String now = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());

        try(InputStream is = new FileInputStream(new File("src/main/resources/static/template_staff_statistic.xlsx"));) {
            //System.out.println(new File("target/a.xlsx").getAbsolutePath());
            try (OutputStream os = new FileOutputStream("src/main/resources/static/"+nowTittle+"_staff_statistic_output.xlsx");) {
                Context context = new Context();
                context.putVar("statistics", statistic);
                context.putVar("now", now);
                JxlsHelper.getInstance().processTemplate(is, os, context);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.getStatus().setMessage(MessageUtils.get(language, "msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("exportExcel :: End");
        return response;
    }


    public StaffStatisticResponse getDate(String language, int numberPage, int sizePage,
                                          String idBill, String status, String createdDate,
                                          String price, String creator, String date
    ) {
        log.info("StaffStatisticResponse :: Start");
        List<String> filters = new ArrayList<String>();
        filters.add(idBill);
        filters.add(status);
        filters.add(createdDate);
        filters.add(price);
        filters.add(creator);

        Calendar sDate = Calendar.getInstance();
        sDate.setTime(stringToDate(date));
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,0);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);
        Calendar eDate = Calendar.getInstance();
        eDate.setTime(stringToDate(date));
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

        //System.out.println(sDate.getTime() +"---"+ eDate.getTime()+"-date: "+date);
        StaffStatisticResponse response = new StaffStatisticResponse();
        Pageable page;
        List<VBill> bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime());

        for (int i = 0; i < filters.size(); i++) {
            if (filters.get(i).equalsIgnoreCase(ASC)) {
                page =  PageRequest.of(0, bills.size(), Sort.by(Sort.Direction.ASC, typeFilter.get(i)));
                bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime(),page).getContent();
                break;
            } else if (filters.get(i).equalsIgnoreCase(DESC)) {
                page =  PageRequest.of(0, bills.size(), Sort.by(Sort.Direction.DESC, typeFilter.get(i)));
                bills = billRepo.findAllBillByDateAndPage(sDate.getTime(), eDate.getTime(),page).getContent();
                break;
            }
        }


        List<StaffStatisticItem> statisticItem = new ArrayList<StaffStatisticItem>();
        int index = 0;
        for (VBill bill : bills) {
            StaffStatisticItem item = new StaffStatisticItem();
            item.setIndex(++index);
            item.setIdBill(bill.getId());
            item.setCreatedDate(bill.getCreatedDate());
            //item.setCreator(userRepo.getById(bill.getCreator()).getFullname());
            item.setPrice(bill.getPrice());
            item.setStatusName(Utiliies.convertStatusStatistic(bill.getStatus()));
            item.setCount(Integer.parseInt(detailBillRepo.findBySumQuantity(1, bill.getId()).orElse(0L)+""));
            statisticItem.add(item);
        }

        response.setList(statisticItem);
        response.getStatus().setMessage(MessageUtils.get(language, "msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("StaffStatisticResponse :: End");
        return response;
    }
}
