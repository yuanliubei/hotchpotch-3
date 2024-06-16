package com.yuanliubei.hotchpotch.utils;

/**
 * @author yuanlb
 * @since 2023/4/16
 */
public class ThreadLocalUtil {

    private static ThreadLocal<Long> SESSION_USER_ID_THREAD_LOCAL = new ThreadLocal<>();

    private ThreadLocalUtil() {
    }

    public static void setSessionUserId(Long userId) {
        SESSION_USER_ID_THREAD_LOCAL.set(userId);
    }

    public static Long getSessionUserId() {
        return SESSION_USER_ID_THREAD_LOCAL.get();
    }
}
