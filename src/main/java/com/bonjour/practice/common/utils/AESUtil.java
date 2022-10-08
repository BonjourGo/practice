package com.bonjour.practice.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 加解密工具类
 * @authur tc
 * @date 2022/10/8 10:56
 */
public class AESUtil {
    static String iv = "d22b0a851e014f7b";

    private final static Logger logger = LoggerFactory.getLogger(AESUtil.class);

    private final static Base64.Encoder encoder = Base64.getEncoder();
    private final static Base64.Decoder decoder = Base64.getDecoder();

    private static final String defaultCharset = "UTF-8";
    private static final String KEY_AES = "AES";
    public static final String key = "DEMO_ENCRYPT_KEY";

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @return
     */
    public static String encrypt(String data) {
        return doAES(data, key, iv.getBytes(), Cipher.ENCRYPT_MODE);
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @return
     */
    public static String decrypt(String data) {
        return doAES(data, key, iv.getBytes(), Cipher.DECRYPT_MODE);
    }

    /**
     * 加解密
     * @param data
     * @param secretKey
     * @param iv
     * @param mode
     * @return
     */
    public static String doAES(String data, String secretKey, byte[] iv, int mode) {

        try {
            boolean encrypt = mode == Cipher.ENCRYPT_MODE;
            byte[] content;
            //true 加密内容 false 解密内容
            if (encrypt) {
                content = data.getBytes(defaultCharset);
            } else {
                content = decoder.decode(data);
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec skeySpec = new SecretKeySpec(secretKey.getBytes(), KEY_AES);
            cipher.init(mode, skeySpec, new IvParameterSpec(iv));
            byte[] result = cipher.doFinal(content);
            if (encrypt) {
                return new String(encoder.encode(result));
            } else {
                return new String(result, defaultCharset);
            }
        } catch (Exception e) {
            logger.error("加密失败:", e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(encrypt("3333"));
        System.out.println(decrypt("8p9cCER0dS1/YZ8dOJOMgA=="));
    }
}
