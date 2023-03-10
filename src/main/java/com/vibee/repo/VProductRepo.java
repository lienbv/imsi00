package com.vibee.repo;

import com.vibee.entity.VProduct;
import com.vibee.model.ObjectResponse.*;
import com.vibee.model.response.product.IgetHomeSellOnline;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface VProductRepo extends JpaSpecificationExecutor<VProduct>,JpaRepository<VProduct, Integer>{
    @Query(value = "select v_product.id as productId,v_product.file_id as fileId,v_product.name_product as productName,v_warehouse.number_of_entries as countWarehouse, v_warehouse.in_amount as inAmount, v_warehouse.out_amount as outAmount, " +
            "v_warehouse.out_price as outPrice, v_warehouse.in_price as inPrice, v_product.status as status from v_product \n" +
            "join v_warehouse on v_warehouse.product_id=v_product.id \n" +
            "join v_unit on v_unit.id=v_warehouse.unit_id where v_product.id=?1",nativeQuery = true)
    GetProductObject getProductById(int id);


    @Query("SELECT count(p) FROM product p WHERE p.barCode like :barCode")
    long countProductByBarCode(String barCode);

    @Modifying
    @Query("UPDATE product p SET p.status = 4 WHERE p.id= :productId")
    int delete(@Param("productId") int productId);

    @Modifying
    @Query("UPDATE product p SET p.status = 3 WHERE p.id= :productId")
    int lock(@Param("productId") int productId);

    @Modifying
    @Query("UPDATE product p SET p.status = 1 WHERE p.id= :productId")
    int unLock(@Param("productId") int productId);

    @Query(value = "SELECT id ,name_product as productName,bar_code as barCode FROM v_product where status=1",nativeQuery = true)
    List<ProductStallObject> viewStall(Pageable pageable);

    @Query("SELECT p.id as productId,p.productName as productName ,p.barCode as barCode, (SELECT u.url FROM uploadFile u WHERE u.id=p.fileId) as img FROM product p JOIN warehouse w on w.productId=p.id JOIN import i on i.warehouseId = w.id where i.status=1 AND p.barCode LIKE :barCode")
    List<ProductStallObject> searchProductByBarCode(@Param("barCode") String searchValue);

    @Query("SELECT p.id as productId,p.productName as productName ,p.barCode as barCode, (SELECT u.url FROM uploadFile u WHERE u.id=p.fileId) as img FROM product p JOIN warehouse w on w.productId=p.id JOIN import i on i.warehouseId = w.id WHERE i.status=1 AND p.productName LIKE :productName")
    List<ProductStallObject> searchProductByproductName(@Param("productName") String searchValue);

    @Query("SELECT p.id as productId,p.productName as productName ,p.barCode as barCode, (SELECT u.url FROM uploadFile u WHERE u.id=p.fileId) as img FROM product p where p.status=1 AND p.id= :productId")
    ProductStallObject searchProductById(@Param("productId") int productId);

    @Query("select p.id,p.productName,p.status,p.fileId,w.inAmount-w.outAmount,w.outPrice-w.inPrice,p.supplierName , w.unitId,w.outPrice, w.inPrice from product p JOIN warehouse w ON w.productId=p.id where p.productName like :productName% ")
    List<Object> getProducts(@Param("productName") String search, Pageable pageable);

    @Query("SELECT count(p) FROM product p WHERE p.productName like %:productName% AND p.status=1")
    long countProduct(String productName);

    @Query("SELECT count(p) FROM product p WHERE p.status=1")
    long countProduct();

    @Query(value = "SELECT v_product.created_date,v_user.fullname,v_product.status,v_product.bar_code,v_product.description,v_type_product.name,v_product.img,v_product.name_product\n" +
            "from v_product join v_type_product on v_type_product.id=v_product.type_product \n" +
            "join v_warehouse on v_warehouse.id_product=v_product.id join v_export on v_export.id_warehouse=v_warehouse.id join v_unit on v_unit.id=export.id_unit join v_user on v_user.id=v_product.creator where v_product.id= ?1 group by v_product.id", nativeQuery = true)
    Object findProductById( int id);

    @Query("SELECT p.id as productId,p.productName as productName ,p.barCode as barCode, (SELECT u.url FROM uploadFile u WHERE u.id=p.fileId) as img, i.id as importId, i.productCode as productCode FROM product p JOIN warehouse w on w.productId=p.id JOIN import i on i.warehouseId = w.id WHERE i.status=1 AND i.productCode LIKE :barCode")
    List<ProductStallObject> searchProductByProductCode(@Param("barCode") String searchValue);

    @Query("SELECT p.id as productId,p.productName as productName ,p.barCode as barCode, (SELECT u.url FROM uploadFile u WHERE u.id=p.fileId) as img,i.id as importId FROM product p JOIN warehouse w on w.productId=p.id JOIN import i on i.warehouseId = w.id where i.status=1 AND i.productCode= :productCode")
    ProductStallObject searchProductByImport(@Param("productCode") String productCode);
    List<VProduct> findById(int id);
    @Query(value = "select v_import.ID as importId, v_product.BAR_CODE as baseCode, v_product.NAME_PRODUCT as productName, v_upload_file.FILE_NAME as fileName," +
            " v_warehouse.OUT_AMOUNT as outAmount,\n" +
            "v_warehouse.OUT_PRICE as outPrice from v_import join v_warehouse on v_import.WAREHOUSE_ID = v_warehouse.ID join v_product\n" +
            "on v_product.ID = v_warehouse.PRODUCT_ID join v_upload_file on v_product.FILE_ID = v_upload_file.ID", nativeQuery = true)
    List<IgetHomeSellOnline> getHomeSellOnline();
    @Query("SELECT CASE WHEN COUNT(p)>0 THEN TRUE ELSE FALSE END FROM product p WHERE p.barCode= :barcode")
    Boolean existsByBarcode(@Param("barcode") String barcode);

    @Query("SELECT count (p.status) FROM product  p WHERE p.status = 2")
    public Optional<Long> sumReportBlockProduct();

    @Query("SELECT count(p.status) FROM product p WHERE p.status = 3")
    public Optional<Long> sumReportSoldOutProduct();

    public List<VProduct> findTop6ByOrderByCreatedDateDesc();

    VProduct findByBarCodeAndStatus(String barcode, int status_1);
//    VProduct findByBarCodeAndStatusOrStatus(String barcode, int status_1, int status_2);
@Query("SELECT p FROM product p WHERE p.barCode= :barcode AND p.status= :status or p.status= :active")
    VProduct findByBarCodeAndStatusOrStatus(@Param("barcode") String barcode,@Param("status") int status_1,@Param("active") int status_2);

    @Query("select max (p.id) from product p")
    int findMaxId();

    @Query(value = "select p.BAR_CODE as barCode,  i.EXPIRED_DATE as expiredDate from v_product p join v_warehouse w on p.ID = w.PRODUCT_ID \n" +
            "join v_import i on i.WAREHOUSE_ID = w.ID where p.BAR_CODE =?1", nativeQuery = true)
    List<ImportInWarehouseObject>findByBarcodeAndRangeDate(String barCode);

    @Query("SELECT p FROM product p WHERE p.id= :id")
    VProduct getProduct(@Param("id") int id);

    @Query("SELECT p FROM product p WHERE p.status=1 AND p.productName LIKE %:productName%")
    List<VProduct> searchProductByName(@Param("productName") String productName, Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.status=1")
    List<VProduct> searchProductByName( Pageable pageable);

    @Query("SELECT p FROM product p WHERE p.status=1 AND p.barCode = :barCode")
    VProduct getProductByBarCode(@Param("barCode") String barCode);

    @Query("SELECT p FROM product p WHERE p.status=1 AND p.id=(SELECT w.productId FROM warehouse w WHERE w.id= :warehouseId)")
    VProduct getProductByWarehouseId(@Param("warehouseId") int warehouseId);
    @Query(value = "SELECT count(p.id) from v_product p join v_type_product t \n" +
            "on p.TYPE_PRODUCT = t.ID join v_warehouse w on p.ID = w.PRODUCT_ID \n" +
            "join v_import i on i.WAREHOUSE_ID = w.ID join v_export e on e.IMPORT_ID = i.ID\n" +
            "join v_unit u on u.ID = w.UNIT_ID \n" +
            "where t.ID=?1 \n" +
            "having sum(w.IN_AMOUNT) - sum(w.OUT_AMOUNT) >0" , nativeQuery = true)
    String amountProductByType(int idType);
    @Query(value = "SELECT count(p.id) from v_product p join v_type_product t \n" +
            "on p.TYPE_PRODUCT = t.ID join v_warehouse w on p.ID = w.PRODUCT_ID \n" +
            "join v_import i on i.WAREHOUSE_ID = w.ID join v_export e on e.IMPORT_ID = i.ID\n" +
            "join v_unit u on u.ID = w.UNIT_ID \n" +
            "where t.PARENT_ID=?1 \n" +
            "having sum(w.IN_AMOUNT) - sum(w.OUT_AMOUNT) >0" , nativeQuery = true)
    String amountProductByType1(int idType);

    @Query(value = "select p.ID as id, p.NAME_PRODUCT as productName, p.BAR_CODE as barcode, p.DESCRIPTION as description, p.NAME_SUPPLIER as supplierName,\n" +
            "            i.ID as importID, i.EXPIRED_DATE as expired, p.FILE_ID as files, i.FILE_ID as fileImport\n" +
            "            from vibee.v_product p join vibee.v_warehouse w on p.ID = w.PRODUCT_ID\n" +
            "            join vibee.v_import i on i.WAREHOUSE_ID = w.ID\n" +
            "            where p.STATUS =1\n" +
            "            group by importID\n" +
            "            order by i.EXPIRED_DATE asc ", nativeQuery = true)
    List<ShowProductStaff> showProduct();
}
