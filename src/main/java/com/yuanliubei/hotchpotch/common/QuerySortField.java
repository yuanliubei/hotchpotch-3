package com.yuanliubei.hotchpotch.common;

import com.querydsl.core.types.dsl.EntityPathBase;

/**
 * @author yuanlb
 * @since 2023/4/9
 */
public interface QuerySortField {

    String getFieldName();

    default EntityPathBase<?> getEntityPath() {
        return null;
    }
}
