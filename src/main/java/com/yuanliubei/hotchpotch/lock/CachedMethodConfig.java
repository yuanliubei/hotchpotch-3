package com.yuanliubei.hotchpotch.lock;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.expression.Expression;

import java.lang.reflect.Method;

@Accessors(chain = true)
@Data
public class CachedMethodConfig {

    private Method method;
    private RedisLockRequire annotation;
    private Expression expression;
    private String[] parameterNames;
}
