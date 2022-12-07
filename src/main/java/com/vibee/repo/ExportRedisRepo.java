package com.vibee.repo;

import com.vibee.jedis.ExportRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExportRedisRepo extends CrudRepository<ExportRedis, String> {
    List<ExportRedis> findByImports(String imports);
}
