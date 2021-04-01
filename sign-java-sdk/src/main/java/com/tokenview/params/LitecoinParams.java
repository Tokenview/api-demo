package com.tokenview.params;

import org.bitcoinj.params.AbstractBitcoinNetParams;

public class LitecoinParams extends AbstractBitcoinNetParams {

    /*
    48 --> L.. addresses
    50 --> the new M.. addresses
    Blockcypher doesn't support M address at the moment
     */

    public LitecoinParams() {
        super();
        p2shHeader = 5;
        dumpedPrivateKeyHeader = 176;
        addressHeader = 48;
        acceptableAddressCodes = new int[]{addressHeader, p2shHeader, 50};
        id = "org.litecoin.production";
    }

    private static LitecoinParams instance;
    public static synchronized LitecoinParams get() {
        if (instance == null) {
            instance = new LitecoinParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return "main";
    }

}
