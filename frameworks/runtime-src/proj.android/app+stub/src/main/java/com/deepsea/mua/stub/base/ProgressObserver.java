package com.deepsea.mua.stub.base;

import android.content.Context;

import com.deepsea.mua.core.dialog.LoadingDialog;

/**
 * Created by JUN on 2019/4/8
 */
public abstract class ProgressObserver<T> extends BaseObserver<T> {

    private Context mContext;
    private LoadingDialog mLoadingDialog;

    public ProgressObserver(Context context) {
        this.mContext = context;
    }

    @Override
    public void onLoading() {
        showProgress();
    }

    @Override
    public void onError(String msg, int code) {
        super.onError(msg, code);
        hideProgress();
    }

    @Override
    protected void onComplete() {
        hideProgress();
    }

    private void showProgress() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext);
        }
        mLoadingDialog.show();
    }

    public void hideProgress() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }
}
