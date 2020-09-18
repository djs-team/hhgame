package com.deepsea.mua.core.login;

/**
 * Created by JUN on 2018/9/28
 */
public interface OnLoginListener {

    /**
     * 第三方登录成功
     *
     * @param apiUser
     */
    void onLogin(ApiUser apiUser);

    void onCancel();

    void onError(String msg);
}
