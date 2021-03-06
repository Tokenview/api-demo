package com.tokenview.api.client;

import com.alibaba.fastjson.JSONObject;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.constant.APIConstants;
import com.tokenview.api.exception.APIException;
import okhttp3.*;

import java.io.IOException;
import java.util.Date;

public class ApiHttp {
    private OkHttpClient client;
    private APIConfiguration config;

    static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public ApiHttp(APIConfiguration config, OkHttpClient client) {
        this.config = config;
        this.client = client;
    }

    private void printResponse(int status, String message, String body, boolean responseIsNotNull) {
        StringBuilder responseInfo = new StringBuilder();
        responseInfo.append("\n\tResponse").append("(").append(new Date()).append("):");
        if (responseIsNotNull) {
            responseInfo.append("\n\t\t").append("Status: ").append(status);
            responseInfo.append("\n\t\t").append("Message: ").append(message);
            responseInfo.append("\n\t\t").append("Response Body: ").append(body);
        } else {
            responseInfo.append("\n\t\t").append("\n\tRequest Error: response is null");
        }
    }

    public String url(String url) {
        return new StringBuilder(this.config.getEndpoint()).append(url).toString();
    }
}
