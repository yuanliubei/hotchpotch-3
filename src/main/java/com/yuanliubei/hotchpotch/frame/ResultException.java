package com.yuanliubei.hotchpotch.frame;

import lombok.Getter;

/**
 * @author yuanlb
 * @since 2022/10/25
 */
@Getter
public class ResultException extends RuntimeException{

    private final ResultSource resultSource;

    public ResultException(ResultSource resultSource) {
        super(resultSource.getMessage());
        this.resultSource = resultSource;
    }


    public ResultException(ResultSource resultSource, String message) {
        super(message);
        this.resultSource = resultSource;
    }

}
