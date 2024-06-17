package com.yuanliubei.hotchpotch.config;

import com.yuanliubei.hotchpotch.common.AutoInject;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * @author yuanlb
 * @since 2024/6/17
 */
public class CustomInjectionAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 检查方法参数或其类型是否标记了@AutoInject
        return methodParameter.hasParameterAnnotation(AutoInject.class) ||
                methodParameter.getParameterType().isAnnotationPresent(AutoInject.class);
    }
}
