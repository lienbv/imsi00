package com.vibee.repo;

import com.vibee.jedis.ImportInWarehouseRedis;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Log4j2
@Repository
public class ImportRedisRepo {
//    private  HashOperations hashOperations;
    @Autowired
    private RedisTemplate redisTemplate;

//    public ImportRedisRepo(){
//        this.hashOperations = redisTemplate.opsForHash();
//    }

    public void create(String key, ImportInWarehouseRedis redis) {
        this.redisTemplate.opsForHash().put(key, redis.getId(), redis);
    }

    public ImportInWarehouseRedis get(String key, String redisId) {
        return (ImportInWarehouseRedis) this.redisTemplate.opsForHash().get(key, redisId);
    }

    public Map<String ,ImportInWarehouseRedis> getAll(int key){
        return  this.redisTemplate.opsForHash().entries(key);
    }
    public List<ImportInWarehouseRedis> getAllList(String key){
        return  this.redisTemplate.opsForHash().values(key);
    }



    public void update(String key, String redisId, ImportInWarehouseRedis redis) {
        this.redisTemplate.opsForHash().put(key,redisId, redis);
        log.info(String.format("User with ID %s updated", redis.getId()));
    }

    public void delete(String key, String redisId) {
        this.redisTemplate.opsForHash().delete(key, redisId);
        log.info(String.format("User with ID %s deleted", redisId));
    }
    public void deleteAll(String key) {
      this.redisTemplate.delete(key);
    }
}
