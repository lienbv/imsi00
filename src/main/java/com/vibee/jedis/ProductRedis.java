package com.vibee.jedis;

import com.vibee.entity.VProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@RedisHash
@Data
public class ProductRedis implements Serializable {
    @Id
    @Indexed
    private String idRedis;
    @Indexed
    private VProduct vProduct;
//    @Indexed
//    private int id;
//    @Indexed
//    private String productName;
//    @Indexed
//    private int productType;
//    @Indexed
//    private Date createdDate;
//    @Indexed
//    private int status;
//    @Indexed
//    private String barCode;
//    @Indexed
//    private String description;
//    @Indexed
//    private int fileId;
//    @Indexed
//    private String creator;
//    @Indexed
//    private String supplierName;

}
