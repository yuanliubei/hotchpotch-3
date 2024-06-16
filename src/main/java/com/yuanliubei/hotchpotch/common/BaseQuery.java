package com.yuanliubei.hotchpotch.common;

import com.google.common.collect.Lists;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2023/4/9
 */
@Getter
@Setter
public abstract class BaseQuery<SortField extends QuerySortField> {

    private Long id;

    private List<Long> ids;

    protected LocalDateTime createTimeMin;
    protected LocalDateTime createTimeMax;
    protected LocalDateTime updateTimeMin;
    protected LocalDateTime updateTimeMax;

    protected List<SortField> sortFields = new ArrayList<>();

    protected List<Sort.Direction> sortOrders = new ArrayList<>();

    protected BaseQuery.PageInfo pageInfo = new BaseQuery.PageInfo();


    public abstract Predicate buildExample();

    public List<Sort.Order> buildOrders() {
        if (!Objects.equals(sortFields.size(), sortOrders.size())) {
            throw new IllegalArgumentException("sortFields and sortOrders not match");
        }
        //默认 id 倒序
        if (Objects.equals(0, sortFields.size())) {
            return Lists.newArrayList(
                    new Sort.Order(Sort.Direction.DESC, "id")
            );
        }
        List<Sort.Order> orders = Lists.newArrayListWithCapacity(sortFields.size());
        for (int idx = 0; idx < sortFields.size(); idx++) {
            Sort.Order order = new Sort.Order(
                    sortOrders.get(idx),
                    sortFields.get(idx).getFieldName()
            );
            orders.add(order);
        }
        return orders;
    }

    public <T> JPAQuery<T> withSort(JPAQuery<T> jpaQuery) {
        for (int idx = 0; idx < sortFields.size(); idx++) {
            SortField sortField = sortFields.get(idx);
            Sort.Direction sortType = sortOrders.get(idx);
            Order order = Order.valueOf(sortType.name());

            Path<Object> fieldPath = Expressions.path(Object.class, sortField.getEntityPath(), sortField.getFieldName());
            jpaQuery.orderBy(new OrderSpecifier(order, fieldPath));
        }
        return jpaQuery;
    }

    public void setSort(SortField field, Sort.Direction order) {
        this.sortFields = Lists.newArrayList(field);
        this.sortOrders = Lists.newArrayList(order);
    }

    public void setSort(SortField field) {
        setSort(field, Sort.Direction.DESC);
    }

    public void addSort(SortField field, Sort.Direction order) {
        sortFields.add(field);
        sortOrders.add(order);
    }

    public void addSort(SortField field) {
        addSort(field, Sort.Direction.DESC);
    }

    public PageRequest toPageRequest() {
        return PageRequest.of(
                pageInfo.getPageNumber() - 1,
                pageInfo.getPageSize(),
                Sort.by(buildOrders())
        );
    }


    @Getter
    @Setter
    public static class PageInfo {

        private Integer pageNumber;

        private Integer pageSize;

        public PageInfo() {
            this.pageNumber = 1;
            this.pageSize = 10;
        }

        public PageInfo(Integer pageNumber, Integer pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public Integer getStart() {
            return (this.pageNumber - 1) * this.pageSize;
        }
    }
}
