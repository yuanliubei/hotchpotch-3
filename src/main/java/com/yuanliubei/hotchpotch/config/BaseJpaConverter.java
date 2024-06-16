package com.yuanliubei.hotchpotch.config;


import jakarta.persistence.AttributeConverter;

/**
 * @author yuanlb
 * @since 2023/5/29
 */
public class BaseJpaConverter implements AttributeConverter<String, Integer> {

    @Override
    public Integer convertToDatabaseColumn(String attribute) {
        return null;
    }

    @Override
    public String convertToEntityAttribute(Integer dbData) {
        return null;
    }
}
