package com.deepsea.mua.stub.utils;

import android.text.TextUtils;

/**
 * Created by JUN on 2019/10/23
 */
public class ProfileUtils {

    public static String getProfile(String age, String city, int stature) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(age)) {
            sb.append(age).append("岁");
        }
        if (!TextUtils.isEmpty(city)) {
            sb.append(sb.length() == 0 ? "" : " | ").append(city);
        }
        if (stature > 0) {
            sb.append(sb.length() == 0 ? "" : " | ").append(stature);
        }
        return sb.toString();
    }

    public static String getProfile(String age, int stature, int sex) {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(age)) {
            sb.append(age).append("岁");
        }
        if (stature > 0) {
            sb.append(sb.length() == 0 ? "" : "|").append(stature).append("cm");
        }
        String sexString="";
        if (sex==1){
            sexString="男";
        }else if (sex==2){
            sexString="女";
        }
        if (!TextUtils.isEmpty(sexString)) {
            sb.append(sb.length() == 0 ? "" : "|").append(sexString);
        }
        return sb.toString();
    }


}
