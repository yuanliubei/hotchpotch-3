package com.yuanliubei.hotchpotch.mapstruct;

import org.springframework.stereotype.Component;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class StringListStringTypeMapper extends AbsIdStringListTypeMapper<String>{
    @Override
    protected String parse(String value) {
        return value;
    }
}
