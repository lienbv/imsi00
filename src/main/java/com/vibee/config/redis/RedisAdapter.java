package com.vibee.config.redis;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vibee.utils.CommonUtil;
import com.vibee.utils.Constant;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.*;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

/**
 * @author thangnq.os redis queue adapter
 */
@Component
public class RedisAdapter implements DisposableBean {
    private final String WORK_QUEUE_POSTFIX = "_WORK";
    private static JedisPool jedisPool = null;
    private static JedisCluster jedisCluster = null;
    private final int DEFAULT_TIME_OUT = 10;
    protected final Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisConfig redisConfig;

    public long sendJobToWaitQueue(String queue, String job) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.lpush(queue, job);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                return jedis.lpush(queue, job);
            } catch (Exception e) {
                logger.error("getJobAndSendToWorkQueue redis error : " + e.getMessage());
                return -1;
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public String getJobAndSendToWorkQueue(String queue) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.brpoplpush(queue, queue + WORK_QUEUE_POSTFIX, DEFAULT_TIME_OUT);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                // set timeout to release connection will make server more available , event it
                // shutdown
                return jedis.brpoplpush(queue, queue + WORK_QUEUE_POSTFIX, DEFAULT_TIME_OUT);
            } catch (Exception e) {
                logger.error("getJobAndSendToWorkQueue redis error : " + e.getMessage());
                return null;
            } finally {
                returnToPool(jedis);
            }
        }

    }

    private JedisCluster getJedisCluster() {
        if (jedisCluster == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(redisConfig.getMaxTotal());
            config.setMaxIdle(redisConfig.getMaxIdle());
            String[] urlList = redisConfig.getHost().split(",");
            Set<HostAndPort> hostAndPortList = new HashSet<HostAndPort>();
            for (String url : urlList) {
                String[] ss = url.split(":");
                HostAndPort hostAndPort = new HostAndPort(ss[0], Integer.parseInt(ss[1]));
                hostAndPortList.add(hostAndPort);
            }
            jedisCluster = new JedisCluster(hostAndPortList, (JedisClientConfig) config);
        }
        return jedisCluster;
    }

    private JedisPool getRedisPool() {
        if (jedisPool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            jedisPool = new JedisPool(config, redisConfig.getHost(), redisConfig.getPort());
        }
        return jedisPool;
    }

    public <T> boolean set(String key, int expireInSeconds, T value) {
        if (CommonUtil.isNullOrEmpty(key)) {
            return false;
        }
        String str = CommonUtil.beanToString(value);
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            if (expireInSeconds < 1) {
                jedisCluster.set(key, str);
            } else {
                jedisCluster.setex(key, expireInSeconds, str);
            }
            return true;
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                if (expireInSeconds < 1) {
                    jedis.set(key, str);
                } else {
                    jedis.setex(key, expireInSeconds, str);
                }
                return true;
            } catch (Exception e) {
                logger.error("Set redis error : " + e.getMessage());
                return false;
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public <T> T get(String key, Class<T> clazz) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            String str = jedisCluster.get(key);
            return CommonUtil.stringToBean(str, clazz);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                String str = jedis.get(key);
                return CommonUtil.stringToBean(str, clazz);
            } catch (Exception e) {
                logger.error("Get redis error : " + e.getMessage());
                return null;
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public boolean exists(String key) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.exists(key);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                return jedis.exists(key);
            } catch (Exception e) {
                logger.error("exists redis error : " + e.getMessage());
                return false;
            } finally {
                returnToPool(jedis);
            }
        }

    }

    public boolean delete(String key) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            long ret = jedisCluster.del(key);
            return ret > 0;
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                long ret = jedis.del(key);
                return ret > 0;
            } catch (Exception e) {
                logger.error("delete redis error : " + e.getMessage());
                return false;
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public boolean deleteByPattern(String pattern) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            Set<String> matchingKeys = new HashSet<>();
            ScanParams params = new ScanParams();
            params.match("*" + pattern + "*");

            String nextCursor = "0";

            do {
                ScanResult<String> scanResult = jedisCluster.scan(nextCursor, params);
                List<String> keys = scanResult.getResult();
                nextCursor = scanResult.getCursor();

                matchingKeys.addAll(keys);

            } while (!nextCursor.equals("0"));

            if (matchingKeys.size() == 0) {
                return false;
            }

            long ret = jedisCluster.del(matchingKeys.toArray(new String[matchingKeys.size()]));
            return ret > 0;
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                Set<String> matchingKeys = new HashSet<>();
                ScanParams params = new ScanParams();
                params.match("*" + pattern + "*");

                String nextCursor = "0";

                do {
                    ScanResult<String> scanResult = jedis.scan(nextCursor, params);
                    List<String> keys = scanResult.getResult();
                    nextCursor = scanResult.getCursor();

                    matchingKeys.addAll(keys);

                } while (!nextCursor.equals("0"));

                if (matchingKeys.size() == 0) {
                    return false;
                }

                long ret = jedis.del(matchingKeys.toArray(new String[matchingKeys.size()]));
                return ret > 0;
            } catch (Exception e) {
                logger.error("delete redis error : " + e.getMessage());
                return false;
            } finally {
                returnToPool(jedis);
            }
        }
    }

    @Override
    public void destroy() throws Exception {
        if (jedisCluster != null) {
            jedisCluster.close();
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public Long returnJobBackToWaitQueue(String queue, String job) {
        Long count = removeJobFromWorkQueue(queue, job);
        if (count > 0) {
            sendJobToWaitQueue(queue, job);
        }
        return count;
    }

    public Long sendJobToIncompleteQueue(String job) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.lpush(Constant.GLOBAL_INCOMPLETE_QUEUE, job);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                return jedis.lpush(Constant.GLOBAL_INCOMPLETE_QUEUE, job);
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public Long removeJobFromWorkQueue(String queue, String job) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.lrem(queue, -1, job);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                return jedis.lrem(queue, -1, job);
            } finally {
                returnToPool(jedis);
            }
        }
    }

    public Long getTimeToLive(String key) {
        if (redisConfig.isCluster()) {
            JedisCluster jedisCluster = getJedisCluster();
            return jedisCluster.ttl(key);
        } else {
            Jedis jedis = null;
            try {
                jedis = getRedisPool().getResource();
                return jedis.ttl(key);
            } catch (Exception e) {
                logger.error("Get redis error : " + e.getMessage());
                return null;
            } finally {
                returnToPool(jedis);
            }
        }
    }
}
