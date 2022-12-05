package com.vibee.service.vstatisticadmin.Impl;

import com.vibee.model.Status;
import com.vibee.model.response.adminstatistic.AdminStatisticResponse;
import com.vibee.repo.VBillRepo;
import com.vibee.service.vstatisticadmin.StatisticAdminService;
import lombok.extern.log4j.Log4j2;
import org.apache.poi.hpsf.Decimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
@Log4j2
@Service
public class StatisticAdminServiceImpl implements StatisticAdminService {
    @Autowired
    private VBillRepo billRepo;

    @Override
    public AdminStatisticResponse totalPriceOfDay() {
        AdminStatisticResponse response = new AdminStatisticResponse();
        Calendar sDate = Calendar.getInstance();
        sDate.setTime(new Date());
        sDate.set(Calendar.HOUR_OF_DAY, 0);
        sDate.set(Calendar.MINUTE,0);
        sDate.set(Calendar.SECOND,0);
        sDate.set(Calendar.MILLISECOND,0);
        Calendar eDate = Calendar.getInstance();
        eDate.setTime(new Date());
        eDate.set(Calendar.HOUR_OF_DAY, 23);
        eDate.set(Calendar.MINUTE, 59);
        eDate.set(Calendar.SECOND, 59);
        eDate.set(Calendar.MILLISECOND, 999);

        BigDecimal totalPriceOfBills = new BigDecimal(billRepo.findByTotalPriceOfBills(sDate.getTime(), eDate.getTime()).orElse(0L));
        response.setTotalPriceOfDay(totalPriceOfBills);
        response.getStatus().setStatus(Status.Success);
        response.getStatus().setMessage("");
        return response;
    }


}
