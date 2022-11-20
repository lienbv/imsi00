package com.vibee.repo;

import com.vibee.entity.VDetailCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VDetailCartRepository extends JpaRepository<VDetailCart, Integer>, JpaSpecificationExecutor<VDetailCart> {
}