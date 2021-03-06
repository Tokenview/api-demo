package com.tokenview.sign.btc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tokenview.enums.Version;
import org.bitcoinj.core.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.script.Script;
import org.spongycastle.util.encoders.Hex;

public class BTCSign {
    public String signBTCTransaction(String privateKey,JSONArray unspents,JSONArray outputs) {
        String hexString = "";
        try {
            NetworkParameters params = MainNetParams.get();
            DumpedPrivateKey priKey = DumpedPrivateKey.fromBase58(params,privateKey);
            ECKey ecKey = priKey.getKey();
            System.out.println(ecKey.getPrivateKeyAsHex());
            System.out.println(ecKey.getPublicKeyAsHex());
            System.out.println(new String(ecKey.getPubKeyHash()));
            //构建交易体
            Transaction transaction = new Transaction(params);
            //交易的版本  默认的BTC，LTC ,DOGE,DASH  版本号是1,   BCH  BSV  版本号是：2；
            transaction.setVersion(Version.BTC.getVersion());
            long fee = (unspents.size() + outputs.size() * 2) * 148 - 10;
            //添加转出到的地址列表和数量
            for(int i = 0;i<outputs.size()-1;i++){
                transaction.addOutput(Coin.valueOf(outputs.getJSONObject(i).getLong("amount")),Address.fromBase58(params,outputs.getJSONObject(i).getString("address")));
            }
            JSONObject outputChange = outputs.getJSONObject(outputs.size()-1);
            transaction.addOutput(Coin.valueOf(outputChange.getLong("amount")-fee),Address.fromBase58(params, outputChange.getString("address")));
        
            for (int i = 0; i < unspents.size(); i++) {
                UTXO utxo = new UTXO(Sha256Hash.wrap(unspents.getJSONObject(i).getString("txid")),
                        Long.parseLong(unspents.getJSONObject(i).getString("tx_output_n")),
                        Coin.valueOf(unspents.getJSONObject(i).getLong("value")),
                        0,
                        false,
                        new Script(Hex.decode(unspents.getJSONObject(i).getString("script_hex"))));
                TransactionOutPoint outPoint = new TransactionOutPoint(params, utxo.getIndex(), utxo.getHash());
                transaction.addSignedInput(outPoint, utxo.getScript(), priKey.getKey(), Transaction.SigHash.ALL, true);
            }
            //将签名的交易转换成16进制字符串，并用于广播交易
            hexString = Hex.toHexString(transaction.bitcoinSerialize());

        }catch (Exception e){
            e.printStackTrace();
        }
        return hexString;
    }

    public String getAddress(String privateKeyHex){
        NetworkParameters params = MainNetParams.get();
        ECKey ecKey = ECKey.fromPrivate(org.bouncycastle.util.encoders.Hex.decode(privateKeyHex));
        return ecKey.toAddress(params).toString();
    }


}
