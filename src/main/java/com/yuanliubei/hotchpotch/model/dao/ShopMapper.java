package com.yuanliubei.hotchpotch.model.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import com.yuanliubei.hotchpotch.model.query.ShopQuery;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yuanlb
 * @since 2024/7/17
 */
@Mapper
public interface ShopMapper extends BaseMapperX<Shop, ShopQuery> {

}
