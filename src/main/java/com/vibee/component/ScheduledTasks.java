package com.vibee.component;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.vibee.entity.VDebit;
import com.vibee.repo.DebitRepository;
import com.vibee.repo.PayRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
@Log4j2
public class ScheduledTasks {
    private final PayRepository payRepository;
    private final DebitRepository debitRepository;

    public ScheduledTasks(PayRepository payRepository, DebitRepository debitRepository) {
        this.payRepository = payRepository;
        this.debitRepository = debitRepository;
    }
    @Value("${twilio.account_sid}")
    private String accountSID;

    @Value("${twilio.auth_token}")
    private String accountAuthToken;

    @Value("${twilio.trial_number}")
    private String twilloSenderNumber;


    @Scheduled(fixedDelay = 120000)
    public void updateStatusDebtAfterFourteenDay(){
        log.info("updateStatusDebtAfterFourteenDay :: Start");
        List<VDebit> findAll = this.debitRepository.findByStatus(2);
        Date date = new Date();

        for (VDebit debit: findAll){
            long millisecond= TimeUnit.MILLISECONDS.toDays( date.getTime()-debit.getDebitDate().getTime());
            if(debit.getStatus()==2 && millisecond >= 14){
                debit.setStatus(3);
              VDebit vDebit=  this.debitRepository.save(debit);
                Twilio.init(accountSID, accountAuthToken);

                PhoneNumber recieverPhoneNumber = new PhoneNumber("+84329891028");
                PhoneNumber senderTwilloPhoneNumber = new PhoneNumber(twilloSenderNumber);

                MessageCreator creator = com.twilio.rest.api.v2010.account.Message.creator(recieverPhoneNumber, senderTwilloPhoneNumber, "\uD83D\uDCDEXin chào bạn " + vDebit.getFullName()+
                        " bạn đã nợ cử hàng chúng tôi "+ vDebit.getTotalAmountOwed()+" vào ngày "+ vDebit.getDebitDate());
                Message create = creator.create();
                Message.Status status = create.getStatus();
                log.info("liên nè "+status);
            }

        }
        log.info("updateStatusDebtAfterFourteenDay :: End");
    }
//    public String sendMessage(MessageModel messageRequest) {
//        try {
//            Twilio.init(accountSID, accountAuthToken);
//
//            String textOTP = messageRequest.setToken(String.valueOf(value));
//
//            String mobileNumber = messageRequest.getMobileNumber();
//
//            UserBO userBO = userService.findByPhoneNumber(mobileNumber);
//            if (userBO == null) {
//                return "Phone number don't exit";
//            }
//
//
//
//
//
//
//            Status status = create.getStatus();
//            userBO.setOTP(textOTP);
//            userService.saveOTP(messageRequest,value);
//
//            logger.info("Message Send Succesfully to the number " + mobileNumber);
//
//            return "Message Send Succesfully";
//        } catch (Exception e) {
//            logger.error("Exception in sendMessage Method " + e);
//            return "Message Send Fail";
//        }
//
//    }
//    public boolean verifyOTP(String OTP) {
//        UserBO user = userDAO.findByOTP(OTP);
//
//        if (user == null || !OTP.equals(user.getOTP())) {
//            return false;
//        } else {
////            user.setOTP(null);
//            userDAO.save(user);
//
//            return true;
//        }

//    }



}
