package com.vibee.repo;

import com.vibee.entity.DebitDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DebitDetailRepository extends JpaRepository<DebitDetail, Integer>, JpaSpecificationExecutor<DebitDetail> {
    List<DebitDetail> findByDebitId(int id);
}
