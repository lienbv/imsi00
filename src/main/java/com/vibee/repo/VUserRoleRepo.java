
package com.vibee.repo;

import com.vibee.entity.VUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VUserRoleRepo extends JpaSpecificationExecutor<VUserRole>,JpaRepository<VUserRole, Integer>{
    @Transactional
    @Modifying
    @Query(value = "UPDATE user_role SET ID_ROLE = ?1 WHERE user_role.ID = ?2", nativeQuery = true)
    void updateRoleAccount(int idRole, int idUserRole);

    @Transactional
    @Modifying
    @Query(value = "SELECT user.FULLNAME, user.NUMBER_PHONE, user.EMAIL, user.STATUS, role.NAME FROM user_role LEFT JOIN role on user_role.ID_ROLE= role.ID LEFT JOIN user on user_role.ID_USER=user.ID order by user.STATUS ASC", nativeQuery = true)
    List<Object[]> getAllAccount();
}
