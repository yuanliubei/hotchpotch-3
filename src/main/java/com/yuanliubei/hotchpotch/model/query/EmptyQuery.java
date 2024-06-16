package com.yuanliubei.hotchpotch.model.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.yuanliubei.hotchpotch.common.BaseQuery;
import com.yuanliubei.hotchpotch.common.QuerySortField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmptyQuery extends BaseQuery<EmptyQuery.SortFieldEnum> {

    @Override
    public Predicate buildExample() {
        return new BooleanBuilder();
    }

    @Getter
    @AllArgsConstructor
    public enum SortFieldEnum implements QuerySortField {

        ID("id"),

        ;

        private final String fieldName;
    }
}
