package com.yuanliubei.hotchpotch.frame;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * @author yuanlb
 * @since 2022/10/25
 */
@Data
public class Result<T> {

    private int code;

    private String message;

    private T data;

    private LocalDateTime dateTime = LocalDateTime.now();

    private String exception;

    private String exception_msg;


    public static Result<Boolean> operateSuccess(){
        return Result.ok(Boolean.TRUE);
    }

    public static <T> Result<T> error(String message) {
        return from(CommonResultCode.ERROR_DEFAULT.getCode(), message);
    }

    public static <T> Result<T> error() {
        return from(CommonResultCode.ERROR_DEFAULT);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = from(CommonResultCode.SUCCESS);
        return result.data(data);
    }

    public static <T> Result<T> ok() {
        return from(CommonResultCode.SUCCESS);
    }

    public static Result<Void> from(ResultException e) {
        if (StringUtils.isNotBlank(e.getMessage())) {
            return from(e.getResultSource().getCode(), e.getMessage());
        } else {
            return from(e.getResultSource());
        }
    }

    public static <T> Result<T> from(ResultSource resultSource) {
        return from(resultSource.getCode(), resultSource.getMessage());
    }

    public static <T> Result<T> from(int code, String message) {
        Result<T> r = new Result<>();
        r.code = code;
        r.message = message;
        return r;
    }


    public Result<T> code(int code) {
        this.code = code;
        return this;
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    public Result<T> exception(Throwable e) {
        if (StringUtils.isNotEmpty(e.getLocalizedMessage())) {
            this.exception_msg = e.getLocalizedMessage();
        }
        this.exception = e.getClass().getName();
        return this;
    }

    public boolean isOK() {
        return code == CommonResultCode.SUCCESS.getCode();
    }
}
