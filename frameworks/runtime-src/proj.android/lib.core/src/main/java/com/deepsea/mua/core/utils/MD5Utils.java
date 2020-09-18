package com.deepsea.mua.core.utils;

import java.security.MessageDigest;

/**
 * Created by JUN on 2019/4/9
 */
public class MD5Utils {

    private MD5Utils() {
    }

    /**
     * SHA1编码
     */
    public static String SHA1Encode(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            return byte2String(md.digest(source.getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException("md5 encode error");
        }
    }

    /**
     * byte数组转换为字符串
     */
    public static String byte2String(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }

    /**
     * md5加密
     *
     * @param text 待加密字符串
     * @return 加密后32位字符串
     */
    public static String getMD5(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : buffer) {
                int a = b & 0xff;
                String hex = Integer.toHexString(a);

                if (hex.length() == 1) {
                    hex = 0 + hex;
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
