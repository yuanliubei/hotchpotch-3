package com.yuanliubei.hotchpotch.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuanlb
 * @since 2023/4/2
 */
@Getter
@AllArgsConstructor
public enum ApiGroupEnum implements ApiGroup {

    USER("前台用户接口", PathPrefix.USER, ""),

    COMMON("通用接口", PathPrefix.COMMON, ""),

    ;

    public interface PathPrefix {
        String USER = "/api/user";
        String COMMON = "/api/common";

    }

    private final String name;

    private final String pathPrefix;

    private final String description;
}
