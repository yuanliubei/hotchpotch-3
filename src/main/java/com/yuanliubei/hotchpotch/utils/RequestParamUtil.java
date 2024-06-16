package com.yuanliubei.hotchpotch.utils;

import com.yuanliubei.hotchpotch.frame.CommonResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by beisiji on 2019/8/4.
 */
public class RequestParamUtil {


    private static final Logger log = LoggerFactory.getLogger(RequestParamUtil.class);

    public RequestParamUtil() {
    }

    public static <T> T convertToObject(Object object, Class<T> clazz) {
        return JacksonUtil.objectMapper().convertValue(object, clazz);
    }

    public static <T> T getFromMap(Map<String, Object> map, String key, Class<T> clazz) {
        Object value = map.get(key);
        if (value == null) {
            log.debug("get map key: {}, value: null", key);
            throw CommonResultCode.ERROR_PARAM_ERROR.exception("缺少参数: " + key);
        } else {
            try {
                return convertToObject(value, clazz);
            } catch (Exception var5) {
                log.debug("get map key: {}, value: {}, ClassCastException", key, value);
                throw CommonResultCode.ERROR_PARAM_ERROR.exception("参数类型错误: " + key);
            }
        }
    }

    public static <T> T getFromMap(Map<String, Object> map, String key, Class<T> clazz, T defaultValue) {
        try {
            return getFromMap(map, key, clazz);
        } catch (Exception var5) {
            return defaultValue;
        }
    }
}
