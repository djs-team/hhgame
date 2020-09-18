package com.deepsea.mua.mine.utils;

import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.utils.UserUtils;

/**
 * Created by JUN on 2019/8/5
 */
public class WebViewUtils {

    /**
     * 添加app相关参数到url
     *
     * @param srcUrl
     * @return
     */
    public static String addTokenToUrl(String srcUrl) {
        String desUrl = srcUrl;
        if (!TextUtils.isEmpty(desUrl)) {
            User user = UserUtils.getUser();
            if (user != null) {
                desUrl = updateParameter(desUrl, "uid", user.getUid(), true);
                desUrl = updateParameter(desUrl, "token", user.getToken(), true);
            }
        }
        Log.i("WebViewUtils", "desUrl: " + desUrl);
        return desUrl;
    }

    /**
     * 更新url参数
     *
     * @param srcUrl
     * @param key
     * @param value
     * @param addNullValue true会加入空的参数对
     * @return
     */
    public static String updateParameter(String srcUrl, String key, String value, boolean addNullValue) {
        String urlParameterKey = key + "=";
        if (!srcUrl.contains(urlParameterKey)) {
            if (addNullValue || !TextUtils.isEmpty(value)) {
                if (srcUrl.contains("?")) {
                    srcUrl = srcUrl + "&" + urlParameterKey + value;
                } else {
                    srcUrl = srcUrl + "?" + urlParameterKey + value;
                }
            }
        } else {
            String[] parts = srcUrl.split(urlParameterKey);
            StringBuilder sb = new StringBuilder(parts[0]);
            if (addNullValue || !TextUtils.isEmpty(value)) {
                sb.append(urlParameterKey);
            }
            if (parts.length < 2 || TextUtils.isEmpty(parts[1])) {
                if (addNullValue || !TextUtils.isEmpty(value)) {
                    sb.append(value);
                }
            } else {
                String paramStr = parts[1];
                int pos = paramStr.indexOf("&");
                if (pos < 0) {
                    if (addNullValue || !TextUtils.isEmpty(value)) {
                        sb.append(value);
                    }
                } else {
                    if (addNullValue || !TextUtils.isEmpty(value)) {
                        sb.append(value);
                    }
                    sb.append(paramStr.substring(pos));
                }
            }
            srcUrl = sb.toString();
        }
        return srcUrl;
    }
}
