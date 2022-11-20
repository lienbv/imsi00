package com.vibee.config;

import com.vibee.config.exception.BackToWaitQueueException;
import com.vibee.config.redis.RedisAdapter;
import com.vibee.utils.CommonUtil;
import org.springframework.beans.factory.DisposableBean;


/**
 * @author thangnq.os
 * base queue listener
 */

public abstract class BaseQueueListener implements DisposableBean, Runnable{
    private Thread thread;
    private String queue;
    private volatile boolean stop;
    private RedisAdapter redisAdapter;

    public BaseQueueListener() {

    }

    public BaseQueueListener(RedisAdapter redisAdapter, String queue) {
        this.redisAdapter = redisAdapter;
        this.queue = queue;
        this.stop = false;
        this.thread = new Thread(this);
        this.thread.start();
    }

    @Override
    public void run() {
        while (!stop) {
            String message = redisAdapter.getJobAndSendToWorkQueue(queue);
            if (!CommonUtil.isNullOrEmpty(message)) {
                try {
                    String result = onMessage(message);
                } catch (BackToWaitQueueException e) {
                    redisAdapter.returnJobBackToWaitQueue(queue, message);
                } catch (Exception ex) {
                    redisAdapter.sendJobToIncompleteQueue(message);
                } finally {
                    redisAdapter.removeJobFromWorkQueue(queue, message);
                }
            }
            try {
                Thread.sleep(1);
            } catch (Exception e) {
            }
        }
    }

    public abstract String onMessage(String message) throws BackToWaitQueueException;

    @Override
    public void destroy() throws Exception {
        stop = true;
    }
}
