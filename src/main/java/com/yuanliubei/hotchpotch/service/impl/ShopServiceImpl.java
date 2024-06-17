package com.yuanliubei.hotchpotch.service.impl;

import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.mapstruct.model.ShopMapper;
import com.yuanliubei.hotchpotch.model.dao.ShopRepository;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import com.yuanliubei.hotchpotch.model.dto.ShopCreateDTO;
import com.yuanliubei.hotchpotch.model.dto.ShopUpdateDTO;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import com.yuanliubei.hotchpotch.model.vo.ShopVO;
import com.yuanliubei.hotchpotch.service.IShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
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

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;

    @Override
    public void create(ShopCreateDTO dto) {
        Shop shop = shopMapper.dto2Entity(dto);
        shopRepository.saveAndFlush(shop);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ShopUpdateDTO dto) {
//        Shop source = shopRepository.findById(dto.getId()).get();
//        source.setName(dto.getName());
//        shopRepository.saveAndFlush(source);
        Shop shop = shopMapper.updateDTO2Entity(dto);
        shopRepository.dynamicUpdate(shop);
    }

    @Override
    public PageResult<ShopVO> query(ShopQuery query) {
        Page<Shop> page = shopRepository.findAll(query.buildExample(), query.toPageRequest());
        PageResult<Shop> pageResult = PageResult.build(page);
        if (pageResult.getTotalCount() == 0) {
            return PageResult.empty(pageResult.getPageSize());
        }
        List<ShopVO> vos = pageResult.getListData()
                .stream()
                .map(shopMapper::entity2VO)
                .collect(Collectors.toList());
        return new PageResult<ShopVO>(vos, pageResult.getTotalCount(), pageResult.getCurrentPage(), pageResult.getPageSize());
    }

    @Override
    public Boolean logicDel(Long id) {
        return shopRepository.logicDel(id) > 0;
    }

    @Override
    public void deleteByName(String name) {
        shopRepository.logicDelByName(name);
    }
}
