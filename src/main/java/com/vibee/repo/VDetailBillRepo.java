package com.vibee.repo;

import com.vibee.entity.VDetailBill;
import com.vibee.entity.VTypeProduct;
import com.vibee.model.ObjectResponse.DetailBillOfDetailDebit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VDetailBillRepo extends JpaSpecificationExecutor<VDetailBill>, JpaRepository<VDetailBill, Integer> {
    @Query("select sum(db.amount) from VDetailBill db where db.status = ?1 and db.billId = ?2")
    Optional<Long> findBySumQuantity(int status, int billId);

    @Query(value="select new VTypeProduct(tp.id ,tp.name) from VTypeProduct tp " +
            "join VProduct p on p.typeProd = tp.id " +
            "join VDetailProduct dp on dp.productId = p.id " +
            "join VDetailBill db on db.detailProductId = dp.id " +
            "GROUP by p.id",nativeQuery = true)
    List<VTypeProduct> findByTop5(Pageable pageable);
    @Query(value = " SELECT p.ID as idProduct,b.id as idBill, db.id as idDetailBill,  p.NAME_PRODUCT as productName, i.id as idImport , db.UNIT_ID as unitId,\n" +
            "            db.price as priceDetailBill, db.amount as amountDetailBill  from v_bill b join v_detail_bill db on b.id= db.id_bill \n" +
            "            join v_import i on i.id = db.import_id\n" +
            "            join v_warehouse w on w.id = i.warehouse_id join v_product p on p.id = w.product_id WHERE b.id =?1", nativeQuery = true)
    List<DetailBillOfDetailDebit> findByProductByBill( int idBill);

    List<VDetailBill> findByBillId(int id);
}
