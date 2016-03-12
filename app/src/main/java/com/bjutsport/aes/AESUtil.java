package com.bjutsport.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by HUDIYU on 2016/3/12.
 */
public class AESUtil {
    //秘钥的初始化向量
    static final public byte[] KEY_IV = {1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8};
    //编码模式
    public static final String ENCODED_MODE = "UTF-8";

    //加密
    public static String encrypt(String seed, String cleartext) throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(KEY_IV);
        SecretKeySpec key = new SecretKeySpec(seed.getBytes(ENCODED_MODE), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(cleartext.getBytes(ENCODED_MODE));
        return new String(toHex(encryptedData));
    }

    //解密
    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] byteMi = toByte(encrypted);
        IvParameterSpec zeroIv = new IvParameterSpec(KEY_IV);
        SecretKeySpec key = new SecretKeySpec(seed.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte[] decryptedData = cipher.doFinal(byteMi);

        return new String(decryptedData, ENCODED_MODE);
    }

    //十六进制字符串转换为十进制字节数组
    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    //将十进制字节数组转换成十六进制字符串
    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }

        StringBuffer result = new StringBuffer(2 * buf.length);

        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    private final static String HEX = "0123456789ABCDEF";

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }
}
