package com.vibee.repo;

import com.vibee.entity.VRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VRoleRepo extends JpaRepository<VRole, Integer>,JpaSpecificationExecutor<VRole>{

}
