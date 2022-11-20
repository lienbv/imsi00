
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

}
