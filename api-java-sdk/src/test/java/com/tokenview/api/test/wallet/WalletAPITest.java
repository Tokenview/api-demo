package com.tokenview.api.test.wallet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tokenview.api.service.wallet.WalletService;
import com.tokenview.api.service.wallet.impl.WalletServiceImpl;
import com.tokenview.sign.btc.BTCSign;
import com.tokenview.sign.eth.ETHSign;
import com.tokenview.utils.BTCWalletUtil;
import com.tokenview.utils.HttpUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * TokenView API Test
 *
 * @author Moilk
 * @version 1.0.0
 * @date 2021/1/20 16:09
 */
public class WalletAPITest extends WalletConfig {

    private WalletService walletservice;

    @Before
    public void before() {
        config = config();
        walletservice = new WalletServiceImpl(config);
    }
    /**由助记词得到eckey+wif格式私钥+hex格式私钥,适用于类BTC币和类ETH币,详细见打印信息，请注意以下两点
     * 类BTC币请使用BTC_MAIN_PATH
     * 类ETH币请使用ETH_MAIN_PATH
     * 使用对的chainpath才能和目前市场主流钱包生成地址相同，否则不同！！！！
     * @Param List<String>
     */
    @Test
    public void getEckey(){
        List stringList = new ArrayList();
        stringList.add("gym");
        stringList.add("please");
        stringList.add("sauce");
        stringList.add("elephant");
        stringList.add("trap");
        stringList.add("bag");
        stringList.add("logic");
        stringList.add("okay");
        stringList.add("impulse");
        stringList.add("slogan");
        stringList.add("goose");
        stringList.add("birth");
        BTCWalletUtil.loadWalletByMnemonic("btc",stringList,"bc1q44elpv05mkdkp5qyd8fajcq2lrczpsats7zjgr","mywallet");
        BTCWalletUtil.loadWalletByMnemonic("eth",stringList,"bc1q44elpv05mkdkp5qyd8fajcq2lrczpsats7zjgr","mywallet");
    }

    /**
     * 创建账户类型(类eth)地址
     * @Param privateKey
     */
    @Test
    public void createACCOUNTAddress(){
        toResultString("CreateACCOUNTAddress", new ETHSign().getAddress("b71a7616b42110d8345ddc6826ec42c2f1ce24d5f4d8efeb616168d5c1ef4a1f"));
    }

    /**
     * 创建UTXO(类BTC)地址
     * @Param privateKey
     */
    @Test
    public void createUTXOAddress(){
        toResultString("CreateUTXOAddress",new BTCSign().getAddress("ab4ff104eddcc43430e0afe9487f21e1033fdc4eca9448065593d49fbc5e6b53"));
    }


    /**
     * 使用私钥对账户类型(类eth)地址发交易进行签名，nonce调用BaseAPI获取，gasPrice和gasLimit可使用下面固定值约 $2/笔
     * @Param privateKey
     * @Param toAddress
     * @Param nonce
     * @Param gasPrice
     * @Param gasLimit
     * @Param amount
     */
    @Test
    public void SignEthTransaction(){
        toResultString("SignEthTransaction", new ETHSign().signETHTransaction(
                "0xb71a7616b42110d8345ddc6826ec42c2f1ce24d5f4d8efeb616168d5c1ef4a1f",
                "0x9Ae75431335d2e70f8DB0b35F6C179a43756f78e",
                "1",
                78500000000L,
                21000L,
                100000000L));
    }

