package com.yuanliubei.hotchpotch.lock;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class RedisLockConfig {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
    private final Map<Method, CachedMethodConfig> method2Annotation = new ConcurrentHashMap<>();

    public RedisLockConfig() {
    }

    public CachedMethodConfig load(Method method) {
        RedisLockRequire annotation =  AnnotationUtils.findAnnotation(method, RedisLockRequire.class);
        if (Objects.isNull(annotation)) {
            return null;
        } else {
            CachedMethodConfig methodConfig = (new CachedMethodConfig()).setMethod(method).setAnnotation(annotation);
            if (annotation.keyType() == RedisLockRequire.KeyType.EL) {
                methodConfig.setExpression(parser.parseExpression(annotation.key()));
                methodConfig.setParameterNames(parameterNameDiscoverer.getParameterNames(method));
            }

            this.method2Annotation.put(method, methodConfig);
            return methodConfig;
        }
    }

    public CachedMethodConfig get(Method method) {
        return this.method2Annotation.get(method);
    }
}
