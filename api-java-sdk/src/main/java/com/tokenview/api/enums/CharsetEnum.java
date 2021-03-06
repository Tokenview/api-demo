package com.tokenview.api.enums;

/**
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2020/1/18 13:00
 */
public enum CharsetEnum {

    UTF_8("UTF-8"),
    ISO_8859_1("ISO-8859-1"),;


    private String charset;

    CharsetEnum(String charset) {
        this.charset = charset;
    }

    public String charset() {
        return charset;
    }
}
