package com.deepsea.mua.stub.utils;

import com.deepsea.mua.core.utils.MD5Utils;
import com.deepsea.mua.stub.data.User;

/**
 * Created by JUN on 2019/5/8
 */
public class SignatureUtils {

    /**
     * 接口签名
     *
     * @return
     */
    public static String signByToken() {
        User user = UserUtils.getUser();
        if (user != null) {
            String md5 = MD5Utils.getMD5(user.getToken());
            return StringUtils.toLowerCase(md5);
        }
        return "";
    }

    /**
     * 接口签名
     *
     * @param param
     * @return
     */
    public static String signWith(String param) {
        User user = UserUtils.getUser();
        if (user != null) {
            String md5 = MD5Utils.getMD5(user.getToken() + param);
            return StringUtils.toLowerCase(md5);
        }
        return "";
    }
}
