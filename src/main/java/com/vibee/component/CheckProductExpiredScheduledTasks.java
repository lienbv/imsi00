package com.vibee.component;

import com.vibee.entity.VImport;
import com.vibee.entity.VUnit;
import com.vibee.repo.VImportRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Log4j2
public class CheckProductExpiredScheduledTasks {

    @Autowired
    private VImportRepo vImportRepo;

    @Scheduled(cron = "0 0/1 16 * * ?")
    public void checkImport() {
       log.info("checking import.............");
        Map<String, Calendar> map = this.getStartAndEndDate(new Date());
        List<VImport> list = vImportRepo.getImportsByDateCheckExpired(map.get("startDate").getTime(), map.get("endDate").getTime());
        for (VImport item : list) {
            item.setStatus(0);
            vImportRepo.save(item);
        }
        log.info("checked import!");
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
