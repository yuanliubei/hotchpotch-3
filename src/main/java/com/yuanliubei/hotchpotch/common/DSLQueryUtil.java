package com.yuanliubei.hotchpotch.common;

import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * Copyright: 中传互动 & 卓讯互动
 * Author: yuanliubei
 * Date: 2023/9/1
 */
public abstract class DSLQueryUtil<T> {

    public <Q extends BaseQuery<? extends QuerySortField>> Page<T> joinQuery(Q query) {
        Predicate predicate = query.buildExample();
        PageRequest pageRequest = query.toPageRequest();
        JPAQuery<T> jpaQuery = join();
        jpaQuery.where(predicate);
        long total = jpaQuery.fetchCount();
        if (total == 0) {
            return Page.empty();
        }
        jpaQuery.limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset());

        query.withSort(jpaQuery);

        List<T> list = jpaQuery.fetch();
        return new PageImpl<>(list, pageRequest, total);
    }


    public abstract JPAQuery<T> join();
}
