package com.vibee.repo;

import com.vibee.entity.VDebitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DebitDetailRepository extends JpaRepository<VDebitDetail, Integer>, JpaSpecificationExecutor<VDebitDetail> {
    List<VDebitDetail> findByDebitId(int id);

}
