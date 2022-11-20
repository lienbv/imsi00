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

    VUser findByUsername(String user);
//
    @Query("SELECT u.id FROM VUser u WHERE u.username= :username")
    int findUserIdByUsername(@Param("username") String username);
    VUser findByUsernameAndEmail(String username, String email);


}
