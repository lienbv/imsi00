package com.vibee.service.vstatisticadmin.Impl;

import com.vibee.entity.VBill;
import com.vibee.entity.VProduct;
import com.vibee.entity.VTypeProduct;
import com.vibee.model.Status;
import com.vibee.model.item.GetProductItem;
import com.vibee.model.item.InterestRateItem;
import com.vibee.model.item.StatisticBill;
import com.vibee.model.request.BaseRequest;
import com.vibee.model.response.adminstatistic.ReportTopProductResponse;
import com.vibee.model.response.adminstatistic.StatisticAdminResponse;
import com.vibee.model.response.adminstatistic.TypeProductResponse;
import com.vibee.repo.*;
import com.vibee.service.vstatisticadmin.StatisticAdminService;
import com.vibee.utils.MessageUtils;
import com.vibee.utils.ProductUtils;
import com.vibee.utils.Utiliies;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Log4j2
@Service
public class StatisticAdminServiceImpl implements StatisticAdminService {
    @Autowired
    private VBillRepo billRepo;

    @Autowired
    private VDetailBillRepo detailBillRepo;

    @Autowired
    private VProductRepo productRepo;

    @Autowired
    private VOrderRepo orderRepo;

    @Autowired
    private VFileUploadRepo vFileUploadRepo;

    @Override
    public StatisticAdminResponse totalPriceOfDay() {
        StatisticAdminResponse response = new StatisticAdminResponse();
        Map<String, Calendar> now = this.getStartAndEndDate(new Date());

        BigDecimal totalPriceOfBillsOfDay = new BigDecimal(billRepo.findByTotalPriceOfBills(now.get("startDate").getTime(), now.get("endDate").getTime()).orElse(0L));
        List<Optional<Long>> interestRatesOfDay = billRepo.getInterestRateOfDay(now.get("startDate").getTime(), now.get("endDate").getTime());
        BigDecimal interestRateOfDay = BigDecimal.ZERO;
        for (Optional<Long> aLong: interestRatesOfDay) {
            interestRateOfDay = interestRateOfDay.add(new BigDecimal(aLong.orElse(0L).longValue()));
        }

        Calendar yesterdayCalender = Calendar.getInstance();
        yesterdayCalender.setTime(new Date());
        yesterdayCalender.roll(Calendar.DATE, -1);
        Map<String, Calendar> yesterday = getStartAndEndDate(yesterdayCalender.getTime());

        BigDecimal totalPriceOfBillsOfYesterday = new BigDecimal(billRepo.findByTotalPriceOfBills(yesterday.get("startDate").getTime(), yesterday.get("endDate").getTime()).orElse(0L));
        List<Optional<Long>> interestRatesOfYesterday = billRepo.getInterestRateOfDay(yesterday.get("startDate").getTime(), yesterday.get("endDate").getTime());
        BigDecimal interestRateOfYesterday = BigDecimal.ZERO;
        for (Optional<Long> aLong: interestRatesOfYesterday) {
            interestRateOfYesterday = interestRateOfYesterday.add(new BigDecimal(aLong.orElse(0L).longValue()));
        }

        //status
        if (totalPriceOfBillsOfDay.compareTo(totalPriceOfBillsOfYesterday) == 0) {
            response.setStatusTotalPriceOfDay("equal");
        } else if (totalPriceOfBillsOfDay.compareTo(totalPriceOfBillsOfYesterday) == 1) {
            response.setStatusTotalPriceOfDay("more");
        } else {
            response.setStatusTotalPriceOfDay("less");
        }

        //status
        if (interestRateOfDay.compareTo(interestRateOfYesterday) == 0) {
            response.setStatusInterestRateOfDay("equal");
        } else if (interestRateOfDay.compareTo(interestRateOfYesterday) == 1) {
            response.setStatusInterestRateOfDay("more");
        } else {
            response.setStatusInterestRateOfDay("less");
        }

        float percentTotalPrice = 0;
        float percentInterestRate = 0;
        //percent
        try{
            if (interestRateOfYesterday.intValue() == 0 && interestRateOfDay.intValue() == 0){
                percentTotalPrice = 0;
            } else if (interestRateOfYesterday.intValue() == 0) {
                percentTotalPrice = 100;
            } else if (interestRateOfDay.intValue() == 0) {
                percentTotalPrice = -100;
            } else {
                BigDecimal subtractTotalPrice = interestRateOfDay.subtract(interestRateOfYesterday, MathContext.DECIMAL128);
                BigDecimal divideTotalPrice = subtractTotalPrice.divide(interestRateOfYesterday,MathContext.DECIMAL128);
                BigDecimal multiTotalPrice = divideTotalPrice.multiply(new BigDecimal(100), MathContext.DECIMAL128);
                percentTotalPrice = multiTotalPrice.floatValue();
            }
        } catch (Exception e) {
//            percentTotalPrice = 100;
            e.printStackTrace();
        }

        System.out.println(interestRateOfDay.intValue()+"/"+interestRateOfYesterday.intValue());

        try{
            if (interestRateOfYesterday.intValue() == 0 && interestRateOfDay.intValue() == 0) {
                percentInterestRate = 0;
            } else if (interestRateOfYesterday.intValue() == 0) {
                percentInterestRate = 100;
            } else if (interestRateOfDay.intValue() == 0) {
                percentInterestRate = -100;
            } else {
                BigDecimal subtractTotalPrice = totalPriceOfBillsOfDay.subtract(totalPriceOfBillsOfYesterday, MathContext.DECIMAL128);
                BigDecimal divideTotalPrice = subtractTotalPrice.divide(totalPriceOfBillsOfYesterday,MathContext.DECIMAL128);
                BigDecimal multiTotalPrice = divideTotalPrice.multiply(new BigDecimal(100), MathContext.DECIMAL128);
                percentInterestRate = multiTotalPrice.floatValue();
            }
        } catch (Exception e) {
//            percentInterestRate = 100;
            e.printStackTrace();
        }

        response.setPercentTotalPriceOfDay(percentTotalPrice);
        response.setPercentInterestRateOfDay(percentInterestRate);
        System.out.println(percentTotalPrice+"/"+percentInterestRate);
        response.setInterestRateOfDay(interestRateOfDay);
        response.setTotalPriceOfDay(totalPriceOfBillsOfDay);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        return response;
    }

