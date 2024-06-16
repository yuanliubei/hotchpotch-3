package com.yuanliubei.hotchpotch.mapstruct.config;

import com.yuanliubei.hotchpotch.mapstruct.*;
import com.yuanliubei.hotchpotch.mapstruct.biz.ShopAddressMapper;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@MapperConfig(
        componentModel = "spring",
        uses =  {
                //基础、通用的转换器
                StringListStringTypeMapper.class,
                IntegerListStringTypeMapper.class,
                LongListStringTypeMapper.class,
                IntValueEnumTypeMapper.class,
                IntValueEnumList2StringTypeMapper.class,
                ObjectNodeTypeMapper.class,

                //自定义的业务转换器
                ShopAddressMapper.class
        },

        //没有匹配的属性跳过
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MapStructConfig {
}
