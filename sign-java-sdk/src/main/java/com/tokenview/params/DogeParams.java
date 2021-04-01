package com.tokenview.params;

import org.bitcoinj.params.AbstractBitcoinNetParams;

/**
 * @author zhaoda
 * @date 2019/10/18.
 * GitHub：
 * email：
 * description：
 */
public class DogeParams extends AbstractBitcoinNetParams {
    public static final String ID_DOGE_MAINNET = "org.dogecoin.production";
    public DogeParams() {
        super();
        p2shHeader = 22;
        dumpedPrivateKeyHeader = 158;
        addressHeader = 30;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader};
        id = ID_DOGE_MAINNET;
//        interval = INTERVAL;
//        bip32HeaderPub = 0x02facafd; //The 4 byte header that serializes in base58 to "dgub".
//        bip32HeaderPriv =  0x02fac398; //The 4 byte header that serializes in base58 to "dgpv".
    }

    private static DogeParams instance;
    public static synchronized DogeParams get() {
        if (instance == null) {
            instance = new DogeParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return "main";
    }
}
