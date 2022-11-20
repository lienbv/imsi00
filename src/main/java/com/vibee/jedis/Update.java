package com.vibee.jedis;

import com.vibee.model.item.SelectExportItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash
public class Update implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Indexed
    private String key;
    @Indexed
    private int importId;
    @Indexed
    private String productName;
    @Indexed
    private String img;
    @Indexed
    private String barCode;
    @Indexed
    private int amount;
    @Indexed
    private List<SelectExportItem> items;
    @Indexed
    private int productId;
}
