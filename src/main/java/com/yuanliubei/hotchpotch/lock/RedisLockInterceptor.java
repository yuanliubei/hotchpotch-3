package com.yuanliubei.hotchpotch.lock;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class RedisLockInterceptor implements MethodInterceptor {

    private final RedisLockConfig config;

    public RedisLockInterceptor(@Nonnull RedisLockConfig config) {
        this.config = config;
    }

    @Nullable
    public Object invoke(@Nonnull MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        CachedMethodConfig methodConfig = this.config.get(method);
        Assert.notNull(methodConfig, "config error");
        RedisLockRequire annotation = methodConfig.getAnnotation();
        String key = this.generateKey(methodConfig, methodInvocation);
        RLock lock = LockUtil.getLock(key);
        Boolean getLockFlag = tryLock(lock, annotation);
        if (BooleanUtils.isNotTrue(getLockFlag)) {
            log.debug("get lock failed [{}]", key);
            String failMessage = StringUtils.isNotBlank(annotation.lockFailMessage()) ? annotation.lockFailMessage() : "当前繁忙，请稍后重试";
            throw new RuntimeException(failMessage);
        }
        //得到锁,执行方法，释放锁
        log.info("get lock success [{}]", key);
        try {
            return methodInvocation.proceed();
        } catch (Exception e) {
            log.error("execute locked method occured an exception", e);
        } finally {
            if (BooleanUtils.isTrue(lock.isLocked())) {
                LockUtil.unLock(lock);
            }
            log.info("release lock [{}]", key);
        }
        return null;
    }

    private Boolean tryLock(RLock lock, RedisLockRequire annotation) {
        if (annotation.lockExpireTimeInMillis() >= 0) {
            return LockUtil.tryLockNeedUnlockWithTTl(lock, annotation.waitTimeInMillis(), annotation.lockExpireTimeInMillis());
        }else {
            return LockUtil.tryLockAndWaitAndNeedUnlock(lock, annotation.waitTimeInMillis());
        }
    }

    private String generateKey(CachedMethodConfig config, MethodInvocation methodInvocation) {
        Method method = config.getMethod();
        RedisLockRequire annotation = config.getAnnotation();
        StringBuilder sb = new StringBuilder();
        if (annotation.useMethodNameAsKeyPrefix()) {
            sb.append(method.getDeclaringClass().getSimpleName()).append(".").append(method.getName());
        }

        String key = switch (annotation.keyType()) {
            case STRING -> annotation.key();
            case EL -> this.generateKey4SpEL(config, methodInvocation);
            default -> throw new RuntimeException("key type not supported: " + annotation.keyType());
        };

        if (StringUtils.isNotBlank(key)) {
            sb.append(key);
        }

        String finalKey = sb.toString();
        if (StringUtils.isBlank(finalKey)) {
            throw new IllegalArgumentException("invalid key, check config: " + method.getDeclaringClass().getSimpleName() + "." + method.getName());
        } else {
            return finalKey;
        }
    }

    private String generateKey4SpEL(CachedMethodConfig config, MethodInvocation methodInvocation) {
        EvaluationContext context = new StandardEvaluationContext();
        Expression expression = config.getExpression();
        String[] parameterNames = config.getParameterNames();
        if (Objects.nonNull(parameterNames)) {
            for (int i = 0; i < parameterNames.length; ++i) {
                context.setVariable(parameterNames[i], methodInvocation.getArguments()[i]);
            }
        }

        Object value = expression.getValue(context);
        Assert.notNull(value, "generate key fail");
        return value.toString();
    }
}
