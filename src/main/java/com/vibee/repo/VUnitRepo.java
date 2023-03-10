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

    @Query("SELECT u FROM unit u WHERE (u.parentId= :unitId or u.id= :unitId) AND u.status=1")
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

    VUnit findByIdAndStatus(int id, int status);

    @Query(value = "select * from v_unit where id =?1 or PARENT_ID=?2 or id= (select PARENT_ID from v_unit where v_unit.id=?3)", nativeQuery = true)
    List<VUnit> getAllById(int id, int parentId, int id_1);

    List<VUnit> findByParentIdAndStatus(int parent, int status);

    List<VUnit> findByParentId(int parent);
    List<VUnit> findByParentIdOrIdAndStatus(int parent, int id, int status);

    @Query(value = "select u.id from v_unit u where u.PARENT_ID = ?1 or u.id= ?2 and u.status=1 order by u.amount desc limit 1", nativeQuery = true)
    int getMaxIdByParenId(int parentId,int unitId);

//

    //
    @Query("SELECT u FROM unit u WHERE u.parentId=0 and u.status = 1")
    Page<VUnit> getAllUnitParents(Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1 and a.status = 1) or (u.parentId = 0 and u.unitName like ?1 and u.status = 1) and u.status = 1")
    Page<VUnit> findByUnitName(String name, Pageable pageable);

    @Query("select u from unit u where u.id in (select a.parentId from unit a where a.unitName like ?1 and a.status = 1) or (u.parentId = 0 and u.unitName like ?1 and u.status = 1) and u.status = 1")
    List<VUnit> findByUnitName(String name);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId and u.status = 1 order by u.amount asc ")
    List<VUnit> getUnitsByParentId(@Param("unitId") int unitId);

    @Query(value="SELECT * FROM v_unit u JOIN v_import i ON i.ID_UNIT=u.id JOIN v_warehouse w ON w.id=i.WAREHOUSE_ID JOIN v_product p ON p.id = w.PRODUCT_ID WHERE u.status=1 AND p.BAR_CODE = ?1 ORDER BY i.CREATED_DATE DESC LIMIT 1", nativeQuery = true)
    VUnit getUnitByBarCode(String barcode);

    @Query(value = "SELECT * FROM vibee.v_unit v where v.PARENT_ID= ?1 or v.ID = ?2 order by v.AMOUNT desc limit 1 ", nativeQuery = true)
    VUnit getByIdChild(int parent, int id);

    @Query("SELECT u.unitName FROM unit u where u.id = :id")
    String getUnitNameByUnitId (@Param("id") int unitId);

    @Query("SELECT u FROM unit u WHERE u.id not in :unitsId AND u.parentId= :parentId AND u.status=1")
    List<VUnit> getUnitsNotInId(@Param("unitsId") List<Integer> list, @Param("parentId") int parentId);

    @Query("SELECT u FROM unit u WHERE u.id= :unitId AND u.status=1")
    VUnit getUnitById(@Param("unitId") int unitId);
    @Query(value = "select * from vibee.v_unit where PARENT_ID=?1 or id =?2 or id =?1 order by amount asc", nativeQuery = true)
    List<VUnit> getVUnit(int parent, int id_1, int id_2);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId or u.id= :unitId AND u.status=1 order by u.amount asc")
    List<VUnit> getAllUnitASCByParentId(@Param("unitId") int unitId);

    @Query("SELECT u FROM unit u where u.parentId = :id order by u.amount desc")
    List<VUnit> getUnitByUnitId (@Param("id") int unitId);

    @Query("SELECT u FROM unit u WHERE u.parentId= :unitId AND u.status=1 order by u.amount asc")
    List<VUnit> getChildUnitASCByParentId(@Param("unitId") int unitId);
}
