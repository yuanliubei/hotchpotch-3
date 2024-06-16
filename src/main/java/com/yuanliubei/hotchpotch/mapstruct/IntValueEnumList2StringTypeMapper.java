package com.yuanliubei.hotchpotch.mapstruct;

import com.google.common.base.Joiner;
import com.yuanliubei.hotchpotch.enums.IntValueEnum;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class IntValueEnumList2StringTypeMapper {

    public String map(List<? extends IntValueEnum> values) {
        if (Objects.isNull(values)) {
            return null;
        } else {
            return values.isEmpty() ? "" : Joiner.on(",").skipNulls().join(IntValueEnum.toLongList(values));
        }
    }
}
