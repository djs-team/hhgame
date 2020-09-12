package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAlertBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 */
public class AAlertAotoDismissDialog extends BaseDialog<DialogAlertBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }

    public AAlertAotoDismissDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
            dismiss();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_alert;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public AAlertAotoDismissDialog setLeftButton(String str, int colorRes, int textSize, OnClickListener cli) {
        ViewBindUtils.setTextColor(mBinding.leftButton, colorRes);
        mBinding.leftButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return setLeftButton(str, cli);
    }

    public AAlertAotoDismissDialog setLeftButton(String str, int colorRes, OnClickListener cli) {
        ViewBindUtils.setTextColor(mBinding.leftButton, colorRes);
        return setLeftButton(str, cli);
    }

    public AAlertAotoDismissDialog setLeftButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, false);
        initButton(mBinding.leftButton, str, cli);
        return this;
    }

    public AAlertAotoDismissDialog setRightButton(String str, int colorRes, int textSize, OnClickListener cli) {
        ViewBindUtils.setTextColor(mBinding.rightButton, colorRes);
        mBinding.rightButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return setRightButton(str, cli);
    }

    public AAlertAotoDismissDialog setRightButton(String str, int colorRes, OnClickListener cli) {
        ViewBindUtils.setTextColor(mBinding.rightButton, colorRes);
        return setRightButton(str, cli);
    }

    public AAlertAotoDismissDialog setRightButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, false);
        initButton(mBinding.rightButton, str, cli);
        return this;
    }

    public AAlertAotoDismissDialog setButton(String str, int colorRes, OnClickListener cli) {
        ViewBindUtils.setTextColor(mBinding.middleButton, colorRes);
        return setButton(str, cli);
    }

    public AAlertAotoDismissDialog setButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, true);
        ViewBindUtils.setVisible(mBinding.leftButton, false);
        ViewBindUtils.setVisible(mBinding.rightButton, false);
        startObservable();
        initButton(mBinding.middleButton, str, cli);
        return this;
    }
    private Subscription mSubscription;

    public void startObservable() {
        mSubscription = Observable.interval(5000, 0, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> dismiss());
    }

    public void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void dismiss() {
        stopObservable();
        super.dismiss();
    }

    private void initButton(TextView btn, String str, OnClickListener cli) {
        btn.setText(str);
        if (cli == null) {
            cli = (v, dia) -> dismiss();
        }
        final OnClickListener c = cli;
        btn.setOnClickListener(v -> c.onClick(v, AAlertAotoDismissDialog.this));
    }

    public AAlertAotoDismissDialog setTitle(String str) {
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertAotoDismissDialog setTitleSize(float size) {
        mBinding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertAotoDismissDialog setTitle(String str, int colorRes, float size) {
        ViewBindUtils.setTextColor(mBinding.titleTv, colorRes);
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        mBinding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertAotoDismissDialog setTitle(String str, int colorRes) {
        ViewBindUtils.setTextColor(mBinding.titleTv, colorRes);
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertAotoDismissDialog setMessage(String str) {
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertAotoDismissDialog setMessageSize(float size) {
        mBinding.messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertAotoDismissDialog setMessage(String str, int colorRes) {
        ViewBindUtils.setTextColor(mBinding.messageTv, colorRes);
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertAotoDismissDialog setMessage(String str, int colorRes, int size) {
        ViewBindUtils.setTextColor(mBinding.messageTv, colorRes);
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        mBinding.messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertAotoDismissDialog setCloseVisible() {
        ViewBindUtils.setVisible(mBinding.closeIv, true);
        return this;
    }

    public void setLineVisible(boolean visible) {
//        ViewBindUtils.setVisible(mBinding.line, visible);
    }

    public TextView getTitleTv() {
        return mBinding.titleTv;
    }

    public TextView getMessageTv() {
        return mBinding.messageTv;
    }
}
