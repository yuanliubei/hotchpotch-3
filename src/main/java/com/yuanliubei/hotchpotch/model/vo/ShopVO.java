package com.yuanliubei.hotchpotch.model.vo;

import com.yuanliubei.hotchpotch.enums.ShopStatusEnum;
import com.yuanliubei.hotchpotch.model.dto.ShopAddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Tag(name = "新增店铺VO")
@Data
public class ShopVO {

    @Schema(title = "id")
    private Long id;

    @Schema(title = "名称vo")
    private String name;

    @Schema(title = "地址")
    private ShopAddressDTO address;

    @Schema(title = "状态")
    private ShopStatusEnum status;

    @Schema(title = "电话")
    private String phone;
}
