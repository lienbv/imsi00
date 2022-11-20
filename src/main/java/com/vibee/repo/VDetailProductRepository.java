package com.vibee.repo;

import com.vibee.entity.VWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VDetailProductRepository extends JpaRepository<VWarehouse, Integer>, JpaSpecificationExecutor<VWarehouse> {







}