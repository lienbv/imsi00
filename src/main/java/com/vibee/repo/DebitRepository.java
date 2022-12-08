package com.vibee.repo;

import com.vibee.entity.Debit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DebitRepository extends JpaRepository<Debit, Integer>, JpaSpecificationExecutor<Debit> {
    Debit findByBillId(int idBill);

    Page<Debit> findByFullNameOrPhoneNumberContainingIgnoreCase(String searchName, String searchPhone, Pageable pageable);
    Debit findById(int id);

    @Query("select d.id, p.id, d.totalAmountOwed, p.amountPay, p.status, d.status from Debit d join  Pay p on d.id= p.debitId")
    List<Object> findByPayAnDebit();
}