package com.yuanliubei.hotchpotch.mapstruct;

import com.google.common.base.Splitter;
import com.yuanliubei.hotchpotch.enums.IntValueEnum;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
public abstract class AbsIntValueEnumString2ListTypeMapper <T extends Enum<? extends IntValueEnum> & IntValueEnum>{

    public List<T> map(String values) {
        Iterable<String> splitResult = Splitter.on(",")
                .omitEmptyStrings()
                .trimResults()
                .split(values);
        if (splitResult == null || !splitResult.iterator().hasNext()) {
            return Collections.emptyList();
        }
        List<Long> enumValList = StreamSupport.stream(splitResult.spliterator(), false)
                .map(Long::parseLong)
                .collect(Collectors.toList());
        return IntValueEnum.fromLongList(enumValList, getIntValueEnumClass());
    }

    protected abstract Class<T> getIntValueEnumClass();
}

