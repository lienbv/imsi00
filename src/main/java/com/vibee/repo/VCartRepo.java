package com.vibee.repo;

import com.vibee.entity.VCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VCartRepo extends JpaSpecificationExecutor<VCart>,JpaRepository<VCart, Integer>{

}
