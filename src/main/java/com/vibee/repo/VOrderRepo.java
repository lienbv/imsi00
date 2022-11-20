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

}
