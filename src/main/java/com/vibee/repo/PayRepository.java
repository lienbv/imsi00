package com.vibee.repo;

import com.vibee.entity.VPay;
import com.vibee.model.ObjectResponse.DebitObjectPayResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayRepository extends JpaRepository<VPay, Integer>, JpaSpecificationExecutor<VPay> {
    List<VPay> findByBillId(int debitId);

    List<VPay> existsByBillId(int debitId);

    @Query(value = "select sum(v.IN_PRICE) as inPrice, v.PRICE as price from v_pay v where v.BILL_ID=?1", nativeQuery = true)
    DebitObjectPayResponse getPay(int debitId);
    @Query(value = "select sum(v.NUMBER_OF_PAYOUTS) as num from v_pay v where v.BILL_ID=?1", nativeQuery = true)
    int getNum(int debit);
    @Query(value = "select * from v_pay v where v.BILL_ID=?1 order by v.ACTUAL_DATE_OF_PAYMENT_OF_DEBT desc limit 1", nativeQuery = true)
    VPay getNewDate(int debitId);

}