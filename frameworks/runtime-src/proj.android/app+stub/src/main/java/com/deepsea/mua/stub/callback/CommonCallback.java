package com.deepsea.mua.stub.callback;

/**
 * Created by JUN on 2019/7/29
 */
public abstract class CommonCallback<T> {

    public abstract void onSuccess(T data);

    public void onError(String msg) {
    }
}
