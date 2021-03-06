package com.tokenview.api.enums;

/**
 * I18nEnum
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2020/1/18 13:00
 */
public enum I18nEnum {
    ENGLISH("en_US"),
    SIMPLIFIED_CHINESE("zh_CN"),
    TRADITIONAL_CHINESE("zh_HK"),;

    private String i18n;

    I18nEnum(String i18n) {
        this.i18n = i18n;
    }

    public String i18n() {
        return i18n;
    }
}
