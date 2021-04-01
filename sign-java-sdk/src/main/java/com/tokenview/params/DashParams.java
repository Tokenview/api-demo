package com.tokenview.params;

import org.bitcoinj.params.AbstractBitcoinNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhaoda
 * @date 2019/10/17.
 * GitHub：
 * email：
 * description：
 */
public class DashParams extends AbstractBitcoinNetParams {
    private static final Logger log = LoggerFactory.getLogger(DashParams.class);

    public static final int MAINNET_MAJORITY_WINDOW = 1000;
    public static final int MAINNET_MAJORITY_REJECT_BLOCK_OUTDATED = 950;
    public static final int MAINNET_MAJORITY_ENFORCE_BLOCK_UPGRADE = 750;

    public static final int MAINNET_MAJORITY_DIP0001_WINDOW = 4032;
    public static final int MAINNET_MAJORITY_DIP0001_THRESHOLD = 3226;

    public static final int p2shHeaderDash = 16;

    public static final int AddressHeaderDash = 76;

    public DashParams() {
        super();
        p2shHeader = p2shHeaderDash;
        dumpedPrivateKeyHeader = 204;
        addressHeader = AddressHeaderDash;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader};
        id = ID_MAINNET;
        interval = INTERVAL;

    }

    private static DashParams instance;
    public static synchronized DashParams get() {
        if (instance == null) {
            instance = new DashParams();
        }
        return instance;
    }

    @Override
    public String getPaymentProtocolId() {
        return PAYMENT_PROTOCOL_ID_MAINNET;
    }

}

