package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.socket.receive.ShowGuardAnimationToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateFinishTaskToClientParam;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogGuardSuccessBinding;
import com.deepsea.mua.voice.databinding.DialogTaskRoseAlertBinding;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/10/18
 */
public class TaskRoseReceiveDialog extends BaseDialog<DialogTaskRoseAlertBinding> {
    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onReceive();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public TaskRoseReceiveDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_task_rose_alert;
    }

    @Override
    protected float getWidthPercent() {
        return 0.86F;
    }


    @Override
    protected float getDimAmount() {
        return 0.5f;
    }

    @Override
    protected void initListener() {
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onReceive();
                }
            }
        });
    }


    public void setData(UpdateFinishTaskToClientParam bean) {
        ViewBindUtils.setText(mBinding.tvRoseNum, bean.getAwardNum() + "玫瑰");

        startObservable();
    }


    private Subscription mSubscription;

    private void startObservable() {
        mSubscription = Observable.interval(3000, 3000, TimeUnit.MILLISECONDS)
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
