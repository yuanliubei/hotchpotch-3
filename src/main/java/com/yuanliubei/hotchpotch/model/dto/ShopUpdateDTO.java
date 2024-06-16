package com.yuanliubei.hotchpotch.model.dto;

import com.yuanliubei.hotchpotch.enums.ShopStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Tag(name = "修改店铺DTO")
@Data
public class ShopUpdateDTO {

    @Schema(title = "id")
    private Long id;

    @Schema(title = "名称dto")
    private String name;

    @Schema(title = "地址")
    private ShopAddressDTO address;

    @Schema(title = "状态")
    private ShopStatusEnum status;

    @Schema(title = "电话")
    private String phone;

    @Schema(title = "是否更新")
    private Boolean deleted;
}
