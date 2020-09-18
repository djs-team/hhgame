package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.socket.receive.ShowGuardAnimationToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateGuardSignToClientParam;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogGuardSuccessBinding;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/10/18
 */
public class GuardSuccessDialog extends BaseDialog<DialogGuardSuccessBinding> {


    public GuardSuccessDialog(@NonNull Context context) {
        super(context,android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_success;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }

    @Override
    public boolean isHeightFullScree() {
        return true;
    }

    @Override
    protected float getDimAmount() {
        return 0.5f;
    }

    @Override
    protected void initListener() {

    }


    public void setData(ShowGuardAnimationToClientParam bean) {
        GlideUtils.loadGif(mBinding.ivBg,R.drawable. bg_guard_success);
        GlideUtils.circleImage(mBinding.ivHeartUser,bean.getUserImage(),R.drawable.ic_place,R.drawable.ic_place);
        GlideUtils.circleImage(mBinding.ivHostUser,bean.getTargetImage(),R.drawable.ic_place,R.drawable.ic_place);
        ViewBindUtils.setText(mBinding.tvDesc,"恭喜“"+bean.getUserName()+"”成为“"+bean.getTargetName()+"”的守护");
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
