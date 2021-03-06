package com.tokenview.api.service.base.Impl;

import com.alibaba.fastjson.JSONObject;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BaseAPI {

    @GET("/tx/{currency}/{txid}")
    Call<JSONObject> getTransaction(@Path("currency") String currency, @Path("txid") String txid);

    @GET("/address/{currency}/{address}/{page}/{size}")
    Call<JSONObject> getTransactionList(@Path("currency") String currency, @Path("address") String address,
                                            @Path("page") String page, @Path("size") String size);

    @GET("/{currency}/address/normal/{address}/{page}/{size}")
    Call<JSONObject> getACCOUNTTransactionList(@Path("currency") String currency, @Path("address") String address,
                                               @Path("page") String page, @Path("size") String size);

    @GET("/address/{currency}/{address}/1/1")
    Call<JSONObject> getAddressBalance(@Path("currency") String currency, @Path("address") String address);

    @GET("/addr/b/{currency}/{address}")
    Call<JSONObject> getACCOUNTAddressBalance(@Path("currency") String currency, @Path("address") String address);

    @GET("/{currency}/address/{address}")
    Call<JSONObject> getACCOUNTAddressInfo(@Path("currency") String currency, @Path("address") String address);
}
