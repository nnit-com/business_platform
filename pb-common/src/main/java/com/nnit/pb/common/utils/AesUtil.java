package com.nnit.pb.common.utils;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public class AesUtil {
    private static final String AES = "AES";
    private static final String CRYPT_KEY = "#Gj46v9=dTHNwqE=";
    static {
        String errorString = "error : 手动覆盖密钥长度权限失败";
        int newMaxKeyLength;
        try {
            if ((newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES")) < 256) {
                Class c = Class.forName("javax.crypto.CryptoAllPermissionCollection");
                Constructor con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissionCollection = con.newInstance();
                Field f = c.getDeclaredField("all_allowed");
                f.setAccessible(true);
                f.setBoolean(allPermissionCollection, true);
                c = Class.forName("javax.crypto.CryptoPermissions");
                con = c.getDeclaredConstructor();
                con.setAccessible(true);
                Object allPermissions = con.newInstance();
                f = c.getDeclaredField("perms");
                f.setAccessible(true);
                ((Map) f.get(allPermissions)).put("*", allPermissionCollection);
                c = Class.forName("javax.crypto.JceSecurityManager");
                f = c.getDeclaredField("defaultPolicy");
                f.setAccessible(true);
                Field mf = Field.class.getDeclaredField("modifiers");
                mf.setAccessible(true);
                mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                f.set(null, allPermissions);
                newMaxKeyLength = Cipher.getMaxAllowedKeyLength("AES");
            }
        } catch (Exception e) {
            throw new RuntimeException(errorString, e);
        }
        if (newMaxKeyLength < 256) {
            throw new RuntimeException(errorString);
        }
    }
    /**
     * 加密
     *
     * @return
     */
    public static byte[] encrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
        //设置密钥和加密形式
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }
    /**
     * 解密
     *
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        //设置加密Key
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes(), AES);
        //设置密钥和解密形式
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        return cipher.doFinal(src);
    }
    /**
     * 二行制转十六进制字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String tmp = "";
        for (int n = 0; n < b.length; n++) {
            tmp = (Integer.toHexString(b[n] & 0XFF));
            if (tmp.length() == 1) {
                hs.append("0" + tmp);
            } else {
                hs.append(tmp);
            }
        }
        return hs.toString().toUpperCase();
    }
    public static byte[] hex2byte(byte[] b) {
        int num = 2;
        if ((b.length % num) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += num) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }
    /**
     * 解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes()),
                    CRYPT_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data) {
        try {
            return byte2hex(encrypt(data.getBytes(), CRYPT_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        String json = "lufy27273d"; 
        System.out.println("原数据：" + json);
        String dataEncrypt = encrypt(json);
        System.out.println("加密数据：" + dataEncrypt);
        String dataDecrypt = decrypt(dataEncrypt);
        System.out.println("解密数据：" + dataDecrypt);
    }
}