package com.tokenview.sign.test;

import com.tokenview.sign.eth.ETHSign;
import com.tokenview.utils.BTCWalletUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ETHSignTest {

    @Test
    public void testGetPrivateKeyFromStrList() {
        List stringList = new ArrayList();
        stringList.add("gym");
        stringList.add("please");
        stringList.add("sauce");
        stringList.add("elephant");
        stringList.add("trap");
        stringList.add("bag");
        stringList.add("logic");
        stringList.add("okay");
        stringList.add("impulse");
        stringList.add("slogan");
        stringList.add("goose");
        stringList.add("birth");
        BTCWalletUtil.loadWalletByMnemonic("eth",stringList,",","");
    }

    @Test
    public void testGetAddress() {
        String address = new ETHSign().getAddress("b71a7616b42110d8345ddc6826ec42c2f1ce24d5f4d8efeb616168d5c1ef4a1f");
        log.info(address);
    }

    @Test
    public void testETHSign() {

        String sinature = new ETHSign().signETHTransaction(
                "0xb71a7616b42110d8345ddc6826ec42c2f1ce24d5f4d8efeb616168d5c1ef4a1f",
                "0x9Ae75431335d2e70f8DB0b35F6C179a43756f78e",
                "1",
                78500000000L,
                21000L,
                80000000000L);
        log.info(sinature);
    }

}
