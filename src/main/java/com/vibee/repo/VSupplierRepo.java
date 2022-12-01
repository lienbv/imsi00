package com.vibee.repo;

import com.vibee.entity.VSupplier;
import org.springframework.data.domain.Pageable;
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
    @Transactional
    @Modifying
    @Query(value = "  UPDATE supplier SET supplier.STATUS=2 WHERE supplier.ID= ?1", nativeQuery = true)
    void updateStatusSupplier(int id);
//	@Query("select o from Supplier o where o.status = ?1 and o.nameSup like ?2")
//	List<Supplier> findBySuppliers(int status,String nameSup);

    @Query("select o from VSupplier o where o.nameSup like ?1")
    List<VSupplier> findBySuppliers(String nameSup);

    @Query("select o from VSupplier o where o.nameSup like ?1 and o.status = ?2")
    List<VSupplier> findBySuppliers(String nameSup, int status);

    @Query("select o from VSupplier o where o.nameSup like ?1")
    List<VSupplier> findBySuppliers(String nameSup, Pageable pageable);

    @Query("SELECT s.nameSup FROM VSupplier s WHERE s.id= :supplierId")
    String findbyid(@Param("supplierId") int id);
}
