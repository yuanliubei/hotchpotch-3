package com.yuanliubei.hotchpotch.mapstruct;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.yuanliubei.hotchpotch.utils.JacksonUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
public abstract class AbsJacksonTypeMapper<T> {

    private Class<T> clazz;
    private TypeReference<T> typeReference;
    private JavaType collectionLikeType;

    protected AbsJacksonTypeMapper(Class<T> clazz) {
        this.clazz = clazz;
        if (Objects.nonNull(clazz)) {
            this.collectionLikeType = JacksonUtil.objectMapper().getTypeFactory().constructCollectionLikeType(List.class, clazz);
        }

    }

    protected AbsJacksonTypeMapper(TypeReference<T> typeReference) {
        this.typeReference = typeReference;
        if (Objects.nonNull(typeReference)) {
            JavaType elementType = JacksonUtil.objectMapper().getTypeFactory().constructType(typeReference);
            this.collectionLikeType = JacksonUtil.objectMapper().getTypeFactory().constructCollectionLikeType(List.class, elementType);
        }
    }

    public T map(String jsonString) {
        if (Objects.nonNull(this.clazz)) {
            return JacksonUtil.fromJson(jsonString, this.clazz);
        } else if (Objects.nonNull(this.typeReference)) {
            return JacksonUtil.fromJson(jsonString, this.typeReference);
        } else {
            throw new UnsupportedOperationException("not supported");
        }
    }

    public String map(T obj) {
        return JacksonUtil.toJson(obj);
    }

    public List<T> map2List(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return Collections.emptyList();
        } else if (Objects.isNull(this.collectionLikeType)) {
            throw new UnsupportedOperationException("not supported");
        } else {
            return JacksonUtil.fromJson(jsonString, this.collectionLikeType);
        }
    }

    public String map2String(List<T> objList) {
        return JacksonUtil.toJson(objList);
    }

}
