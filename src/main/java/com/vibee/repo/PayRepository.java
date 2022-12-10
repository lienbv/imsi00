package com.vibee.repo;

import com.vibee.entity.Pay;
import com.vibee.model.ObjectResponse.DebitObjectPayResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Integer>, JpaSpecificationExecutor<Pay> {
    List<Pay> findByDebitId(int debitId);

    List<Pay> existsByDebitId(int debitId);

    @Query(value = "select sum(v.IN_PRICE) as inPrice, v.PRICE as price from v_pay v where v.DEBIT_ID=?1", nativeQuery = true)
    DebitObjectPayResponse getPay(int debitId);
}