    /**
     * 使用私钥对UTXO类型(类BTC)地址发交易进行签名,包含获取unspent并组装交易过程,暂不支持隔离见证bech32地址
     * @Param txid
     * @Param inputAddress(可若干个)
     * @Param toAddress(可若干个)
     * @Param toAmount(可若干个)
     * @Param changeAddress
     * @Param privateKeyWif(privateKeyHex也可只要能生成Eckey,本次测试用wif格式)
     */
    @Test
    public void SignBTCTransaction(){
        //unspent所在txid和所在地址
        String txid = "6680c65d3b1f4ab61b7096441a257de51f8bab48d0dca2f76080891ad9b0796a";
        String inputAddress="1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ";
        String toAdddress = "3Kjn9rsoQPrHQXGCJjJuuqZe46JNAseN1t";
        long toAmount = 10000;
        String changeAddress="1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ";
        String privateKeyWIF = "L2xigpefbyesCnzR5hJDFxKPD4A2qSuNHiz4utWnqEovhownw8U2";

        List<String> addrList = new ArrayList<>();
        addrList.add(inputAddress);
        //可自行设置多个输出地址和金额，找零地址和金额
        JSONArray unspents =new HttpUtil().getUnspent(txid,addrList);
        long inputAmount =unspents
                .stream()
                .mapToLong(o->JSONObject.parseObject(o.toString()).getLong("value"))
                .sum();

        JSONArray outputs = new JSONArray();
        JSONObject outputTo = new JSONObject();
        outputTo.put("address",toAdddress);
        outputTo.put("amount",toAmount);
        outputs.add(outputTo);
        long outputToAmount = outputs
                .stream()
                .mapToLong(o -> JSONObject.parseObject(o.toString()).getLong("amount"))
                .sum();
        long outputChangeAmount = inputAmount - outputToAmount;
        //input金额不足输出和找零返回不签名
        if (outputChangeAmount<=0){
            return;
        }
        JSONObject outputChange = new JSONObject();
        outputChange.put("address",changeAddress);
        outputChange.put("amount",outputChangeAmount);

        outputs.add(outputChange);

        //找零金额小于等于手续费返回不签名
        long fee = (unspents.size() + outputs.size() * 2) * 148 - 10;
        if (outputChangeAmount <= fee) {
            return;
        }
        toResultString("SignBTCTransaction",new BTCSign().signBTCTransaction(privateKeyWIF,unspents,outputs));
    }


    /**
     * 广播类btc/eth/etc交易上链，对应不同method，注意替换
     * POST /onchainwallet/{currency}
     * @Param method
     * @Param signature
     * @Param currency
     */
    @Test
    public void testSendETHRawTransaction() {

        JSONObject jo =new JSONObject();
        jo.put("jsonrpc","2.0");
        jo.put("id","viewtoken");
        jo.put("method","eth_sendRawTransaction");
        jo.put("params", Arrays.asList("0xf86801851246f6f100825208949ae75431335d2e70f8db0b35f6c179a43756f78e8405f5e100801ca058b2130954d3b84918db5c0fc309dcc942b8656d88964a5f981a4a954bbb1221a0788b6b75afaea28d2e9178a6cea3d432983c10db1c778a1dcb6af009f5457701"));
        JSONObject onchainwallet = walletservice.sendRawTransaction("eth",jo);
        toResultString("SendRawTransaction", onchainwallet);
    }
    @Test
    public void testSendBTCRawTransaction(){
        JSONObject jo =new JSONObject();
        jo.put("jsonrpc","2.0");
        jo.put("id","viewtoken");
        jo.put("method","sendrawtransaction");
        jo.put("params", Arrays.asList("01000000016a79b0d91a898060f7a2dcd048ab8b1fe57d251a4496701bb64a1f3b5dc68066010000006b483045022100f5c22019fdd167dfe16988cabbe31a4eeda609abfd8369612813cd510cadca5102201270c8dca6a6b7826162d7f5b00c7a479ad32505a55e958301473bdcab72079f8121033efaa795ed22b1531127a3fd5b6e72031f6a7f4370fb67367768054ef011def0ffffffff02102700000000000017a914c64907144796b4177d6b5809c388c437385db4bb879ab00000000000001976a914d2ed73cd81bb2d516411c47a8ddb02ae5f2e7d0188ac00000000"));
        JSONObject onchainwallet = walletservice.sendRawTransaction("btc",jo);
        toResultString("SendRawTransaction", onchainwallet);
    }




}
