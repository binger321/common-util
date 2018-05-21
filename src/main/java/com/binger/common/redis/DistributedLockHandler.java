package com.binger.common.redis;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: zhuyubin
 * Date: 2018/5/16
 * Time: 下午3:36
 * To change this template use File | Settings | File Templates.
 * Description:
 */
@Configuration
@ConditionalOnProperty(value = "erp.redis.distributed.lock.enable", havingValue = "true")
public class DistributedLockHandler {

    public final static long ONE_SECOND = 1000L;   // 一秒钟
    public final static long ONE_MINUTE = 60 * ONE_SECOND; // 一分钟
    public final static long ONE_HOUR = 60 * ONE_MINUTE;   // 一小时
    public final static long ONE_DAY = 24 * ONE_HOUR;  // 一天

    private final static Logger logger = LoggerFactory.getLogger(DistributedLockHandler.class);
    private final static long LOCK_EXPIRE = 30 * ONE_SECOND;//单个业务持有锁的时间30s，防止死锁
    private final static long LOCK_TRY_INTERVAL = 30L;//默认30ms尝试一次
    private final static long LOCK_TRY_TIMEOUT = 20 * 1000L;//默认尝试20s

    @Autowired
    private StringRedisTemplate template;

    /**
     * 尝试获取全局锁,  未获得全局锁则线程等待, 直到超时
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock) {
        return getLock(lock, LOCK_TRY_TIMEOUT, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁,  未获得全局锁则线程等待, 直到超时
     *
     * @param lock    锁的名称
     * @param timeout 获取超时时间 单位ms
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout) {
        return getLock(lock, timeout, LOCK_TRY_INTERVAL, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁,  未获得全局锁则线程等待, 直到超时
     *
     * @param lock        锁的名称
     * @param timeout     获取锁的超时时间
     * @param tryInterval 多少毫秒尝试获取一次
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval) {
        return getLock(lock, timeout, tryInterval, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁,  未获得全局锁则线程等待, 直到超时
     *
     * @param lock           锁的名称
     * @param timeout        获取锁的超时时间
     * @param tryInterval    多少毫秒尝试获取一次
     * @param lockExpireTime 锁的过期
     * @return true 获取成功，false获取失败
     */
    public boolean tryLock(Lock lock, long timeout, long tryInterval, long lockExpireTime) {
        return getLock(lock, timeout, tryInterval, lockExpireTime);
    }


    /**
     * 操作redis获取全局锁,  未获得全局锁则线程等待, 直到超时
     *
     * @param lock           锁的名称
     * @param timeout        获取的超时时间
     * @param tryInterval    多少ms尝试一次
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLock(Lock lock, long timeout, long tryInterval, long lockExpireTime) {
        try {
            if (StringUtils.isEmpty(lock.getName()) || StringUtils.isEmpty(lock.getValue())) {
                return false;
            }
            long startTime = System.currentTimeMillis();
            ValueOperations<String, String> ops = template.opsForValue();
            while (true) {
                if(ops.setIfAbsent(lock.getName(), lock.getValue())){ //使用setnx, 能保证只有一人设置成功
                    template.expire(lock.getName(), lockExpireTime, TimeUnit.MILLISECONDS);
                    return true;
                } else {
                    //存在锁
                    if(logger.isDebugEnabled()) {
                        logger.debug("Lock is exist!");
                    }
                }
                if (System.currentTimeMillis() - startTime > timeout) {//尝试超过了设定值之后直接跳出循环
                    return false;
                }
                Thread.sleep(tryInterval);
            }
        } catch (InterruptedException | DataAccessException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 尝试获取全局锁, 无锁则设置锁, 有锁则线程不等待, 直接返回 false
     *
     * @param lock 锁的名称
     * @return true 获取成功，false获取失败
     */
    public boolean tryLockFailFast(Lock lock) {
        return getLockFailFast(lock, LOCK_EXPIRE);
    }

    /**
     * 尝试获取全局锁, 无锁则设置锁, 有锁则线程不等待, 直接返回 false
     *
     * @param lock    锁的名称
     * @param lockExpireTime 获取成功后锁的过期时间 单位ms
     * @return true 获取成功，false获取失败
     */
    public boolean tryLockFailFast(Lock lock, long lockExpireTime) {
        return getLockFailFast(lock, lockExpireTime);
    }

    /**
     * 操作redis获取全局锁,  无锁则设置锁, 有锁则不等待, 直接返回 false
     *
     * @param lock           锁的名称
     * @param lockExpireTime 获取成功后锁的过期时间
     * @return true 获取成功，false获取失败
     */
    public boolean getLockFailFast(Lock lock, long lockExpireTime) {
        if (StringUtils.isEmpty(lock.getName()) || StringUtils.isEmpty(lock.getValue())) {
            return false;
        }
        if (!template.hasKey(lock.getName())) {
            ValueOperations<String, String> ops = template.opsForValue();
            if(ops.setIfAbsent(lock.getName(), lock.getValue())){//使用setnx, 能保证只有一人设置成功
                template.expire(lock.getName(), lockExpireTime, TimeUnit.MILLISECONDS);
                return true;
            }
        }
        return false;
    }

    /**
     * 释放锁
     */
    public void releaseLock(Lock lock) {
        if (!StringUtils.isEmpty(lock.getName())) {
            template.delete(lock.getName());
        }
    }
}
