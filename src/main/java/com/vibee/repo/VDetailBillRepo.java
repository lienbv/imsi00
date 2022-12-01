package com.vibee.repo;

import com.vibee.entity.VDetailBill;
import com.vibee.entity.VTypeProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VDetailBillRepo extends JpaSpecificationExecutor<VDetailBill>, JpaRepository<VDetailBill, Integer> {
    @Query("select sum(db.amount) from VDetailBill db where db.status = ?1 and db.billId = ?2")
    Optional<Long> findBySumQuantity(int status, int billId);

}
