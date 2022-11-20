package com.vibee.repo;

import com.vibee.entity.VTypeProduct;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VTypeProductRepo extends JpaRepository<VTypeProduct, Integer> {
    List<VTypeProduct> findByStatus(int status);

    @Query("select t.id from VTypeProduct t where t.parentId=0 and t.status=1")
    List<Integer> listId(Pageable pageable);

    @Query("select t from VTypeProduct t where t.parentId in(select t1.id from VTypeProduct t1 where t1.parentId in( ?1)) and t.status=1 " +
            "or t.id in(select t2.parentId from VTypeProduct t2 where t2.parentId in(?1) ) and t.status=1" +
            "or t.id in(select t3.id from VTypeProduct t3 where t3.parentId in(?1)) and t.status=1")
    List<VTypeProduct> findByAll1(List<Integer> listId);

    List<VTypeProduct> findByParentIdAndStatus(int idParent, int status);

    @Query("select t.id from VTypeProduct t where t.name like ?1 ")
    List<Integer> listIdParent(String name, Pageable pageable);

    @Query("select t1 from VTypeProduct t1 where t1.id in (?1) and t1.status=1 " +
            "or t1.id in (select t2.parentId from VTypeProduct t2 where t2.id in (?1) and t1.status=1)" +
            "or t1.parentId in (?1) and  t1.status=1")
    List<VTypeProduct> searchNameByPageAndFilter(List<Integer> listId);

    @Query("select t from VTypeProduct t where t.name like ?1 and t.parentId =0")
    List<VTypeProduct> listId(String name);

    @Query("select t.id, t.parentId, t.name from VTypeProduct t where t.parentId=0 and t.status=1")
    List<Object> getByParentIdAndStatus();

    VTypeProduct findById(int id);
    @Query("select t from VTypeProduct  t where t.id =?1")
    List<VTypeProduct> findId(int id);

    @Transactional
    @Modifying
    @Query(value = "update VTypeProduct t set t.status=2 where t.id in (?1)")
    void updateTypeProductBystatus(int id);

    @Query("select t from VTypeProduct t where t.id=?1 and t.status=1")
    VTypeProduct getByParentIdAndStatus(int id);
}
