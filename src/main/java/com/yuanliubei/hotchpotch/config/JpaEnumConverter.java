package com.yuanliubei.hotchpotch.config;

import com.yuanliubei.hotchpotch.enums.IntValueEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.lang.reflect.ParameterizedType;

/**
 * @author yuanlb
 * @since 2023/5/23
 */
@Converter
public class JpaEnumConverter<E extends Enum<E> & IntValueEnum> implements AttributeConverter<E, Integer> {

    private Class<E> clazz;

    public JpaEnumConverter(Class<E> clazz) {
        this.clazz = clazz;
    }

    public JpaEnumConverter() {
        this.clazz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        return attribute.getIntValue();
    }

    @Override
    public E convertToEntityAttribute(Integer dbData) {

        return IntValueEnum.valueOf(dbData, clazz);
    }
}
