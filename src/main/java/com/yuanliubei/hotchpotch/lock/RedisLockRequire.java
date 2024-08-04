package com.yuanliubei.hotchpotch.lock;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLockRequire {

    KeyType keyType() default RedisLockRequire.KeyType.STRING;

    boolean useMethodNameAsKeyPrefix() default true;

    String key() default "";

    long waitTimeInMillis() default 0;

    long lockExpireTimeInMillis() default 0;

    String lockFailMessage() default "";

    public static enum KeyType {
        STRING,
        EL;

        private KeyType() {
        }
    }

}
