package com.vibee.repo;

import com.vibee.entity.VUploadFile;
import com.vibee.entity.VWarehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VFileUploadRepo extends JpaRepository<VUploadFile, Integer> {
    @Query("SELECT f.url FROM uploadFile f WHERE f.id= :fileId")
    String getURLById(@Param("fileId") int id);
    VUploadFile findById(int id);
}
