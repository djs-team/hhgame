package com.deepsea.mua.stub.mvp;


import com.deepsea.mua.core.utils.LogUtils;

import rx.Subscriber;


public abstract class SubscriberCallBack<T> extends Subscriber<T> {


    @Override
    public void onNext(T data) {
        onSuccess(data);
        LogUtils.e("SubscriberCallBack", "onNext");
    }

    /**
     * 通用错误码
     * TOKEN_REQ(90000, "入参有误,拒绝处理此请求"),
     * USER_DISABLED(90001, "您已被禁用！请联系管理员"),
     * TOKEN_EXPIRE(90002, "登录信息已失效"),
     * DEVICE_DISABLED(90003, "设备被禁用！请联系管理员"),
     */
//    @Override
//    public void onNext(ResponseModel response) {
//        if (response.code == 90001 || response.code == 90002 || response.code == 90003) {
//            showMessageNegativeDialog(response.msg);
//            return;
//        }
//        onSuccess((T) response.data);
//        LogUtil.e("SubscriberCallBack", "onNext");
//    }
    @Override
    public void onCompleted() {
        LogUtils.e("SubscriberCallBackonCompleted");
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e("SubscriberCallBackonError" + e.getMessage());
        onError(e, 0);
    }


    protected abstract void onError(Throwable e, int a);


    protected abstract void onSuccess(T data);


}
