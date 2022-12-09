package com.vibee.repo;

import com.vibee.entity.VUploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VUploadFileRepo extends JpaRepository<VUploadFile, Integer> {
}
