package com.tokenview.api.enums;

/**
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2020/1/18 13:00
 */
public enum ContentTypeEnum {

    APPLICATION_JSON("application/json"),
    APPLICATION_JSON_UTF8("application/json; charset=UTF-8"),
    // The server does not support types
    APPLICATION_FORM("application/x-www-form-urlencoded; charset=UTF-8"),;


    private String contentType;

    ContentTypeEnum(String contentType) {
        this.contentType = contentType;
    }

    public String contentType() {
        return contentType;
    }
}
