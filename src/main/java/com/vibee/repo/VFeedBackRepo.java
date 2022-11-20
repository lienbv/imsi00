package com.vibee.repo;

import com.vibee.entity.VFeedBack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VFeedBackRepo extends JpaSpecificationExecutor<VFeedBack>,JpaRepository<VFeedBack, Integer>{

}
