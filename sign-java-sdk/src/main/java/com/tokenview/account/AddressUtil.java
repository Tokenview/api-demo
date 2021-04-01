package com.tokenview.account;

import com.tokenview.bean.WalletBean;
import com.tokenview.enums.Coin;
import com.tokenview.params.DashParams;
import com.tokenview.params.DogeParams;
import com.tokenview.params.LitecoinParams;
import com.tokenview.utils.ECKeyUtil;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

public class AddressUtil {

    public static WalletBean generateAddress(String mnemonic, Coin coin) throws UnreadableWalletException {
        ECKeyPair keyPair = ECKeyUtil.generateEcKey(mnemonic, coin);
        ECKey ecKey = ECKey.fromPrivate(keyPair.getPrivateKey());
        WalletBean wallet=new WalletBean();
        wallet.setCoin_type(coin.getCoin());
        NetworkParameters parameters=null;
        switch (coin){
            case BTC:
            case BCH:
            case BSV:
                parameters= MainNetParams.get();
                wallet.setAddress(ecKey.toAddress(parameters).toBase58());
                wallet.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
                break;
            case LTC:
                parameters= LitecoinParams.get();
                wallet.setAddress(ecKey.toAddress(parameters).toBase58());
                wallet.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
                break;
            case DOGE:
                parameters= DogeParams.get();
                wallet.setAddress(ecKey.toAddress(parameters).toBase58());
                wallet.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
                break;
            case DASH:
                parameters= DashParams.get();
                wallet.setAddress(ecKey.toAddress(parameters).toBase58());
                wallet.setPrivateKey(ecKey.getPrivateKeyAsWiF(parameters));
                break;
            case ETH:
            case HT:
            case ETC:
            case PI:
            case WAN:
            case EM:
                String address = Keys.getAddress(keyPair);
                if(coin== Coin.EM){
                    address="EM"+address;
                }else{
                    address="0x"+address;
                }
                wallet.setAddress(address);
                String privateKey = Numeric.toHexStringNoPrefixZeroPadded(keyPair.getPrivateKey(), Keys.PRIVATE_KEY_LENGTH_IN_HEX);
                wallet.setPrivateKey(privateKey);
                break;
            case NEO:
                io.neow3j.crypto.ECKeyPair ecKeyPair= io.neow3j.crypto.ECKeyPair.create(keyPair.getPrivateKey());
                wallet.setAddress(ecKeyPair.getAddress());
                wallet.setPrivateKey(ecKeyPair.exportAsWIF());
                break;
                default:
                    break;
        }
        wallet.setMnemonic(mnemonic);
       return wallet;
    }

}
