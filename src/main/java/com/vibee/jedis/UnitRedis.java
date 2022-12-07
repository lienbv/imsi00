package com.vibee.jedis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class UnitRedis {
    @Id
    @Indexed
    private String id;
    @Indexed
    private int idUnit;
    @Indexed
    private String name;
    @Indexed
    private  int parentId;
    @Indexed
    private int amount;
}
