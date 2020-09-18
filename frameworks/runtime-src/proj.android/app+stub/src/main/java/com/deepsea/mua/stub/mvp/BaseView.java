package com.deepsea.mua.stub.mvp;

/**
 * Created by ${cs} on 2018/10/18.
 * @func @see setPresenter() 绑定Presenter
 */

public interface BaseView<T> {
	void setPresenter(T presenter);
    void onFailure();
}
