package com.tokenview.utils;

/**
 * @author zhaoda
 * @date 2020/6/1.
 * GitHub：
 * email：
 * description：
 */
public class HexUtil {
    private static final char[] digits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public static final byte[] emptybytes = new byte[0];

    public HexUtil() {
    }

    public static String byte2HexStr(byte b) {
        char[] buf = new char[]{'\u0000', digits[b & 15]};
        b = (byte)(b >>> 4);
        buf[0] = digits[b & 15];
        return new String(buf);
    }

    public static String bytes2HexStr(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            char[] buf = new char[2 * bytes.length];

            for(int i = 0; i < bytes.length; ++i) {
                byte b = bytes[i];
                buf[2 * i + 1] = digits[b & 15];
                b = (byte)(b >>> 4);
                buf[2 * i + 0] = digits[b & 15];
            }

            return new String(buf);
        } else {
            return null;
        }
    }

    public static byte hexStr2Byte(String str) {
        return str != null && str.length() == 1 ? char2Byte(str.charAt(0)) : 0;
    }

    public static byte char2Byte(char ch) {
        if (ch >= '0' && ch <= '9') {
            return (byte)(ch - 48);
        } else if (ch >= 'a' && ch <= 'f') {
            return (byte)(ch - 97 + 10);
        } else {
            return ch >= 'A' && ch <= 'F' ? (byte)(ch - 65 + 10) : 0;
        }
    }

    public static byte[] hexStr2Bytes(String str) {
        if (str != null && !str.equals("")) {
            byte[] bytes = new byte[str.length() / 2];

            for(int i = 0; i < bytes.length; ++i) {
                char high = str.charAt(i * 2);
                char low = str.charAt(i * 2 + 1);
                bytes[i] = (byte)(char2Byte(high) * 16 + char2Byte(low));
            }

            return bytes;
        } else {
            return emptybytes;
        }
    }

    public static void main(String[] args) {
        try {
            byte[] bytes = "Hello WebSocket World?".getBytes("gbk");
            System.out.println(bytes2HexStr(bytes));
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
