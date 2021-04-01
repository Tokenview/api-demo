package com.tokenview.enums;

public enum Coin {
    BTC("BTC","m/44'/0'/0'/0/0"),
    BCH("BCH","m/44'/145'/0'/0/0"),
    BSV("BSV","m/44'/145'/0'/0/0"),

    LTC("LTC","m/44'/2'/0'/0/0"),
    DOGE("Doge","m/44'/3'/0'/0/0"),
    DASH("Dash","m/44'/5'/0'/0/0"),

    NEO("NEO","m/44'/888'/0'/0/0"),

    EM("EM","m/44'/60'/0'/0/0"),

    ETH("ETH","m/44'/60'/0'/0/0"),
    ETC("ETC","m/44'/61'/0'/0/0"),
    HT("HT","m/44'/60'/0'/0/0"),
    PI("PI","m/44'/60'/0'/0/0"),
    WAN("WAN","m/44'/5718350'/0'/0/0");

    private String coin;
    private String path;
    Coin(String coin,String path){this.coin=coin;this.path=path;}

    public String getCoin(){return coin;}

    public String getPath(){return path;}
}