    //không dùng
    @Override
    public InterestRateItem interestRate() {
        log.info("AdminStatisticResponse-interestRate :: Start");
        InterestRateItem response = new InterestRateItem();
        Date now = new Date();
        Map<String, Calendar> current = getStartAndEndDate(now);
        Calendar yesterdayCalender = Calendar.getInstance();
        yesterdayCalender.setTime(now);
        yesterdayCalender.roll(Calendar.DATE, -1);
        Map<String, Calendar> yesterday = getStartAndEndDate(yesterdayCalender.getTime());

        BigDecimal totalCurrent = getTotalPriceOfDay(current.get("startDate"), current.get("endDate"));
        BigDecimal totalYesterday = getTotalPriceOfDay(yesterday.get("startDate"), yesterday.get("endDate"));
        response.setTotalPriceCurrent(totalCurrent);
        response.setTotalPriceYesterDay(totalYesterday);

        try {
            BigDecimal subtract = totalCurrent.subtract(totalYesterday, MathContext.DECIMAL128);
            BigDecimal divide = subtract.divide(totalYesterday,MathContext.DECIMAL128);
            BigDecimal multi = divide.multiply(new BigDecimal(100), MathContext.DECIMAL128);
            response.setPercent(multi.intValue());
            log.info("AdminStatisticResponse-interestRate :: End");
            return response;
        } catch (Exception e) {
            log.info("AdminStatisticResponse-interestRate :: End");
            return response;
        }


    }

