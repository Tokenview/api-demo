package com.tokenview.api.test.base;

import com.alibaba.fastjson.JSON;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.enums.I18nEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseConfig {
    public APIConfiguration config;

    public APIConfiguration config() {
        APIConfiguration config = new APIConfiguration();

        config.setEndpoint("http://www.tokenview.com:8088/");
        config.setApiKey("ab2ac254-b5b3-4b13-bdea-e5d9a12319d7");

        config.setPrint(true);
        config.setI18n(I18nEnum.ENGLISH);

        return config;
    }

    public void toResultString(String flag, Object object) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(flag).append(":\n").append(JSON.toJSONString(object));
        System.out.println(stringBuilder);
    }
}
