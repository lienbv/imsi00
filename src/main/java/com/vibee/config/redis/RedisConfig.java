package com.vibee.config.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "vibee.redis")
@Component
public class RedisConfig {
	private boolean isCluster;
	private String host;
	private int port;
	private int timeout;
	private String password;
	private int database;
	private int maxTotal;
	private int maxIdle;
	private int maxWait;
}
