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
    private  HashOperations hashOperations;


    public ImportRedisRepo(RedisTemplate redisTemplate){
        this.hashOperations = redisTemplate.opsForHash();
    }

    public void create(String key, ImportInWarehouseRedis redis) {
        this.hashOperations .put(key, redis.getId(), redis);
    }

    public ImportInWarehouseRedis get(String key, String redisId) {
        return (ImportInWarehouseRedis)  this.hashOperations.get(key, redisId);
    }

    public Map<String ,ImportInWarehouseRedis> getAll(int key){
        return  this.hashOperations.entries(key);
    }
    public List<ImportInWarehouseRedis> getAllList(String key){
        return  this.hashOperations.values(key);
    }



    public void update(String key, String redisKey, ImportInWarehouseRedis redis) {
        this.hashOperations.put(key,redisKey, redis);
        log.info(String.format("User with ID %s updated", redisKey));
    }

    public void delete(String key, String redisId) {
        this.hashOperations.delete(key, redisId);
        log.info(String.format("User with ID %s deleted", redisId));
    }
    public void deleteAll(String key) {
        this.hashOperations.delete(key);
    }
}
