package com.vibee.jedis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class ExportRedis implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Indexed
    private String id;
    @Indexed
    private BigDecimal inPrice;
    @Indexed
    private BigDecimal outPrice;
    @Indexed
    private int unitId;
    @Indexed
    private String file;
    @Indexed
    private String qrCode;

}
