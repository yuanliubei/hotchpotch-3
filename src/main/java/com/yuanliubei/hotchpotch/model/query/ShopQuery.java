package com.yuanliubei.hotchpotch.model.query;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yuanliubei.hotchpotch.common.BasePageSortQuery;
import com.yuanliubei.hotchpotch.enums.ShopStatusEnum;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
public class ShopQuery extends BasePageSortQuery<Shop> {

    private String nameLike;

    private ShopStatusEnum status;

    private String phone;

    @Override
    public LambdaQueryWrapper<Shop> buildExample() {
        LambdaQueryWrapper<Shop> lmq = Wrappers.lambdaQuery(Shop.class);
        if (StringUtils.isNotBlank(nameLike)) {
                lmq.like(Shop::getName, "%" + nameLike + "%");
        }
        if (Objects.nonNull(status)) {
            lmq.eq(Shop::getStatus, status.getIntValue());
        }
        if (StringUtils.isNotBlank(phone)) {
            lmq.eq(Shop::getPhone, phone);
        }
        return lmq;
    }
}
