package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogGuardBayWindowBinding;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 */
public class GuardBayWindowDialog extends BaseDialog<DialogGuardBayWindowBinding> {

    public GuardBayWindowDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_bay_window;
    }

    @Override
    protected int getAnimationResId() {
        return com.deepsea.mua.stub.R.style.dialog_guard_style;
    }

    @Override
    protected float getDimAmount() {
        return 0F;
    }

    public void setMsg(JoinUser joinUser) {
        ViewBindUtils.setText(mBinding.tvLevel, "LV"+joinUser.getUserLevel());
        ViewBindUtils.setText(mBinding.tvGuardSign, String.valueOf(joinUser.getGuardSign()));
        ViewBindUtils.setText(mBinding.tvUserName, joinUser.getName());
        GlideUtils.circleImage(mBinding.avatarIv, joinUser.getAvatar(), R.drawable.icon_guard_place, R.drawable.icon_guard_place);
    }

    public void setAutoDismiss() {
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