    @Override
    public StatisticAdminResponse statisticDisplay7Days(BaseRequest request, String startDate, String endDate) {
        StatisticAdminResponse response = new StatisticAdminResponse();
        log.info("AdminStatisticResponse :: Start");

        Calendar sDate = Calendar.getInstance();
        sDate.setTime(stringToDate(startDate));
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,1);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);

        Calendar eDate = Calendar.getInstance();
        eDate.setTime(stringToDate(endDate));
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

        if (startDate.equals("") && endDate.equals("")) {
            sDate.roll(Calendar.DATE, -7);
        }

        List<StatisticBill> statisticOfDay = new ArrayList<>();

        Calendar dateLoop = Calendar.getInstance();
        dateLoop.setTime(stringToDate(startDate));
        for (Calendar date = dateLoop; date.before(eDate); date.roll(Calendar.DATE, 1)) {
            BigDecimal sales = BigDecimal.ZERO;
            long quantity = 0;

            LocalDate currentDate = LocalDate.parse(Utiliies.formatDateReverse(date.getTime()));
            Map<String, Calendar> map = new HashMap<>();
            if ((currentDate.getDayOfMonth()) == date.getActualMaximum(Calendar.DATE)) {
                map = getStartAndEndDate(date.getTime());
                Calendar sDateBill = map.get("startDate");
                Calendar eDateBill = map.get("endDate");

                List<VBill> bills = billRepo.findBillBy7Days(5, sDateBill.getTime(), eDateBill.getTime());
                StatisticBill statisticBill = new StatisticBill();
                for (VBill bill : bills) {
                    sales = sales.add(bill.getPrice());
                    quantity += detailBillRepo.findBySumQuantity(1, bill.getId()).orElse(0L);
                }
                statisticBill.setSales(sales);
                statisticBill.setAmount(quantity);
                sDateBill.roll(Calendar.DATE, 1);
                statisticBill.setDate(sDateBill.getTime());
                statisticOfDay.add(statisticBill);
                sales = BigDecimal.ZERO;
                quantity = 0;

                Date getNewDate = Date.from(currentDate.plusMonths(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
                date.setTime(getNewDate);
                date.roll(Calendar.DATE, -(currentDate.getDayOfMonth() - 1));
            }

            map = getStartAndEndDate(date.getTime());
            Calendar sDateBill = map.get("startDate");
            Calendar eDateBill = map.get("endDate");

            List<VBill> bills = billRepo.findBillBy7Days(5, sDateBill.getTime(), eDateBill.getTime());
            StatisticBill statisticBill = new StatisticBill();
            for (VBill bill : bills) {
                sales = sales.add(bill.getPrice());
                quantity += detailBillRepo.findBySumQuantity(1, bill.getId()).orElse(0L);
            }
            statisticBill.setSales(sales);
            statisticBill.setAmount(quantity);
            sDateBill.roll(Calendar.DATE, 1);
            statisticBill.setDate(sDateBill.getTime());
            statisticOfDay.add(statisticBill);
        }

        sDate.roll(Calendar.DATE, 1);
        response.setStartDate(sDate.getTime());
        response.setEndDate(eDate.getTime());
        response.setStatisticOfDay(statisticOfDay);
        response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.success"));
        response.getStatus().setStatus(Status.Success);

        log.info("AdminStatisticResponse :: End");
        return response;
    }

    @Override
    public TypeProductResponse displayTop5TypeProduct(BaseRequest request) {
        log.info("displayTop5TypeProduct :: Start");
        TypeProductResponse response = new TypeProductResponse();
        Pageable pageable = PageRequest.of(0,5);
        List<VTypeProduct> list = detailBillRepo.findByTop5(pageable);
        response.setTypeProducts(list);
        response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("displayTop5TypeProduct :: End");
        return response;
    }

    //done
    @Override
    public StatisticAdminResponse reportSumProduct(BaseRequest request) {
        StatisticAdminResponse response = null;
        log.info("AdminStatisticResponse :: Start");
        long block_product, sold_out = 0;

        block_product = this.productRepo.sumReportBlockProduct().orElse(0L);
        sold_out = this.productRepo.sumReportSoldOutProduct().orElse(0L);

        response = new StatisticAdminResponse(block_product, sold_out);
        response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.success"));
        response.getStatus().setStatus(Status.Success);

        log.info("AdminStatisticResponse :: End");
        return response;
    }

    @Override
    public StatisticAdminResponse reportSumOrder(BaseRequest request) {
        StatisticAdminResponse response = null;
        log.info("AdminStatisticResponse :: Start");
        int sumOrderUnConfimred, sumOrderPacking, sumOrderShipping, sumOrderCancel = 0;

        sumOrderUnConfimred = this.orderRepo.sumOrderUnConfimred();
        sumOrderPacking = this.orderRepo.sumOrderPacking();
        sumOrderShipping = this.orderRepo.sumOrderShipping();
        sumOrderCancel = this.orderRepo.sumOrderCancel();

        response = new StatisticAdminResponse(sumOrderUnConfimred, sumOrderPacking, sumOrderShipping, sumOrderCancel);
        response.getStatus().setMessage(MessageUtils.get(request.getLanguage(), "msg.success"));
        response.getStatus().setStatus(Status.Success);
        log.info("AdminStatisticResponse :: End");
        return response;
    }

    @Override
    public ReportTopProductResponse getTop6Product(String language) {
        log.info("DashBoardService :: Start");
        ReportTopProductResponse response=new ReportTopProductResponse();
        List<VProduct> products=this.productRepo.findTop6ByOrderByCreatedDateDesc();
        List<GetProductItem> items=this.convertProduct(products,language);
        response.setItems(items);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        log.info("DashBoardService :: END");
        return response;
    }

    public BigDecimal getTotalPriceOfDay(Calendar start, Calendar end) {
        List<VBill> bills = billRepo.findBillBy7Days(5, start.getTime(), end.getTime());
        if (bills.isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            BigDecimal total = BigDecimal.ZERO;
            for (VBill bill : bills) {
                total = total.add(bill.getPrice());
            }
            return total;
        }
    }

    private List<GetProductItem> convertProduct(List<VProduct> products,String language){
        List<GetProductItem> items=new ArrayList<GetProductItem>();
        for(VProduct product:products) {
            GetProductItem item=new GetProductItem();
            item.setImg(vFileUploadRepo.getURLById(product.getId()));
            item.setProductName(product.getProductName());
            item.setProductCode(product.getId());
            item.setStatusCode(product.getStatus());
            item.setStatusName(ProductUtils.getstatusname(product.getStatus(), language));
            items.add(item);
        }
        return items;
    }

    public Date stringToDate(String string) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(string);
            System.out.println(date+"-->");
            return date;
        } catch (Exception e) {
            return new Date();
        }
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

    public String dateConvertString(Date date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            return sdf.format(date);
        } catch (Exception e) {
            return "";
        }
    }
}
