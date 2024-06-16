package com.yuanliubei.hotchpotch.frame;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

/**
 * 忽略请求中的空格
 * @author yuanlb
 * @since 2024/5/24
 */
@ControllerAdvice
public class ControllerDataBinder {

    public ControllerDataBinder() {
    }

    @InitBinder
    public void customizeBinding(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null) {
                    this.setValue((Object)null);
                } else {
                    this.setValue(StringUtils.trim(text));
                }

            }
        });
    }
}
