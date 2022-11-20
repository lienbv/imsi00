package com.vibee.repo;

import com.vibee.entity.VDetailCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VDetailCartRepo extends JpaSpecificationExecutor<VDetailCart>,JpaRepository<VDetailCart, Integer>{
}
