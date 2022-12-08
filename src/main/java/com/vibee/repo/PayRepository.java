package com.vibee.repo;

import com.vibee.entity.Pay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PayRepository extends JpaRepository<Pay, Integer>, JpaSpecificationExecutor<Pay> {
    List<Pay> findByDebitId(int debitId);

    List<Pay> existsByDebitId(int debitId);
}