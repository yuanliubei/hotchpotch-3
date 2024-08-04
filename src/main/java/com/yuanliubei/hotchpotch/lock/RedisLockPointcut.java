package com.yuanliubei.hotchpotch.lock;

import lombok.NonNull;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

import java.lang.reflect.Method;
import java.util.Objects;

public class RedisLockPointcut extends StaticMethodMatcherPointcut {

    private final RedisLockConfig config;

    public RedisLockPointcut(@NonNull RedisLockConfig config) {
        if (Objects.isNull(config)) {
            throw new NullPointerException("config is marked non-null but is null");
        } else {
            this.config = config;
        }
    }

    public boolean matches(Method method, Class<?> targetClass) {
        return Objects.nonNull(this.config.load(method));
    }
}
