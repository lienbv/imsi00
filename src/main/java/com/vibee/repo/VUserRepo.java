package com.vibee.repo;

import com.vibee.entity.VBill;
import com.vibee.entity.VUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
@Repository
public interface VUserRepo extends JpaRepository<VUser, Integer> {

    @Transactional
    @Modifying
    @Query(value = "  UPDATE user SET user.STATUS=2 WHERE user.ID= ?1", nativeQuery = true)
    void updateStatusAccount(int id);

    VUser findByUsername(String user);
//
    @Query("SELECT u.id FROM user u WHERE u.username= :username")
    int findUserIdByUsername(@Param("username") String username);
    VUser findByUsernameAndEmail(String username, String email);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.numberPhone like :numberPhone")
    public List<VUser> getCustomerByNumberPhones(@Param("status") int status, @Param("numberPhone") String numberPhone);
    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.numberPhone like :numberPhone")
    public Page<VUser> getCustomerByNumberPhone(@Param("status") int status, @Param("numberPhone") String numberPhone,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.cccd like :cccd")
    public Page<VUser> getCustomerByCCCD(@Param("status") int status,@Param("cccd") String cccd,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.cccd like :cccd")
    public List<VUser> getCustomerByCCCD(@Param("status") int status,@Param("cccd") String cccd);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.email like :email")
    public Page<VUser> getCustomerByEmail(@Param("status") int status,@Param("email") String email,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.email like :email")
    public List<VUser> getCustomerByEmail(@Param("status") int status,@Param("email") String email);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.fullname like :fullname")
    public Page<VUser> getCustomerByName(@Param("status") int status,@Param("fullname") String fullname,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.fullname like :fullname")
    public List<VUser> getCustomerByNames(@Param("status") int status,@Param("fullname") String fullname);

}
