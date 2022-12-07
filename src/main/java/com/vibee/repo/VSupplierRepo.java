package com.vibee.repo;

import com.vibee.entity.VSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VSupplierRepo extends JpaSpecificationExecutor<VSupplier>,JpaRepository<VSupplier, Integer>{
    List<VSupplier> findByStatus(int status);

    @Query("select s.id from VSupplier s ")
    List<String> findById();
    VSupplier findByIdAndStatus(int id, int status);
}
