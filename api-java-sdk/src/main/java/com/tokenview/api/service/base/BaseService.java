package com.tokenview.api.service.base;

import com.alibaba.fastjson.JSONObject;

public interface BaseService {

    //获取指定交易详情
    JSONObject getTransaction(String currency, String txid);

    //获取UTXO(类BTC)指定地址交易列表
    JSONObject getUTXOTransactionList(String currency, String address, String page, String size);

    //获取ACCOUNT(类ETH/TRX)指定地址交易列表
    JSONObject getACCOUNTTransactionList(String currency, String address, String page, String size);

    //获取UTXO(类BTC)指定地址余额
    JSONObject getUTXOAddressBalance(String currency, String address);

    //获取ACCOUNT(类ETH/TRX)指定地址余额
    JSONObject getACCOUNTAddressBalance(String currency, String address);

    //T获取ACCOUNT(ETH，ETC，NAS，NEO)指定地址信息
    JSONObject getACCOUNTAddressInfo(String currency, String address);

}
