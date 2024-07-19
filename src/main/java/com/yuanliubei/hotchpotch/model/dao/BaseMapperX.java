package com.yuanliubei.hotchpotch.model.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.toolkit.Db;
import com.yuanliubei.hotchpotch.common.BasePageQuery;
import com.yuanliubei.hotchpotch.common.BaseQuery;
import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.frame.CommonResultCode;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yuanlb
 * @since 2024/7/18
 */
public interface BaseMapperX<T extends BaseEntity, Q extends BaseQuery<T>> extends BaseMapper<T> {

    default <PQ extends BasePageQuery<T>> PageResult<T> pageQuery(PQ q) {
        IPage<T> page = selectPage(q.toPageRequest(), q.buildExample());
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return PageResult.empty(q.getPageInfo().getPageSize());
        }
        return new PageResult<>(
                page.getRecords(),
                page.getTotal(),
                q.getPageInfo().getPageNumber(),
                q.getPageInfo().getPageSize()
        );
    }

    default List<T> query(Q q) {
        return selectList(q.buildExample());
    }

    default <K> Map<K,T> query(Q q, Function<T,K> function) {
        List<T> list = query(q);
        return list.stream()
                .collect(Collectors.toMap(function, Function.identity()));
    }

    default <PQ extends BasePageQuery<T>> List<T> queryWithSort(PQ q) {
        BasePageQuery<T> pageQuery = q.withoutPage();
        return selectList(pageQuery.toPageRequest(), q.buildExample());
    }


    default Long count(Q q) {
        return selectCount(q.buildExample());
    }

    default T getOneSilence(Wrapper<T> wrapper) {
        return selectList(wrapper).get(0);
    }

    default T getOneSilence(Q q){
        return getOneSilence(q.buildExample());
    }

    default T getOneThrow(Wrapper<T> wrapper){
        List<T> list = selectList(wrapper);
        if (list.size() > 1){
            throw CommonResultCode.ERROR_BUSINESS.exception("数据异常");
        }
        return list.get(0);
    }

    default T getOneThrow(Q q) {
        return getOneThrow(q.buildExample());
    }

    default Boolean insertBatch(Collection<T> entities) {
        return Db.saveBatch(entities);
    }

    default Boolean insertBatch(Collection<T> entities, int size) {
        return Db.saveBatch(entities, size);
    }

    default Boolean updateBatch(Collection<T> entities) {
        return Db.updateBatchById(entities);
    }

    default Boolean updateBatch(Collection<T> entities, int size) {
        return Db.updateBatchById(entities, size);
    }
}
