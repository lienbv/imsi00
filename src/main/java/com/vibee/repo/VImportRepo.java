package com.vibee.repo;

import com.vibee.entity.VImport;
import com.vibee.model.ObjectResponse.GetCharWarehouseObject;
import com.vibee.model.ObjectResponse.GetExportsObject;
import com.vibee.model.ObjectResponse.GetWarehousesObject;
import com.vibee.model.ObjectResponse.ViewStallObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Repository
@Transactional
public interface VImportRepo extends JpaSpecificationExecutor<VImport>,JpaRepository<VImport, Integer> {

    @Query(value = "SELECT sum(v_import.in_amount) as inAmount, sum(v_export.out_amount/v_unit.amount), v_import.IN_PRICE, sum(v_export.out_amount*v_export.out_price)" +
            "from v_import join v_export on v_export.import_id=v_import.id " +
            "join v_unit on v_unit.id=v_export.id_unit " +
            "join v_warehouse on v_import.warehouse_id=v_warehouse.id  where v_warehouse.product_id= ?1 group by v_import.id order by v_import.created_date desc limit 2",nativeQuery = true)
    List<Object> getReportEst(int productId);

    @Query("DELETE from import i WHERE i.warehouseId= (SELECT w.id FROM warehouse w WHERE w.productId= :productId)")
    int deleteByProductId(@Param("productId") int id);

    @Query(value = "SELECT Import.ID as id, Import.name_supplier as nameSupplier, (Import.inventory/v_unit.amount) as inventory, Import.ID_UNIT as unitId, " +
            "Import.img as img FROM v_v_import JOIN v_unit ON v_unit.id=v_v_import.id_unit WHERE v_v_import.status = 1 AND v_v_import.id_product = ?1",nativeQuery = true)
    List<ViewStallObject> viewStall(int productId);

    @Query(value="SELECT v_v_import.created_date FROM v_v_import WHERE v_v_import.id_product= ?1 ORDER BY v_v_import.created_date DESC LIMIT 1",nativeQuery = true)
    Date getCreatedDateByProductId( int productId);

    @Query("SELECT i.supplierName FROM import i WHERE i.warehouseId= (SELECT w.id FROM warehouse w WHERE w.productId= :productId)")
    List<String> getSupplierNamesByProductId(@Param("productId") int productId);

    @Query("SELECT sum(i.inAmount) as inAmount, sum(e.outAmount/u.amount) as outAmount, i.createdDate, i.inMoney as inPrice, sum(e.outAmount*e.outPrice) as inPrice\n" +
            "from import i join export e on e.importId=i.id join unit u on u.id=e.unitId join warehouse w on i.warehouseId=w.id where w.productId=?1 group by i.id order by i.createdDate")
    List<GetCharWarehouseObject> getCharWarehouseByProductId(int productId);

    @Query(value = "SELECT us.fullname,v_import.id as importId, v_import.created_date as createdDate,ui.unit_name as unitName, v_import.IN_PRICE as inPrice, sum(v_import.in_amount) as inAmount, sum(e.out_amount/un.amount) as outAmount, sum(e.out_amount*e.out_price) as outPrice, v_import.status, (sum(v_import.in_amount)-sum(e.out_amount/un.amount)) as inventory,w.number_of_entries as countWarehouse \n" +
            "FROM v_import JOIN v_warehouse as w on w.id=v_import.warehouse_id JOIN v_export as e ON e.import_id=v_import.id \n" +
            "JOIN v_unit as un ON un.id=e.id_unit \n" +
            "JOIN v_unit as ui ON ui.id=v_import.id_unit \n" +
            "JOIN v_user as us ON us.username=v_import.creator WHERE w.product_id= ?1 GROUP BY v_import.id,us.fullname ORDER BY ?2 ?3",nativeQuery = true)
    List<GetWarehousesObject> getWarehouseByProductId(int productId, String orderBy, String orderType);

    @Query("SELECT CASE WHEN COUNT(i)>0 THEN TRUE ELSE FALSE END FROM import i WHERE i.status=1 AND i.productCode= :productCode")
    Boolean isExistProductByProductCode(@Param("productCode") String productCode);

    @Query( value = "SELECT u.id as unit, u.unitName as unitName" +
            "FROM import i JOIN warehouse w ON w.id=i.warehouseId " +
            "JOIN product p ON p.id=w.productId JOIN unit u ON u.id=i.unitId WHERE p.barCode = :barCode AND e.status=1 ORDER BY i.id DESC LIMIT 1", nativeQuery = true)
    GetExportsObject getUnitImportByBarCode(@Param("barCode") String barCode);
}
