package com.tokenview.api.client;

import com.tokenview.api.config.APIConfiguration;

/**
 * API Credentials
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2021/1/18 12:24
 */
public class APICredentials {
    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * The user's secret key provided by TokenView.
     */
    private String apiKey;

    public APICredentials(APIConfiguration config) {
        super();
        this.apiKey = config.getApiKey();
    }
}
