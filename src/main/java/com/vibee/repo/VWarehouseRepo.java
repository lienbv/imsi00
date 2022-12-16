package com.vibee.repo;

import com.vibee.entity.VWarehouse;
import com.vibee.model.ObjectResponse.GetReportEstObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VWarehouseRepo extends JpaRepository<VWarehouse, Integer> {
    @Query(value = "SELECT in_amount as inAmount, out_price as outPrice, in_Price as inPrice, out_amount as outAmount, unit_id as unitId from v_warehouse WHERE v_warehouse.product_id= ?1",nativeQuery = true)
    GetReportEstObject getReportEst(int productId);
    @Query("SELECT w from warehouse w WHERE w.id=(SELECT i.warehouseId FROM import i WHERE i.id= :importId)")
    VWarehouse getWarehouseByImportId(@Param("importId") int importId);
    List<VWarehouse> findById(int id);
    VWarehouse findByProductId(int id);

    @Query("SELECT w from warehouse w")
    List<VWarehouse> getAllWarehouse(Pageable pageable);
    @Query(value = "SELECT * FROM vibee.v_warehouse w where w.PRODUCT_ID= ?1 order by w.MODIFIED_Date desc limit 1 ", nativeQuery = true)
    VWarehouse getNumberOfEntries(int idProduct);
    @Query(value = "SELECT * FROM vibee.v_warehouse w where w.PRODUCT_ID = ?1 and  w.CREATED_DATE = ?2",nativeQuery = true)
    VWarehouse getProductByCreateDate(int idProduct, Date date);
    @Query(value = "SELECT * FROM vibee.v_warehouse w where w.PRODUCT_ID = ?1 and  w.MODIFIED_Date = ?2", nativeQuery = true)
    VWarehouse getProductByModifyDate(int idProduct, Date date);
}
