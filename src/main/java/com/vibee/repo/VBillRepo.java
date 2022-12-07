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
import java.util.Optional;

@Repository
public interface VBillRepo extends JpaSpecificationExecutor<VBill>, JpaRepository<VBill, Integer> {
    @Query("select o from VBill o where o.createdDate between ?1 and ?2")
    Page<VBill> findAllBillByDateAndPage(Date startDate, Date endDate, Pageable pageable);

    @Query("select o from VBill o where o.createdDate between ?1 and ?2")
    List<VBill> findAllBillByDateAndPage(Date startDate, Date endDate);

    @Query("select o from VBill o where o.createdDate between ?1 and ?2")
    List<VBill> findAllBillByDate(Date startDate, Date endDate);

    @Query("select sum(b.price) from VBill b where b.createdDate between ?1 and ?2 and b.status = 5")
    Optional<Long> findByTotalPriceOfBills(Date startDay, Date endDay);

    @Query("select sum(ip.inMoney) * sum(vdb.amount) from VBill vb join VDetailBill vdb on vb.id = vdb.id join import ip on ip.id = vdb.importId where vb.createdDate between ?1 and ?2 and vb.status = 5 group by ip.warehouseId")
    List<Long> getInterestRateOfDay(Date startDay, Date endDay);

    @Query("select o from VBill o where o.status = ?1 and o.createdDate between ?2 and ?3")
    List<VBill> findBillBy7Days(int status, Date startDate, Date endDate);
}
