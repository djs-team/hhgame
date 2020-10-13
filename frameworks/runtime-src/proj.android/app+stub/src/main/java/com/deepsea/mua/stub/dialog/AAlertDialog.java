package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAlertBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 */
public class AAlertDialog extends BaseDialog<DialogAlertBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }

    public interface OnDismissListener {
        /**
         * 回调
         */
        void onDismiss();
    }

    private OnDismissListener dismissListener;

    public void setDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public AAlertDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
            dismiss();
        });
    }

    boolean isAutoDismiss = false;

    public void setIsAutoDismiss(boolean isAutoDismiss) {
        this.isAutoDismiss = isAutoDismiss;
        if (isAutoDismiss) {
            startObservable();
        }
    }

    private Subscription mSubscription;

    public void startObservable() {
        mSubscription = Observable.interval(0, 10, TimeUnit.MILLISECONDS)
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
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        if (isAutoDismiss) {
            stopObservable();
        }
        super.dismiss();

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

    public AAlertDialog setLeftButton(String str, int colorRes, int textSize, OnClickListener cli) {
//        ViewBindUtils.setTextColor(mBinding.leftButton, colorRes);
        mBinding.leftButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return setLeftButton(str, cli);
    }

    public AAlertDialog setLeftButton(String str, int colorRes, OnClickListener cli) {
//        ViewBindUtils.setTextColor(mBinding.leftButton, colorRes);
        return setLeftButton(str, cli);
    }

    public AAlertDialog setLeftButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, false);
        initButton(mBinding.leftButton, str, cli);
        return this;
    }

    public AAlertDialog setRightButton(String str, int colorRes, int textSize, OnClickListener cli) {
//        ViewBindUtils.setTextColor(mBinding.rightButton, colorRes);
        mBinding.rightButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        return setRightButton(str, cli);
    }

    public AAlertDialog setRightButton(String str, int colorRes, OnClickListener cli) {
//        ViewBindUtils.setTextColor(mBinding.rightButton, colorRes);
        return setRightButton(str, cli);
    }

    public AAlertDialog setRightButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, false);
        initButton(mBinding.rightButton, str, cli);
        return this;
    }

    public AAlertDialog setButton(String str, int colorRes, OnClickListener cli) {
//        ViewBindUtils.setTextColor(mBinding.middleButton, colorRes);
        return setButton(str, cli);
    }

    public AAlertDialog setButton(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, true);
        ViewBindUtils.setVisible(mBinding.leftButton, false);
        ViewBindUtils.setVisible(mBinding.rightButton, false);
        initButton(mBinding.middleButton, str, cli);
        return this;
    }
    public AAlertDialog setButtonInLogin(String str, OnClickListener cli) {
        ViewBindUtils.setVisible(mBinding.middleButton, true);
        ViewBindUtils.setVisible(mBinding.leftButton, false);
        ViewBindUtils.setVisible(mBinding.rightButton, false);
//获取控件的布局参数，设置控件的宽高
        ViewGroup.LayoutParams params = mBinding.middleButton.getLayoutParams();
        params.width = DisplayUtil.dip2px(mContext,50);
        params.height = DisplayUtil.dip2px(mContext,20);
        mBinding.middleButton.setLayoutParams(params);
        mBinding.middleButton.setGravity(Gravity.CENTER);
        mBinding.middleButton.setTextSize(10);
        initButton(mBinding.middleButton, str, cli);
        return this;
    }

    public void setTips(String tips) {
        ViewBindUtils.setVisible(mBinding.tvTips, true);
        ViewBindUtils.setText(mBinding.tvTips, tips);
    }

    private void initButton(TextView btn, String str, OnClickListener cli) {
        btn.setVisibility(View.VISIBLE);
        btn.setText(str);
        if (cli == null) {
            cli = (v, dia) -> dismiss();
        }
        final OnClickListener c = cli;
        btn.setOnClickListener(v -> c.onClick(v, AAlertDialog.this));
    }

    public AAlertDialog setTitle(String str) {
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertDialog setTitleSize(float size) {
        mBinding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertDialog setTitle(String str, int colorRes, float size) {
        ViewBindUtils.setTextColor(mBinding.titleTv, colorRes);
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        mBinding.titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertDialog setTitle(String str, int colorRes) {
        ViewBindUtils.setTextColor(mBinding.titleTv, colorRes);
        ViewBindUtils.setText(mBinding.titleTv, str);
        ViewBindUtils.setVisible(mBinding.titleTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertDialog setMessage(String str) {
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertDialog setMessageSize(float size) {
        mBinding.messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    public AAlertDialog setMessage(String str, int colorRes) {
        ViewBindUtils.setTextColor(mBinding.messageTv, colorRes);
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        return this;
    }

    public AAlertDialog setMessage(String str, int colorRes, int size) {
        ViewBindUtils.setTextColor(mBinding.messageTv, colorRes);
        ViewBindUtils.setText(mBinding.messageTv, str);
        ViewBindUtils.setVisible(mBinding.messageTv, !TextUtils.isEmpty(str));
        mBinding.messageTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }


    public AAlertDialog setCloseVisible() {
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
