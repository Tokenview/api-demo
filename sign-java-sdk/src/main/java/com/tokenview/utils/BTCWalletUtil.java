package com.tokenview.utils;


import com.tokenview.bean.WalletBean;
import org.apache.commons.lang3.StringUtils;
import org.bitcoinj.core.*;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import java.util.List;


public class BTCWalletUtil {

    //   public static boolean BTC_TEST_NET = SharedPreferencesUtil.getInstance().getBoolean(NumberConstant.IS_TEST_NET,false);
    public static String BTC_TEST_PATH = "m/44'/1'/0'/0/0";
    public static String BTC_MAIN_PATH = "m/44'/0'/0'/0/0";
    public static String BTC_SEGWIT_MAIN_PATH = "m/49'/0'/0'/0/0";
    public static String BTC_SEGWIT_TEST_PATH = "m/49'/1'/0'/0/0";
    public static String ETH_MAIN_PATH="m/44'/60'/0'/0/0";

    /**
     * 通过助记词生成私钥
     */
    public static WalletBean loadWalletByDeterministicSeed(DeterministicSeed ds, String pwd, String walletName) {
        String path;
        NetworkParameters params;
        path = BTC_MAIN_PATH;
        params = MainNetParams.get();
        ECKeyPair keyPair = getEcKeyPairByDeterministicSeed(path, ds);
//        loadWalletBIP49ByDeterministicSeed(ds);
        if (keyPair == null) return null;
        //  获取Base64编码的64位的私钥
//        String privateKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
//               //第二种方式生成
//              ECKeyPair ecKeyPair = generateEcKeyPair(ds);
        ECKey ecKey = ECKey.fromPrivate(keyPair.getPrivateKey());
        //获取Base58编码压缩后的私钥
        String privateKeyAsWiF = ecKey.getPrivateKeyAsWiF(params);
        String address = ecKey.toAddress(params).toString();
        WalletBean bean = new WalletBean();
        bean.setCoin_type("BTC");
        bean.setMnemonic(getMnemonic(ds));
        bean.setAddress(address);
        bean.setKeystore("");
        bean.setPrivateKey(privateKeyAsWiF);
        bean.setName(walletName);
        return bean;
    }


    /**
     * 通过助记词生成 BIP49 协议的隔离见证地址
     */
    public static WalletBean loadWalletBIP49ByDeterministicSeed(List<String> mnemonic) {
        DeterministicSeed ds = getDeterministicSeed(mnemonic);
        String  path=BTC_SEGWIT_MAIN_PATH;
        NetworkParameters params= MainNetParams.get();
        String[] pathArray = path.split("/");
        byte[] seedBytes = ds.getSeedBytes();
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        //获取P2SH地址的过程
        String publicKey=Numeric.toHexStringNoPrefix(dkKey.getPubKeyHash());
        String redeemScript = String.format("0x0014%s", publicKey);
        byte[] bytes = Numeric.hexStringToByteArray(redeemScript);
        byte[] bytes1 = Utils.sha256hash160(bytes);
        String p2shAddress = Address.fromP2SHHash(params, bytes1).toBase58();
        System.out.println("p2sh address == "+p2shAddress);
        WalletBean bean=new WalletBean();
        bean.setCoin_type("BTC");
        bean.setName("BTC");
        bean.setAddress(p2shAddress);
        bean.setMnemonic(getMnemonic(ds));
        bean.setInsert_type(1);
        bean.setPrivateKey(dkKey.getPrivateKeyAsWiF(params));
        return bean;
    }



    /**
     * 通过助记词生成hex私钥,WIF私钥,也可生成地址
     */
    public static WalletBean loadWalletByDeterministicSeedTest(DeterministicSeed ds, String pwd, String walletName) {
        String path;
        NetworkParameters params;
        path = BTC_MAIN_PATH;
        params = MainNetParams.get();
        //params = TestNet3Params.get();
        ECKeyPair keyPair = getEcKeyPairByDeterministicSeed(path, ds);
        if (keyPair == null) return null;
        //  获取Base64编码的64位的私钥
//        String privateKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
        //       第二种方式生成
        //      ECKeyPair ecKeyPair = generateEcKeyPair(ds);
        ECKey ecKey = ECKey.fromPrivate(keyPair.getPrivateKey());
        String hexPrivateKey = ecKey.getPrivateKeyAsHex();
        ECKey ecKey1 = ECKey.fromPrivate(Hex.decode(hexPrivateKey));
        Address address1 = ecKey1.toAddress(params);
        System.out.println("bitcoin address == "+address1);
        //获取Base58编码压缩后的私钥
        System.out.println("privateKeyHex == "+ecKey.getPrivateKeyAsHex());
        //ecKey.
        String privateKeyAsWiF = ecKey.getPrivateKeyAsWiF(params);
        System.out.println("privateKeyWif =="+privateKeyAsWiF);
        String address = ecKey.toAddress(params).toString();
        System.out.println("bitcoin address == "+address);
        WalletBean bean = new WalletBean();
        bean.setCoin_type("BTC");
        bean.setMnemonic(getMnemonic(ds));
        bean.setAddress(address);
        bean.setKeystore("");
        bean.setPrivateKey(privateKeyAsWiF);
        bean.setName(walletName);
        return bean;
    }

