package com.yuanliubei.hotchpotch.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuanliubei.hotchpotch.common.PageResult;
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
        IPage<Shop> pageResult = shopMapper.selectPage(query.toPageRequest(), query.buildExample());
        if (CollectionUtils.isEmpty(pageResult.getRecords())) {
            return PageResult.empty(query.getPageInfo().getPageSize());
        }
        List<ShopVO> vos = pageResult.getRecords()
                .stream()
                .map(shopConveter::entity2VO)
                .collect(Collectors.toList());
        return new PageResult<ShopVO>(
                vos,
                pageResult.getTotal(),
                query.getPageInfo().getPageNumber(),
                query.getPageInfo().getPageSize()
        );
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
}
