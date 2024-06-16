package com.yuanliubei.hotchpotch.enums;

//import com.yuanliubei.hotchpotch.config.JpaEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanlb
 * @since 2023/4/5
 */
@Getter
@AllArgsConstructor
public enum DeleteStateEnum implements IntValueEnum {


    NOT_DELETE(0, "未删除"),

    DELETED(1, "已删除"),

    ;

    private final int intValue;

    private final String title;
}
