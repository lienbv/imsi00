package com.vibee.repo;

import com.vibee.entity.VUnit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VUnitRepo extends JpaRepository<VUnit, Integer>{
    @Query(value="SELECT u FROM unit u WHERE u.detailProductId = (SELECT TOP1 dp.id FROM VDetailProduct dp WHERE dp.productId = ?1 ORDER BY dp.id DESC)", nativeQuery = true)
    List<VUnit> findByProductId(int productId);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId or u.id= :unitId")
    List<VUnit> getAllUnitByParentId(@Param("unitId") int unitId);

    @Query("SELECT u FROM unit u WHERE u.parentId=0")
    List<VUnit> getAllUnitParents();

    @Query(value="SELECT unit.unit_name FROM unit where unit.id =(select import.id_unit from import where import.id_product=?1 order by warehouse.created_date asc limit 1)",nativeQuery = true)
    String getUnitNameByProductId(int productId);

    @Query(value="SELECT unit.id FROM unit where unit.id =(select import.id_unit from import where import.id_product=?1 order by warehouse.created_date asc limit 1)",nativeQuery = true)
    int getUnitIdById(int productId);

//

    //
    @Query("SELECT u FROM unit u WHERE u.parentId=0")
    Page<VUnit> getAllUnitParents(Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1) or (u.parentId = 0 and u.unitName like ?1)")
    Page<VUnit> findByUnitName(String name, Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1) or (u.parentId = 0 and u.unitName like ?1)")
    List<VUnit> findByUnitName(String name);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId order by u.amount asc ")
    List<VUnit> getUnitsByParentId(@Param("unitId") int unitId);

}
