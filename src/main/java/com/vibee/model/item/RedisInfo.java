package com.vibee.model.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "vibee.redis")
@Component
public class RedisInfo {
    private boolean isCluster;
    private String host;
    private int port;
    private int timeOut;
    private String password;
    private int database;
    private int maxTotal;
    private int maxIdle;
    private int maxWait;
}
