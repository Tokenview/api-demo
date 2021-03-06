package com.tokenview.sign.eth;

import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ETHSign {

    public String signETHTransaction(String privateKey, String toAddress, String nonce,Long gasPrice,Long gasLimit,Long amount) {
        String signedStr = "";
        try {
            //发送ERC20Token时，需要构建交易的合约数据：
            List<Type> inputParameters = new ArrayList<>();
            inputParameters.add(new Address(toAddress));
            inputParameters.add(new Uint256(0L));//转出数量
            List<TypeReference<?>> outputParameters = new ArrayList<>();
            //调用的合约的方法  transfer，参数，输出 等等
            Function function = new Function("transfer", inputParameters, outputParameters);
            //将合约数据转换成16进制字符串
            String dataStr = FunctionEncoder.encode(function);
            RawTransaction tx = RawTransaction.createTransaction(
                    BigInteger.valueOf(Long.parseLong(nonce)),
                    BigInteger.valueOf(gasPrice),//gasPrice
                    BigInteger.valueOf(gasLimit),//gasLimit
                    toAddress,
                    BigInteger.valueOf(amount),//转账的"数量"
                    "");
            //创建钥匙对
            ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
            Credentials credentials = Credentials.create(ecKeyPair);
            System.out.println(credentials.getAddress());
            //签名交易
            byte[] signed = TransactionEncoder.signMessage(tx, credentials);
            //将签名的交易转换成16进制字符串，并用于广播交易
            signedStr = Numeric.toHexString(signed);
        } catch (Exception e) {

        }
        return signedStr;
    }

    public String getAddress(String privateKey){
        ECKeyPair ecKeyPair = ECKeyPair.create(Numeric.toBigInt(privateKey));
        Credentials credentials = Credentials.create(ecKeyPair);
        return credentials.getAddress();
    }
}
