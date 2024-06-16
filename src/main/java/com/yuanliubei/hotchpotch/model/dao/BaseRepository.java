package com.yuanliubei.hotchpotch.model.dao;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.collect.Maps;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.yuanliubei.hotchpotch.common.BaseQuery;
import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.constant.EntityFieldConstant;
import com.yuanliubei.hotchpotch.model.domain.BaseEntity;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author yuanlb
 * @since 2024/5/23
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity, ID, Q extends BaseQuery<?>> extends JpaRepository<T, ID>,
        JpaSpecificationExecutor<T>,
        QuerydslPredicateExecutor<T> {

    /**
     * 获取DSL 生成的'Q'对象
     */
    default EntityPathBase<T> getEntityPath() {
        return null;
    }

    /**
     * 获取子类指定的第一个泛型class
     */
    @SuppressWarnings(value = "unchecked")
    default Class<T> getDOClass() {
        Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(this.getClass(), BaseRepository.class);
        return (Class<T>) classes[0];
    }


    /**
     * 获取 JPAQueryFactory
     */
    default JPAQueryFactory getJPAQueryFactory() {
        return SpringUtil.getBean(JPAQueryFactory.class);
    }


    /**
     * 执行函数方法，可读写
     *
     * @param function fun
     */
    @Transactional(rollbackFor = Throwable.class)
    default <K> K apply(@NonNull Function<JPAQueryFactory, K> function) {
        return function.apply(getJPAQueryFactory());
    }

    /**
     * 执行函数方法，只读
     *
     * @param function fun
     */
    @Transactional(readOnly = true, rollbackFor = Throwable.class)
    default <K> K applyReadOnly(@NonNull Function<JPAQueryFactory, K> function) {
        return function.apply(getJPAQueryFactory());
    }

    @Transactional(readOnly = true)
    default T queryById(Long id) {
        EntityPathBase<T> Q = getEntityPath();
        BooleanPath deletedPath = Expressions.booleanPath(Q, EntityFieldConstant.DELETED);
        NumberPath<Long> idPath = Expressions.numberPath(Long.class, Q, EntityFieldConstant.ID);

        Function<JPAQueryFactory, T> fun = qf -> qf.select(Q)
                .from(Q)
                .where(deletedPath.eq(false).and(idPath.eq(id)))
                .fetchFirst();
        return applyReadOnly(fun);
    }


    @Transactional(readOnly = true)
    default List<T> queryByIds(Collection<Long> ids) {
        EntityPathBase<T> Q = getEntityPath();
        BooleanPath deletedPath = Expressions.booleanPath(Q, EntityFieldConstant.DELETED);
        NumberPath<Long> idPath = Expressions.numberPath(Long.class, Q, EntityFieldConstant.ID);

        Function<JPAQueryFactory, List<T>> fun = qf -> qf.select(Q)
                .from(Q)
                .where(deletedPath.eq(false).and(idPath.in(ids)))
                .fetch();
        return applyReadOnly(fun);
    }

    @Transactional(readOnly = true)
    default <J extends Serializable> Map<J, T> queryWithMap(Collection<Long> ids, Function<T, J> fun) {
        List<T> list = queryByIds(ids);
        if (CollectionUtils.isEmpty(list)) {
            return Maps.newHashMapWithExpectedSize(0);
        }
        return list.stream()
                .collect(Collectors.toMap(fun, Function.identity()));
    }


    /**
     * 分页查询
     */
    @Transactional(readOnly = true)
    default PageResult<T> pageQuery(Q query) {
        BooleanBuilder booleanBuilder = (BooleanBuilder) query.buildExample();
        EntityPathBase<T> entityPath = getEntityPath();
        BooleanPath deletedPath = Expressions.booleanPath(entityPath, EntityFieldConstant.DELETED);

        booleanBuilder.and(deletedPath.eq(false));
        Page<T> page = this.findAll(booleanBuilder, query.toPageRequest());
        return PageResult.build(page);
    }

    /**
     * 逻辑删除
     *
     * @param id
     */
    @Transactional(rollbackFor = Throwable.class)
    default long logicDel(@NonNull Long id) {
        EntityPathBase<T> entityPath = getEntityPath();
        BooleanPath deletedPath = Expressions.booleanPath(entityPath, EntityFieldConstant.DELETED);
        NumberPath<Long> idPath = Expressions.numberPath(Long.class, entityPath, EntityFieldConstant.ID);
        return getJPAQueryFactory().update(entityPath)
                .set(deletedPath, true)
                .where(idPath.eq(id))
                .execute();
    }

    /**
     * 动态更新，field.value != null
     */
    @Transactional(rollbackFor = Throwable.class)
    default void dynamicUpdate(@NonNull T obj) {
        EntityPathBase<T> entityPath = getEntityPath();
        JPAUpdateClause update = getJPAQueryFactory()
                .update(getEntityPath());
        Field[] fields = ReflectUtil.getFields(getDOClass());
        for (Field field : fields) {
            String fieldName = field.getName();
            if (StringUtils.equals(EntityFieldConstant.ID, fieldName)) {
                continue;
            }
            Object value = ReflectUtil.getFieldValue(obj, field);
            if (Objects.isNull(value)) {
                continue;
            }
            update.set(Expressions.path(field.getType(), fieldName), value);
        }
        NumberPath<Long> idPath = Expressions.numberPath(Long.class, entityPath, EntityFieldConstant.ID);
        //TODO 过滤未删除
        update.set(getEntityPath(), obj)
                .where(idPath.eq(obj.getId()))
                .execute();
    }
}
