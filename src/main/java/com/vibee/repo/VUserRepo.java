package com.vibee.repo;

import com.vibee.entity.VUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VUserRepo extends JpaRepository<VUser, Integer> {


    VUser findByUsername(String user);
    VUser findByEmail(String email);
    VUser findByCccd(String cccd);
    @Query("SELECT u.id FROM user u WHERE u.username= :username")
    int findUserIdByUsername(@Param("username") String username);
    VUser findByUsernameAndEmail(String username, String email);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.numberPhone like :numberPhone")
     List<VUser> getCustomerByNumberPhones(@Param("status") int status, @Param("numberPhone") String numberPhone);
    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.numberPhone like :numberPhone")
     Page<VUser> getCustomerByNumberPhone(@Param("status") int status, @Param("numberPhone") String numberPhone,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.cccd like :cccd")
     Page<VUser> getCustomerByCCCD(@Param("status") int status,@Param("cccd") String cccd,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.cccd like :cccd")
     List<VUser> getCustomerByCCCD(@Param("status") int status,@Param("cccd") String cccd);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.email like :email")
     Page<VUser> getCustomerByEmail(@Param("status") int status,@Param("email") String email,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.email like :email")
     List<VUser> getCustomerByEmail(@Param("status") int status,@Param("email") String email);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.fullname like :fullname")
     Page<VUser> getCustomerByName(@Param("status") int status,@Param("fullname") String fullname,Pageable pageable);

    @Query("select u from user u join userRole ur on u.id = ur.userId where u.status = :status and ur.roleId = 2 and u.fullname like :fullname")
     List<VUser> getCustomerByNames(@Param("status") int status,@Param("fullname") String fullname);

    Page<VUser> findByFullname(String number, Pageable pageable);

    VUser findByNumberPhone(String phone);
    VUser findById(int id);

}
