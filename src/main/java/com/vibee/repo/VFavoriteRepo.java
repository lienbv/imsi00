package com.vibee.repo;

import com.vibee.entity.VFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VFavoriteRepo extends JpaSpecificationExecutor<VFavorite>,JpaRepository<VFavorite, Integer>{

}
