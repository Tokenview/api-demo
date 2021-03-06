package com.tokenview.api.service.base.Impl;

import com.alibaba.fastjson.JSONObject;
import com.tokenview.api.client.APIClient;
import com.tokenview.api.config.APIConfiguration;
import com.tokenview.api.service.base.BaseService;

public class BaseServiceImpl implements BaseService {

    private APIClient client;
    private BaseAPI api;

    public BaseServiceImpl(APIConfiguration config) {
        this.client = new APIClient(config);
        this.api = client.createService(BaseAPI.class);
    }

    @Override
    public JSONObject getTransaction(String currency, String txid){
        return this.client.executeSync(this.api.getTransaction(currency,txid));
    }

    @Override
    public JSONObject getUTXOTransactionList(String currency,String address,String page,String size){
        return this.client.executeSync(this.api.getTransactionList(currency,address,page,size));
    }

    @Override
    public JSONObject getACCOUNTTransactionList(String currency,String address,String page,String size){
        return this.client.executeSync(this.api.getACCOUNTTransactionList(currency,address,page,size));
    }

    @Override
    public JSONObject getUTXOAddressBalance(String currency,String address){
        return this.client.executeSync(this.api.getAddressBalance(currency,address));
    }

    @Override
    public JSONObject getACCOUNTAddressBalance(String currency,String address){
        return this.client.executeSync(this.api.getACCOUNTAddressBalance(currency,address));
    }

    @Override
    public JSONObject getACCOUNTAddressInfo(String currency,String address){
        return this.client.executeSync(this.api.getACCOUNTAddressInfo(currency,address));
    }
}
