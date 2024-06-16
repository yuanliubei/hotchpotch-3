package com.yuanliubei.hotchpotch.model.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.yuanliubei.hotchpotch.common.BaseQuery;
import com.yuanliubei.hotchpotch.common.QuerySortField;
import com.yuanliubei.hotchpotch.enums.ShopStatusEnum;
import com.yuanliubei.hotchpotch.model.domain.QShop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ShopQuery extends BaseQuery<ShopQuery.SortFieldEnum> {

    private String nameLike;

    private ShopStatusEnum status;

    private String phone;


    @Override
    public Predicate buildExample() {
        QShop Q = QShop.shop;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (StringUtils.isNotBlank(nameLike)) {
            booleanBuilder.and(Q.name.like("%" + nameLike + "%"));
        }
        if (Objects.nonNull(status)) {
            booleanBuilder.and(Q.status.eq(status.getIntValue()));
        }
        if (StringUtils.isNotBlank(phone)) {
            booleanBuilder.and(Q.phone.eq(phone));
        }
        return booleanBuilder;
    }


    @Getter
    @AllArgsConstructor
    public enum SortFieldEnum implements QuerySortField{

        ID("id"),

        ;

        private final String fieldName;
    }
}
