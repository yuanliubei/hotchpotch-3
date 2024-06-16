package com.yuanliubei.hotchpotch.mapstruct;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
public abstract class AbsIdStringListTypeMapper<T> {

    public List<T> map(String value) {
        if (StringUtils.isBlank(value)) {
            return Collections.emptyList();
        }else {
            Iterable<String> splitResult = Splitter.on(",")
                    .omitEmptyStrings()
                    .trimResults()
                    .split(value);
            if (!splitResult.iterator().hasNext()) {
                return Collections.emptyList();
            }else {
                return StreamSupport.stream(splitResult.spliterator(), false)
                        .map(this::parse)
                        .collect(Collectors.toList());
            }
        }
    }

    protected abstract T parse(String value);
}
