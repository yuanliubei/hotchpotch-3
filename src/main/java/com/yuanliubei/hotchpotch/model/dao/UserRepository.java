package com.yuanliubei.hotchpotch.model.dao;

import com.google.common.collect.Lists;
import com.yuanliubei.hotchpotch.model.domain.QUserV2;
import com.yuanliubei.hotchpotch.model.domain.UserV2;
import com.yuanliubei.hotchpotch.model.query.EmptyQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author yuanlb
 * @since 2024/5/21
 */
@Repository
public interface UserRepository extends BaseRepository<UserV2, Long, EmptyQuery> {

    QUserV2 Q = QUserV2.userV2;

    default UserV2 findByName(String name) {
        return findOne(Q.name.eq(name))
                .orElse(null);
    }

    default List<UserV2> listByAge(Integer age) {
        return Lists.newArrayList(findAll(Q.age.eq(age)));
    }

    @Transactional(rollbackFor = Exception.class)
    default void update(Long id, Integer age) {
        getJPAQueryFactory()
                .update(Q)
                .set(Q.age, age)
                .where(Q.id.eq(id))
                .execute();
    }

    @Transactional(readOnly = true)
    default UserV2 queryByName(String name) {
        return apply(q -> q.select(Q).from(Q).where(Q.name.eq(name)).fetchFirst());
    }
}
