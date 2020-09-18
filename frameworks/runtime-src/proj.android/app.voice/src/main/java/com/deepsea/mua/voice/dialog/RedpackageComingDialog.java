package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.UserRedPacket;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RedPakageResultAdapter;
import com.deepsea.mua.voice.databinding.DialogRedpackageComingBinding;
import com.deepsea.mua.voice.databinding.DialogRedpackageResultBinding;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 * 粉丝榜
 */
public class RedpackageComingDialog extends BaseDialog<DialogRedpackageComingBinding> {

    public RedpackageComingDialog(@NonNull Context context) {
        super(context);

    }

    public interface OnMyClickListener {
        void onRobClick();
    }

    private OnMyClickListener onMyClickListener;

    public void setOnMyClickListener(OnMyClickListener onMyClickListener) {
        this.onMyClickListener = onMyClickListener;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_redpackage_coming;
    }

    @Override
    protected float getWidthPercent() {
        return 0.84F;
    }


    RedPakageResultAdapter mAdapter;

    public void setMsg() {
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.rlRop, o -> {
            if (onMyClickListener != null) {
                onMyClickListener.onRobClick();
            }
            dismiss();
        });

        startObservable();


    }

    private Subscription mSubscription;
    private int defaultTime = 10;

    private void startObservable() {
        mSubscription = Observable.interval(0, 1, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> downTime());
    }

    private void downTime() {
        defaultTime--;
        ViewBindUtils.setText(mBinding.tvDownTime, defaultTime + "S");
        if (defaultTime == 0) {
            dismiss();
        }
    }


    private void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void dismiss() {
        stopObservable();
        super.dismiss();
    }
}
