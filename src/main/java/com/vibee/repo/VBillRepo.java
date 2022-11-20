package com.vibee.repo;

import com.vibee.entity.VBill;
import com.vibee.model.response.bill.GetBillResponse;
import com.vibee.model.response.order.IDetailOrderResponse;
import com.vibee.model.response.order.IOrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
public interface VBillRepo extends JpaSpecificationExecutor<VBill>, JpaRepository<VBill, Integer> {

}
