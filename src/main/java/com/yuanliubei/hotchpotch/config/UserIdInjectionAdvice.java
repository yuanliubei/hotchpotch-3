package com.yuanliubei.hotchpotch.config;

import com.yuanliubei.hotchpotch.common.AutoInjectUserId;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/6/17
 */
@ControllerAdvice
public class UserIdInjectionAdvice extends CustomInjectionAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return super.supports(methodParameter, targetType, converterType);
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String userId = request.getHeader("X-User-Id"); // 假设userId在X-User-Id header中
        if (Objects.isNull(body) || Objects.isNull(userId)) {
            return body;
        }
        // 如果是方法参数上的注解，且body是DTO，遍历其所有字段寻找@AutoInjectUserId并设置
        Arrays.stream(body.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(AutoInjectUserId.class))
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        field.set(body, userId);
                    } catch (IllegalAccessException e) {
                        // 处理异常
                        throw new RuntimeException("参数绑定异常");
                    }
                });
        return body;
    }
}
