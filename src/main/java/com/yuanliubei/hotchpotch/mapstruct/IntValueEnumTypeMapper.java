package com.yuanliubei.hotchpotch.mapstruct;

import com.yuanliubei.hotchpotch.enums.IntValueEnum;
import org.mapstruct.TargetType;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class IntValueEnumTypeMapper {

    public Integer map(IntValueEnum value) {
        return Objects.isNull(value) ? null : value.intValue();
    }

    public <T extends Enum<? extends IntValueEnum> & IntValueEnum> T map(Integer value, @TargetType Class<T> targetClass) {
        return Objects.isNull(value) ? null : IntValueEnum.valueOf(value, targetClass);
    }
}
