package com.yuanliubei.hotchpotch.model.query;

import com.yuanliubei.hotchpotch.common.BaseQuery;
import com.yuanliubei.hotchpotch.common.QuerySortField;
import com.yuanliubei.hotchpotch.enums.ShopStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;


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
    public Object buildExample() {
        return null;
    }


    @Getter
    @AllArgsConstructor
    public enum SortFieldEnum implements QuerySortField{

        ID("id"),

        ;

        private final String fieldName;
    }
}
