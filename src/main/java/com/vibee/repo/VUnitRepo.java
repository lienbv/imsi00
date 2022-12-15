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

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId or u.id= :unitId AND u.status=1")
    List<VUnit> getAllUnitByParentId(@Param("unitId") int unitId);

    @Query("SELECT u FROM unit u WHERE u.parentId=0 and u.status=1")
    List<VUnit> getAllUnitParents();

    @Query("SELECT u FROM unit u WHERE u.status=1")
    List<VUnit> getAllUnits();



    @Query(value="SELECT unit.unit_name FROM unit where unit.id =(select import.id_unit from import where import.id_product=?1 order by warehouse.created_date asc limit 1)",nativeQuery = true)
    String getUnitNameByProductId(int productId);

    @Query(value="SELECT unit.id FROM unit where unit.id =(select import.id_unit from import where import.id_product=?1 order by warehouse.created_date asc limit 1)",nativeQuery = true)
    int getUnitIdById(int productId);
    List<VUnit> findByParentIdOrStatus(int parentId, int status);
    List<VUnit> findByStatus(int status);
    VUnit findById(int id);
    @Query(value = "select * from v_unit where id =?1 or PARENT_ID=?2 or id= (select PARENT_ID from v_unit where v_unit.id=?3)", nativeQuery = true)
    List<VUnit> getAllById(int id, int parentId, int id_1);

    List<VUnit> findByParentIdAndStatus(int parent, int status);

    List<VUnit> findByParentIdOrIdAndStatus(int parent, int id, int status);

    @Query(value = "select max(v.id) from v_unit v where v.PARENT_ID =?1 and v.status=?2", nativeQuery = true)
    int  getMaxIdByParenId(int parent, int status);

    @Query("SELECT u FROM unit u WHERE u.parentId=0 and u.status = 1")
    Page<VUnit> getAllUnitParents(Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1 and a.status = 1) or (u.parentId = 0 and u.unitName like ?1 and u.status = 1) and u.status = 1")
    Page<VUnit> findByUnitName(String name, Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1 and a.status = 1) or (u.parentId = 0 and u.unitName like ?1 and u.status = 1) and u.status = 1")
    List<VUnit> findByUnitName(String name);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId and u.status = 1 order by u.amount asc ")
    List<VUnit> getUnitsByParentId(@Param("unitId") int unitId);

    @Query(value="SELECT u FROM unit u JOIN import i ON i.unitId=u.id JOIN warehhouse w ON w.id=i.warehouseId JOIN product p ON p.id = w.productId WHERE u.status=1 AND p.barcode = ?1 ORDER BY i.createdDate DESC LIMIT 1", nativeQuery = true)
    VUnit getUnitByBarCode(String barcode);

    @Query(value = "SELECT * FROM vibee.v_unit v where v.PARENT_ID= ?1 or v.ID = ?2 order by v.AMOUNT desc limit 1 ", nativeQuery = true)
    VUnit getByIdChild(int parent, int id);
}
