package com.tokenview.sign.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tokenview.sign.btc.BTCSign;
import com.tokenview.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BTCSignTest {

    @Test
    public void testGetUnspent(){
        String txid = "13c6235ee5e6403606f1539bc872813ac14a90521da6248ffbf63913a8675573";
        List<String> addrList = new ArrayList<>();
        addrList.add("1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ");
        JSONArray unspents =new HttpUtil().getUnspent(txid,addrList);
        log.info(JSON.toJSONString(unspents));
    }


    @Test
    public void testBTCSign(){
        //unspent所在txid和所在地址
        String txid = "6680c65d3b1f4ab61b7096441a257de51f8bab48d0dca2f76080891ad9b0796a";
        List<String> addrList = new ArrayList<>();
        addrList.add("1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ");
        //可自行设置多个输出地址和金额，找零地址和金额
        JSONArray unspents =new HttpUtil().getUnspent(txid,addrList);
        long inputAmount =unspents
                .stream()
                .mapToLong(o->JSONObject.parseObject(o.toString()).getLong("value"))
                .sum();

        JSONArray outputs = new JSONArray();
        JSONObject outputTo = new JSONObject();
        outputTo.put("account","16SzvWdCrYsVsMuRp43TfqvGvibBah7s17");
        outputTo.put("amount",10000);
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
        outputChange.put("account","1LEHMmGUAzjvMFCoaoUsY46avHzCN3pUdQ");
        outputChange.put("amount",outputChangeAmount);

        outputs.add(outputChange);

        //找零金额小于等于手续费返回不签名
        long fee = (unspents.size() + outputs.size() * 2) * 148 - 10;
        if (outputChangeAmount <= fee) {
            return;
        }

        String signature = new BTCSign().signBTCTransaction("L2xigpefbyesCnzR5hJDFxKPD4A2qSuNHiz4utWnqEovhownw8U2",unspents,outputs);
        log.info(signature);
    }

    @Test
    public void testGetAddress(){
       log.info(new BTCSign().getAddress("d71405cd1c95271e4c17569494100856d9e8706e081702ec5ec91a5b20ce1957"));
    }


}
