package com.deepsea.mua.stub.utils;

import android.text.TextUtils;

import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.data.User;

/**
 * Created by JUN on 2019/3/29
 */
public class UserUtils {

    private static User currentUser = null;
    private static String KEY = User.class.getName();

    /**
     * 获取user
     *
     * @return
     */
    public synchronized static User getUser() {
        if (currentUser == null) {
            String string = SPUtils.getString(KEY, "");
            if (!TextUtils.isEmpty(string)) {
                currentUser = JsonConverter.fromJson(string, User.class);
            }
        }
        return currentUser;
    }

    /**
     * 保存user
     *
     * @param user
     * @return
     */
    public synchronized static void saveUser(User user) {
        if (user != null) {
            boolean result = SPUtils.put(KEY, JsonConverter.toJson(user));
            if (result) {
                currentUser = user;
            }
        }
    }

    /**
     * 清除user
     *
     * @return
     */
    public synchronized static void clearUser() {
        boolean result = SPUtils.remove(KEY);
        if (result) {
            currentUser = null;
        }
    }

    /**
     * 清除user内存缓存
     */
    public synchronized static void releaseUser() {
        currentUser = null;
    }

}
