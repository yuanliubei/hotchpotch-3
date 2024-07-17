package com.yuanliubei.hotchpotch.mapstruct.model;

import com.yuanliubei.hotchpotch.mapstruct.config.MapStructConfig;
import com.yuanliubei.hotchpotch.model.domain.Shop;
import com.yuanliubei.hotchpotch.model.dto.ShopCreateDTO;
import com.yuanliubei.hotchpotch.model.dto.ShopUpdateDTO;
import com.yuanliubei.hotchpotch.model.vo.ShopVO;
import org.mapstruct.Mapper;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Mapper(config = MapStructConfig.class)
public interface ShopConveter {

    Shop dto2Entity(ShopCreateDTO dto);

    Shop updateDTO2Entity(ShopUpdateDTO dto);

    ShopVO entity2VO(Shop shop);
}
