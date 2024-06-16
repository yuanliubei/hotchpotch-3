package com.yuanliubei.hotchpotch.mapstruct;

import org.springframework.stereotype.Component;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class IntegerListStringTypeMapper extends AbsIdStringListTypeMapper<Integer>{

    @Override
    protected Integer parse(String value) {
        return Integer.parseInt(value);
    }
}
