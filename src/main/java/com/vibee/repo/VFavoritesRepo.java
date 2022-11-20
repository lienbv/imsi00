package com.vibee.repo;

import com.vibee.entity.VFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VFavoritesRepo extends JpaSpecificationExecutor<VFavorites>,JpaRepository<VFavorites, Integer>{

}
