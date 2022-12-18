package com.vibee.repo;

import com.vibee.entity.VDebit;
import com.vibee.model.ObjectResponse.GetDebitOfUserObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebitRepository extends JpaRepository<VDebit, Integer>, JpaSpecificationExecutor<VDebit> {
    VDebit findByBillId(int idBill);

//    @Query(value = "select d.FULL_NAME as fullName, d.TOTAL_AMOUNT_OWED as total, d.TYPE_OF_DEBTOR as typeDebt, d.STATUS as status,\n" +
//            "u.NUMBER_PHONE as phone, d.ID as idDebit, u.ID as idUser, d.DEBIT_DATE as debitDate\n" +
//            "from v_debit d join v_user u on d.USER_ID = u.ID  ",nativeQuery = true)
//    Page<GetDebitOfUserObject> getDebitOfUser(Pageable pageable);
//
//    @Query(value = "select v_u.ID as idUser,v_u.NUMBER_PHONE as phone\n" +
//                   "from v_user v_u join v_debit v_d on v_u.ID = v_d.USER_ID group by (v_u.ID)", nativeQuery = true)
//    Page<GetDebitOfUserObject> getDebtByPhone(Pageable pageable);

//    @Query(value = "select * from v_debit d join v_user u on d.USER_ID = u.ID  \n" +
//            "where d.FULL_NAME like ?1 or u.NUMBER_PHONE like ?2", nativeQuery = true)
//    Page<GetDebitOfUserObject> searchByFullnameOrPhone( String fullname, String phone, Pageable pageable);

    Page<VDebit> findByFullNameLikeOrPhoneNumberLike(String searchName, String phone, Pageable pageable);
    VDebit findById(int id);
    Page<VDebit> findByUserId(int id, Pageable pageable); 
    List<VDebit> findByStatus(int status);



}