    /**
     * 通过私钥导入BTC钱包   (base58)
     * */
    public static WalletBean loadWalletByPrivateKey(String privateKey) {
        NetworkParameters params;
        params = MainNetParams.get();
        try {
            DumpedPrivateKey priKey = DumpedPrivateKey.fromBase58(params, privateKey);
            ECKey ecKey = priKey.getKey();
            String address = ecKey.toAddress(params).toString();
            WalletBean bean = new WalletBean();
            bean.setCoin_type("BTC");
            bean.setMnemonic(" ");
            bean.setAddress(address);
            bean.setKeystore("");
            bean.setPrivateKey(privateKey);
            bean.setName(" ");
            return bean;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 通过助记词生成私钥
     */
    public static WalletBean loadWalletByMnemonic(String currency,List<String> list, String pwd, String walletName) {
        String path;
        NetworkParameters params;
        path = "";
        if (currency.equalsIgnoreCase("btc")) {
            path = BTC_MAIN_PATH;
        }
        if (currency.equalsIgnoreCase("eth")) {
            path = ETH_MAIN_PATH;
        }
        params = MainNetParams.get();
        DeterministicSeed ds = getDeterministicSeed(list);
        ECKeyPair keyPair = getEcKeyPairByDeterministicSeed(path, ds);
//        //获取16编码的64位的私钥
//        String privateKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
//        ECKeyPair ecKeyPair = generateEcKeyPair(ds);
        ECKey ecKey = ECKey.fromPrivate(keyPair.getPrivateKey());
        //获取Base58编码压缩后的私钥
        String privateKeyAsWiF = ecKey.getPrivateKeyAsWiF(params);
        String address = ecKey.toAddress(params).toString();
        System.out.println("privateKey ===="+ecKey.getPrivateKeyAsHex());
        System.out.println("address ====="+address);
        WalletBean bean = new WalletBean();
        bean.setCoin_type("BTC");
        bean.setMnemonic(getMnemonic(ds));
        bean.setAddress(address);
        bean.setKeystore("");
        bean.setPrivateKey(privateKeyAsWiF);
        bean.setName(walletName);
        return bean;
    }


    /**
     * 通过路径和种子获取私钥对
     */
    public static ECKeyPair getEcKeyPairByDeterministicSeed(String path, DeterministicSeed ds) {
        String[] pathArray = path.split("/");
        byte[] seedBytes = ds.getSeedBytes();
        if (seedBytes == null)
            return null;
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        return keyPair;
    }



    /**
     * 通过路径和种子获取私钥对
     */
    public static DeterministicKey getPrivateBySeed(String path, DeterministicSeed ds) {
        String[] pathArray = path.split("/");
        byte[] seedBytes = ds.getSeedBytes();
        if (seedBytes == null)
            return null;
        DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
        for (int i = 1; i < pathArray.length; i++) {
            ChildNumber childNumber;
            if (pathArray[i].endsWith("'")) {
                int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
                childNumber = new ChildNumber(number, true);
            } else {
                int number = Integer.parseInt(pathArray[i]);
                childNumber = new ChildNumber(number, false);
            }
            dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
        }
//        ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
        return dkKey;
    }



    /**
     * 通过助记词生成种子
     * */
    public static DeterministicSeed getDeterministicSeed(List<String> list) {
        try {
            long creationTimeSeconds = System.currentTimeMillis() / 1000;
            return new DeterministicSeed(list, null, "", creationTimeSeconds);
        }catch (Exception e){}
        return null;
    }

    /**
     * 通过助记词生成Hex私钥
     * */
    public static String getPrivateKeyHex(List<String> list) {
        try {
            long creationTimeSeconds = System.currentTimeMillis() / 1000;
            DeterministicSeed seed =  new DeterministicSeed(list, null, "", creationTimeSeconds);
            ECKeyPair keyPair = getEcKeyPairByDeterministicSeed("m/44'/1'/0'/0/0", seed);
            if (keyPair == null) return null;
            ECKey ecKey = ECKey.fromPrivate(keyPair.getPrivateKey());
            String hexPrivateKey = ecKey.getPrivateKeyAsHex();
            return hexPrivateKey;
        }catch (Exception e){}
        return null;
    }

    /**
     * 通过种子生成助记词字符串
     */
    public static String getMnemonic(DeterministicSeed ds) {
        StringBuilder sb = new StringBuilder();
        List<String> mnemonicList = ds.getMnemonicCode();
        for (int i = 0; mnemonicList != null && i < mnemonicList.size(); i++) {
            sb.append(mnemonicList.get(i) + " ");
        }
        return sb.toString().trim();
    }

    /**判断地址的有效性*/
    public static boolean isBTCValidAddress(String input) {
        if (StringUtils.isEmpty(input))return false;
        try {
            NetworkParameters networkParameters = null;
            networkParameters = MainNetParams.get();
            Address address = Address.fromBase58(networkParameters, input);
            if (address != null)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }


}
