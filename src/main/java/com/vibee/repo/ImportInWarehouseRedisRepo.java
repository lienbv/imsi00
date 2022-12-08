package com.vibee.repo;

import com.vibee.jedis.ImportInWarehouseRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ImportInWarehouseRedisRepo extends CrudRepository<ImportInWarehouseRedis, String> {
Optional<ImportInWarehouseRedis> findBySupplierId(int id);
List<ImportInWarehouseRedis> findByBarcode(String barcode);
}
