package com.tokenview.api.service.wallet;

import com.alibaba.fastjson.JSONObject;

public interface WalletService {

    //发送ETH/BTC交易到对应区块链
    JSONObject sendRawTransaction(String currency, JSONObject jo);

    //获取ACCOUNT(类ETH/TRX)指定地址信息
    JSONObject getACCOUNTAddressNonce(String currency, JSONObject jo);

    //获取TRX签名前内容
    JSONObject getTRXCreateTransaction(JSONObject jo);

}
