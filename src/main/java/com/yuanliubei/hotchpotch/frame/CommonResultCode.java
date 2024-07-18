package com.yuanliubei.hotchpotch.frame;

/**
 * @author yuanlb
 * @since 2022/10/25
 */
public enum CommonResultCode implements ResultSource {

    SUCCESS(0, "操作成功"),

    NOT_FIND(404, "未找到资源"),

    ERROR_DEFAULT(10000, "未知错误"),

    ERROR_PARAM_ERROR(10101, "参数错误"),

    ERROR_BUSINESS(10300, "业务错误"),

    CONTAIN_SENSITIVE_WORD(10400, "包含铭感词,请您核对内容信息!"),


    TOKEN_NOT_FIND(11000, "未获取到token"),

    TOKEN_INVALID(11001, "token无效"),


    CREDIT_INSUFFICIENT(12001, "余额不足"),

    NOT_TRAILED(12002, "未申请试用"),


    ;

    private final int code;

    private final String message;

    CommonResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }



    public ResultException exception() {
        return new ResultException(this);
    }

    public ResultException exception(String message) {
        return new ResultException(this, message);
    }
}
