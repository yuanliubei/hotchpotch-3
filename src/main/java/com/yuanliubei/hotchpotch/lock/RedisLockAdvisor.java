package com.yuanliubei.hotchpotch.lock;

import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisLockAdvisor extends AbstractBeanFactoryPointcutAdvisor {
    private final RedisLockConfig config = new RedisLockConfig();

    public RedisLockAdvisor() {
        this.setAdvice(new RedisLockInterceptor(this.config));
    }

    public Pointcut getPointcut() {
        return new RedisLockPointcut(this.config);
    }
}
