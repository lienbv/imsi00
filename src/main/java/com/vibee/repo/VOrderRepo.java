package com.vibee.repo;

import com.vibee.entity.VOrder;
import com.vibee.model.response.order.IDetailOrderResponse;
import com.vibee.model.response.order.IOrderListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface VOrderRepo extends JpaSpecificationExecutor<VOrder>,JpaRepository<VOrder, Integer>{
    @Query("SELECT COUNT(o) FROM VOrder o WHERE o.status = 1")
    public int sumOrderUnConfimred();

    @Query("SELECT COUNT(o) FROM VOrder o WHERE o.status = 2")
    public int sumOrderPacking();

    @Query("SELECT COUNT(o) FROM VOrder o WHERE o.status = 3")
    public int sumOrderShipping();

    @Query("SELECT COUNT(o) FROM VOrder o WHERE o.status = 7")
    public int sumOrderCancel();
}
