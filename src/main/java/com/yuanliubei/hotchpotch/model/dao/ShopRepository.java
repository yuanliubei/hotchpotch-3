package com.yuanliubei.hotchpotch.model.dao;

import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yuanliubei.hotchpotch.model.domain.QShop;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Function;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Repository
public interface ShopRepository extends BaseRepository<Shop, Long, ShopQuery> {

    QShop Q = QShop.shop;

    @Override
    default EntityPathBase<Shop> getEntityPath() {
        return Q;
    }

    @Transactional(rollbackFor = Throwable.class)
    default long logicDelByName(String name) {
        Function<JPAQueryFactory, Long> fun = (qf) -> qf.update(Q)
                .set(Q.deleted, true)
                .where(Q.name.eq(name))
                .execute();
        return apply(fun);
    }
}
