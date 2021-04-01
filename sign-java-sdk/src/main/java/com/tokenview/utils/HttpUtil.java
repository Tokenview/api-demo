package com.tokenview.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;

@Slf4j
public class HttpUtil {

    private String get(String url) {
        return HttpClientUtils.get(url, new HashMap<>());
    }

    public JSONObject getTransactionDetail(String tx){
        StringBuffer url = new StringBuffer("https://chain.api.btc.com/v3/tx/");
        url.append(tx);
        url.append("?verbose=3");
        String result = null;
        try {
            result = get(url.toString());
        } catch (Exception e) {
            log.info(e.getMessage(), e);
        }
        return JSONObject.parseObject(result);
    }

    public JSONArray getUnspent(String txid,List<String> addrList){
        JSONObject result =new HttpUtil().getTransactionDetail(txid).getJSONObject("data");
        long height  =result.getLong("block_height");
        JSONArray outputsArray = result.getJSONArray("outputs");
        JSONArray unspents = new JSONArray();
        int index =0;
        for (Object o : outputsArray){
            Object io = JSONObject.parseObject(o.toString()).get("addresses");
            String address = String.valueOf(io).replace("[\"","").replace("\"]","");
            if (addrList.contains(address)){
                JSONObject txJson = JSONObject.parseObject(o.toString());
                txJson.put("txid",txid);
                txJson.put("tx_output_n",index);
                txJson.put("block_height",height);
                unspents.add(txJson);
            }
            index++;
        }
        log.info(JSON.toJSONString(unspents));
        return unspents;
    }
}
