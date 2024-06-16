package com.yuanliubei.hotchpotch.mapstruct;

import org.springframework.stereotype.Component;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class LongListStringTypeMapper extends AbsIdStringListTypeMapper<Long>{
    @Override
    protected Long parse(String value) {
        return Long.parseLong(value);
    }
}
