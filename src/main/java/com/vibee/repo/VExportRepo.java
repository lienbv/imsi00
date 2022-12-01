package com.vibee.repo;

import com.vibee.entity.VExport;
import com.vibee.model.ObjectResponse.ExportStallObject;
import com.vibee.model.ObjectResponse.SelectExportStallObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VExportRepo extends JpaRepository<VExport,Integer> {
    @Query(value="select export.id as exportId, export.out_price as outPrice, (export.out_amount/unit.amount) as outAmount,unit.unit_name unitName, \n" +
            "unit.parent_id as parentId, unit.id as unitId, unit.amount as amount  from v_unit join v_export on v_unit.id=v_export.id_unit join v_warehouse on v_warehouse.id=v_export.id_warehouse " +
            "where v_export.id_warehouse = ?1 AND export.status=1 group by v_export.id",nativeQuery = true)
    List<ExportStallObject> viewStall( int warehouseId);

    @Query(value="select v_export.id as exportId, v_export.out_price as outPrice,v_unit.unit_name unitName,\n" +
            " v_unit.id as unitId, ((v_warehouse.in_amount-v_warehouse.out_amount)/v_unit.amount) as inventory, v_unit.amount as amount  from v_unit join v_export on v_unit.id=v_export.id_unit \n" +
            " join v_import on v_import.id=v_export.import_id\n" +
            " join v_warehouse on v_warehouse.id=v_import.warehouse_id \n" +
            "where v_import.product_code =?1 AND v_export.status=1 group by v_export.id",nativeQuery = true)
    List<SelectExportStallObject> getExportsByProduct(String productCode);

    @Query(value="select v_export.id as exportId, v_export.out_price as outPrice,v_unit.unit_name unitName,\n" +
            " v_unit.id as unitId, ((v_warehouse.in_amount-v_warehouse.out_amount)/v_unit.amount) as inventory, v_unit.amount as amount  from v_unit join v_export on v_unit.id=v_export.id_unit \n" +
            " join v_import on v_import.id=v_export.import_id\n" +
            " join v_warehouse on v_warehouse.id=v_import.warehouse_id \n" +
            "where v_import.product_id =?1 AND v_export.status=1 group by v_export.id",nativeQuery = true)
    List<SelectExportStallObject> getExportsByProduct(int productCode);


}
