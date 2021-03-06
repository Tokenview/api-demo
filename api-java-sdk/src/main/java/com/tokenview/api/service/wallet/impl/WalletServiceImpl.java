package com.tokenview.api.service.wallet.impl;

import com.alibaba.fastjson.JSONObject;
import com.tokenview.api.client.APIClient;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.service.wallet.WalletService;

public class WalletServiceImpl implements WalletService {

    private APIClient client;
    private WalletAPI api;

    public WalletServiceImpl(APIConfiguration config) {
        this.client = new APIClient(config);
        this.api = client.createService(WalletAPI.class);
    }

    @Override
    public JSONObject sendRawTransaction(String currency,JSONObject jsonObject){
        return this.client.executeSync(this.api.sendRawTransaction(currency,jsonObject));
    }

    @Override
    public JSONObject getACCOUNTAddressNonce(String currency,JSONObject jsonObject){
        return this.client.executeSync(this.api.getACCOUNTAddressNonce(currency,jsonObject));
    }

    @Override
    public JSONObject getTRXCreateTransaction(JSONObject jsonObject){
        return this.client.executeSync(this.api.getTRXCreateTransaction(jsonObject));
    }




}
