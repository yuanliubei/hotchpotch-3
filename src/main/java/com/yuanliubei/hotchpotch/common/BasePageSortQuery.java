package com.yuanliubei.hotchpotch.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/7/18
 */
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class BasePageSortQuery <T extends BaseEntity> extends BasePageQuery<T>{

    protected List<String> sortFields = Lists.newArrayList();

    protected List<QuerySortOrderEnum> sortOrders = Lists.newArrayList();

    @Override
    public IPage<T> toPageRequest() {
        Page<T> page = (Page)super.toPageRequest();
        if (!Objects.equals(sortFields.size(), sortOrders.size())) {
            throw new IllegalArgumentException("sortFields and sortOrders not match");
        }
        //默认 id 倒序
        if (CollectionUtils.isEmpty(sortFields)) {
            page.addOrder(OrderItem.desc("id"));
            return page;
        }
        List<OrderItem> orderItems = Lists.newArrayListWithCapacity(sortFields.size());
        for (int idx = 0; idx < sortFields.size(); idx++) {
            String propertiesName = sortFields.get(idx);
            String column = StringUtils.camelToUnderline(propertiesName);
            QuerySortOrderEnum sortOrderEnum = sortOrders.get(idx);

            OrderItem orderItem = QuerySortOrderEnum.ASC == sortOrderEnum ? OrderItem.asc(column) : OrderItem.desc(column);
            orderItems.add(orderItem);
        }
        page.setOrders(orderItems);
        return page;
    }

    public BasePageSortQuery<T> setSort(String field, QuerySortOrderEnum order) {
        this.sortFields = Lists.newArrayList(field);
        this.sortOrders = Lists.newArrayList(order);
        return this;
    }

    public BasePageSortQuery<T> setSort(String field) {
        return setSort(field, QuerySortOrderEnum.DESC);
    }

    public BasePageSortQuery<T> addSort(String field, QuerySortOrderEnum order) {
        sortFields.add(field);
        sortOrders.add(order);
        return this;
    }

    public BasePageSortQuery<T> addSort(String field) {
        addSort(field, QuerySortOrderEnum.DESC);
        return this;
    }


    public enum QuerySortOrderEnum {

        ASC,

        DESC,

        ;
    }
}
