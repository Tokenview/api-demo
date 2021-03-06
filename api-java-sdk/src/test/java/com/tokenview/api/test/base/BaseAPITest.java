package com.tokenview.api.test.base;

import com.alibaba.fastjson.JSONObject;
import com.tokenview.api.service.base.BaseService;
import com.tokenview.api.service.base.Impl.BaseServiceImpl;
import org.junit.Before;
import org.junit.Test;

/**
 * TokenView API Test
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2021/1/20 11:13
 */
public class BaseAPITest extends BaseConfig{

    private BaseService baseService;

    @Before
    public void before() {
        config = config();
        baseService = new BaseServiceImpl(config);
    }

    /**
     * 查看交易详情，支持类BTC，类ETH，TRX等tokenview支持的所有币的交易查询
     * GET /tx/{currency}/{txid}
     */
    @Test
    public void testGetTransaction(){
        JSONObject result = baseService.getTransaction("btc","6680c65d3b1f4ab61b7096441a257de51f8bab48d0dca2f76080891ad9b0796a");
        toResultString("GetTransaction", result);
    }

    /**
     * 查看UTXO(类BTC)指定地址余额
     * GET /address/{currency}/{address}/{page}/{size}
     */
    @Test
    public void testGetUTXOAddressBalance(){
        JSONObject result = baseService.getUTXOAddressBalance("btc","1AqSbAsXh3zxE8Z61afpU65u4pxB9BBRcz");
        toResultString("GetUTXOAddressBalance", result);
    }

    /**
     * 查看UTXO(类BTC)指定地址交易列表
     * GET /address/{currency}/{address}/{page}/{size}
     */
    @Test
    public void testGetUTXOTransactionList(){
        JSONObject result = baseService.getUTXOTransactionList("btc","1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ","1","50");
        toResultString("GetUTXOTransactionList", result);
    }

    /**
     * 查看ACCOUNT(类ETH/TRX)指定地址余额
     * GET /address/{currency}/{address}/{page}/{size}
     */
    @Test
    public void testGetACCOUNTAddressBalance(){
        JSONObject result = baseService.getACCOUNTAddressBalance("eth","0xda9cacf6c13450bea275c33e83503fb705d27bbb");
        toResultString("GetACCOUNTAddressBalance", result);
    }

    /**
     * 查看获取ACCOUNT(类ETH/TRX)指定地址交易列表
     * GET /{currency}/address/normal/{address}/{page}/{size}
     */
    @Test
    public void testGetACCOUNTTransactionList(){
        JSONObject result = baseService.getACCOUNTTransactionList("eth","0x58b6a8a3302369daec383334672404ee733ab239","1","50");
        toResultString("GetACCOUNTTransactionList", result);
    }

    /**
     * 查看ACCOUNT(ETH，ETC，NAS，NEO)指定地址详细信息
     * GET /{currency}/address/{address}
     */
    @Test
    public void getACCOUNTAddressInfo(){
        JSONObject result = baseService.getACCOUNTAddressInfo("eth","0xda9cacf6c13450bea275c33e83503fb705d27bbb");
        toResultString("GetACCOUNTAddressInfo", result);
    }

    /**
     * 获取账户类型(类eth)地址信息(nonce),目前支持ETH，ETC，NAS，NEO，其他币暂时不支持
     *
     */
    @Test
    public void getACCOUNTAddressNonce(){
        String nonce = baseService.getACCOUNTAddressInfo("eth","0xda9cacf6c13450bea275c33e83503fb705d27bbb")
                .getJSONObject("data")
                .getString("nonce");
        toResultString("GetACCOUNTAddressNonce", nonce);
    }


}
