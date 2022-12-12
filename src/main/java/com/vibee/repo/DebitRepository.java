package com.vibee.repo;

import com.vibee.entity.VDebit;
import com.vibee.model.ObjectResponse.GetDebitOfUserObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DebitRepository extends JpaRepository<VDebit, Integer>, JpaSpecificationExecutor<VDebit> {
    VDebit findByBillId(int idBill);

    @Query(value = "select count( v_u.ID ) as amountUserDebit, v_u.ID as idUser, v_d.ID as idDebit, v_d.ADDRESS as address,\n" +
            " v_d.FULL_NAME as fullName, v_u.NUMBER_PHONE as phone, v_d.STATUS , sum(v_d.TOTAL_AMOUNT_OWED) as total\n" +
            "from v_user v_u join v_debit v_d on v_u.ID = v_d.userId group by v_u.ID",nativeQuery = true)
    Page<GetDebitOfUserObject> getDebitOfUser(Pageable pageable);

//    Page<VDebit> findByFullNameOrPhoneNumberContainingIgnoreCase(String searchName, String searchPhone, Pageable pageable);
    VDebit findById(int id);
    Page<VDebit> findByUserId(int id, Pageable pageable);

}