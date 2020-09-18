package com.deepsea.mua.stub.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.AppUtils;

/**
 * Created by JUN on 2019/4/2
 */
public class SPUtils {

    private static final String file_name = "sp_name";
    private static final int sp_mode = Context.MODE_PRIVATE;

    private static SharedPreferences getSp() {
        Context context = AppUtils.getApp().getApplicationContext();
        return context.getSharedPreferences(file_name, sp_mode);
    }

    public static boolean put(String key, Object value) {
        SharedPreferences.Editor edit = getSp().edit();

        if (value instanceof String) {
            if (!TextUtils.isEmpty((CharSequence) value)) {
                edit.putString(key, (String) value);
            }
        } else if (value instanceof Boolean) {
            edit.putBoolean(key, (Boolean) value);

        } else if (value instanceof Float) {
            edit.putFloat(key, (Float) value);

        } else if (value instanceof Integer) {
            edit.putInt(key, (Integer) value);
        } else {
            edit.putLong(key, (Long) value);
        }

        return edit.commit();
    }

    public static String getString(String key, String defualt) {
        return getSp().getString(key, defualt);
    }

    public static int getInt(String key, int defualt) {
        return getSp().getInt(key, defualt);
    }

    public static boolean getBoolean(String key, boolean defualt) {
        return getSp().getBoolean(key, defualt);
    }

    public static long getLong(String key, long defValue) {
        return getSp().getLong(key, defValue);
    }

    public static float getFloat(String key, float defValue) {
        return getSp().getFloat(key, defValue);
    }

    public static boolean remove(String key) {
        SharedPreferences.Editor edit = getSp().edit();
        edit.remove(key);
        return edit.commit();
    }
}
