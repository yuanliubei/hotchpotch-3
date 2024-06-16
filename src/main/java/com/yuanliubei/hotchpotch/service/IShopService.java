package com.yuanliubei.hotchpotch.service;

import com.yuanliubei.hotchpotch.common.PageResult;
import com.yuanliubei.hotchpotch.model.dto.ShopCreateDTO;
import com.yuanliubei.hotchpotch.model.dto.ShopUpdateDTO;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import com.yuanliubei.hotchpotch.model.vo.ShopVO;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
public interface IShopService {

    void create(ShopCreateDTO dto);

    void update(ShopUpdateDTO dto);

    PageResult<ShopVO> query(ShopQuery query);

    Boolean logicDel(Long id);

    void deleteByName(String name);
}
