package com.tokenview.api.test.wallet;

import com.alibaba.fastjson.JSON;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.enums.I18nEnum;

public class WalletConfig {

    public APIConfiguration config;

    public APIConfiguration config() {
        APIConfiguration config = new APIConfiguration();
        // apiKey，api注册成功后页面上有
        config.setEndpoint("https://wallet.tokenview.com/");
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
