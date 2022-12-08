package com.vibee.repo;

import com.vibee.jedis.UnitRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRedisRepo extends CrudRepository<UnitRedis, String> {
}
