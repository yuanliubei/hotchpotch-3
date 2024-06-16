package com.yuanliubei.hotchpotch.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanlb
 * @since 2024/6/9
 */
@Getter
@AllArgsConstructor
public enum ShopStatusEnum implements IntValueEnum{

    PREPARATION(10, "筹备"),

    BUSINESS(20, "营业"),

    CLOSE(30, "闭店"),

    ;

    private final int intValue;

    private final String title;


}
