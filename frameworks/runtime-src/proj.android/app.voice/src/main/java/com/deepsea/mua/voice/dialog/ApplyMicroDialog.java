package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogApplyMicroBinding;
import com.deepsea.mua.voice.databinding.DialogSongAlertBinding;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 */
public class ApplyMicroDialog extends BaseDialog<DialogApplyMicroBinding> {

    public ApplyMicroDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick();

        void onDismiss();
    }

    private OnClickListener mOnClickListener;


    public void setmOnClickListener(OnClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_apply_micro;
    }


    @Override
    protected float getDimAmount() {
        return 0F;
    }

    public void setMsg(String name, String url) {
        ViewBindUtils.setText(mBinding.tvTitle, String.format("%s申请上麦", name));
        GlideUtils.roundImage(mBinding.ivHead, url, R.drawable.ic_place, R.drawable.ic_place);
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            if (mOnClickListener != null) {
                mOnClickListener.onClick();
            }
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });

    }

    public void setAutoDismiss() {
        startObservable();
    }

    private Subscription mSubscription;

    private void startObservable() {
        mSubscription = Observable.interval(5000, 5000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> closeDialog());
    }

    private void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    private void closeDialog() {
        dismiss();
    }

    @Override
    public void dismiss() {
        stopObservable();
        super.dismiss();
    }


}
