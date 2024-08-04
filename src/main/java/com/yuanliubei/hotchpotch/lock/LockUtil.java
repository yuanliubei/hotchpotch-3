package com.yuanliubei.hotchpotch.lock;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.lang.Nullable;

import java.util.concurrent.TimeUnit;

/**
 * @author yuanlb
 * @since 2024/7/21
 */
@Slf4j
public class LockUtil {

    private static RedissonClient REDISSON_CLIENT;

    static {
        REDISSON_CLIENT = SpringUtil.getBean(RedissonClient.class);
    }

    public static RLock getLock(String lockKey) {
        return REDISSON_CLIENT.getLock(lockKey);
    }

    @Nullable
    public static Boolean tryLockNeedUnlock(RLock redissonLock) {
        try {
            return redissonLock.tryLock();
        } catch (Exception e) {
            log.error("Redis tryLock failed {}", redissonLock.getName(), e);
            return null;
        }
    }

    @Nullable
    public static Boolean tryLockAndWaitAndNeedUnlock(RLock redissonLock, long waitMillisSeconds) {
        try {
            return redissonLock.tryLock(waitMillisSeconds,TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Redis tryLock failed {}", redissonLock.getName(), e);
            return null;
        }
    }

    @Nullable
    public static Boolean tryLockNeedUnlockWithTTl(RLock redissonLock, long waitTimeInMillis, long expireMillisSeconds) {
        try {
            return redissonLock.tryLock(waitTimeInMillis, expireMillisSeconds, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Redis tryLock failed {}", redissonLock.getName(), e);
            return false;
        }
    }

    @Nullable
    public static void unLock(RLock redissonLock) {
        try {
            redissonLock.unlock();
        } catch (Exception e) {
            log.error(String.format("Redis redisson unlock = %s failed", redissonLock.getName()), e);
        }
    }
}
