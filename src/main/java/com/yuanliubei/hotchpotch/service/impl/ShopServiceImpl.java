package com.yuanliubei.hotchpotch.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.lock.RedisLockRequire;
import com.yuanliubei.hotchpotch.mapstruct.model.ShopConveter;
import com.yuanliubei.hotchpotch.model.dao.ShopMapper;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import com.yuanliubei.hotchpotch.model.dto.ShopCreateDTO;
import com.yuanliubei.hotchpotch.model.dto.ShopUpdateDTO;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import com.yuanliubei.hotchpotch.model.vo.ShopVO;
import com.yuanliubei.hotchpotch.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements IShopService {

    private final ShopMapper shopMapper;
    private final ShopConveter shopConveter;

    @Override
    public void create(ShopCreateDTO dto) {
        Shop shop = shopConveter.dto2Entity(dto);
        shopMapper.insert(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ShopUpdateDTO dto) {
        Shop shop = shopConveter.updateDTO2Entity(dto);
        shopMapper.updateById(shop);
    }

    @Override
    public PageResult<ShopVO> query(ShopQuery query) {
        PageResult<Shop> pageResult = shopMapper.pageQuery(query);
        return shopConveter.entity2VO(pageResult);
    }

    @Override
    public Boolean logicDel(Long id) {
        return shopMapper.deleteById(id) > 0;
    }

    @Override
    public void deleteByName(String name) {
        Wrapper<Shop> wrapper = Wrappers.lambdaQuery(Shop.class)
                .eq(Shop::getName, name);
        shopMapper.delete(wrapper);
    }

    @Override
    @RedisLockRequire(
            keyType = RedisLockRequire.KeyType.EL,
            key = "'shop_amount_reduct_' + #shopId",
            waitTimeInMillis = 2000,
            lockExpireTimeInMillis = 5000,
            lockFailMessage = "金额扣减异常，请稍后重试"
    )
    public Shop reduce(Long shopId) {
        Shop shop = shopMapper.selectById(shopId);
        shop.setAmount(shop.getAmount() - 1);
        shopMapper.updateById(shop);
        try {
            Thread.sleep(1000 * 10L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return shop;
    }
}
