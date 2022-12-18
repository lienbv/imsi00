package com.vibee.component;

import com.vibee.entity.VDebit;
import com.vibee.repo.DebitRepository;
import com.vibee.repo.PayRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Log4j2
public class ScheduledTasks {
    private final PayRepository payRepository;
    private final DebitRepository debitRepository;

    public ScheduledTasks(PayRepository payRepository, DebitRepository debitRepository) {
        this.payRepository = payRepository;
        this.debitRepository = debitRepository;
    }

    @Scheduled(fixedDelay = 120000)
    public void updateStatusDebtAfterFourteenDay(){
        log.info("updateStatusDebtAfterFourteenDay :: Start");
        List<VDebit> findAll = this.debitRepository.findByStatus(2);
        Date date = new Date();

        for (VDebit debit: findAll){
            long millisecond= TimeUnit.MILLISECONDS.toDays( date.getTime()-debit.getDebitDate().getTime());
            if(millisecond >= 14){
                debit.setStatus(3);
                this.debitRepository.save(debit);
            }
        }
        log.info("updateStatusDebtAfterFourteenDay :: End");
    }

}
