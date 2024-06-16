package com.yuanliubei.hotchpotch.mapstruct.biz;

import com.yuanliubei.hotchpotch.mapstruct.AbsJacksonTypeMapper;
import com.yuanliubei.hotchpotch.model.dto.ShopAddressDTO;
import org.springframework.stereotype.Component;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Component
public class ShopAddressMapper extends AbsJacksonTypeMapper<ShopAddressDTO> {

    public ShopAddressMapper() {
        super(ShopAddressDTO.class);
    }
}
