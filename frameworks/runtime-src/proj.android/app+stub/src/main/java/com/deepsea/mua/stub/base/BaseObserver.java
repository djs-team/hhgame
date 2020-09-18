package com.deepsea.mua.stub.base;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.ToastUtils;

/**
 * t
 * Created by JUN on 2019/4/8
 */
public abstract class BaseObserver<T> implements Observer<Resource<T>> {

    @Override
    public void onChanged(@Nullable Resource<T> resource) {
        if (resource != null) {
            switch (resource.status) {
                case SUCCESS:
                    onComplete();
                    onSuccess(resource.data);
                    break;
                case ERROR:
                    onError(resource.message, resource.code);
                    break;
                case LOADING:
                    onLoading();
                    break;
            }
        }
    }

    public void onLoading() {
    }

    public void onError(String msg, int code) {
        ToastUtils.showToast(msg);
    }

    protected void onComplete() {
    }

    public abstract void onSuccess(T result);
}
