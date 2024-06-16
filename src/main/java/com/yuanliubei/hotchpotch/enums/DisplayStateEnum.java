package com.yuanliubei.hotchpotch.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanlb
 * @since 2023/4/5
 */
@Getter
@AllArgsConstructor
public enum DisplayStateEnum implements IntValueEnum {

    NOT_DISPLAY(0,  "不展示"),

    DISPLAY(1,  "展示"),

    ;

    private final int intValue;

    private final String title;
}
