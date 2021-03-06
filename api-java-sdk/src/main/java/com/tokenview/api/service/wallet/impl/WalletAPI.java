package com.tokenview.api.service.wallet.impl;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalletAPI {

    @POST("/onchainwallet/{currency}")
    Call<JSONObject> sendRawTransaction(@Path("currency") String currency, @Body JSONObject jsonObject);

    @POST("/onchainwallet/{currency}")
    Call<JSONObject> getACCOUNTAddressNonce(@Path("currency") String currency, @Body JSONObject jsonObject);

    @POST("/onchainwallet/trx")
    Call<JSONObject> getTRXCreateTransaction(@Body JSONObject jsonObject);

}
