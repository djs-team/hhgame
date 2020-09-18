package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.UserRedPacket;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RedPakageResultAdapter;
import com.deepsea.mua.voice.databinding.DialogRedpackageResultBinding;
import com.deepsea.mua.voice.databinding.DialogRedpakageRuleBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2018/9/27
 * 粉丝榜
 */
public class RedpackageResultDialog extends BaseDialog<DialogRedpackageResultBinding> {

    public RedpackageResultDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_redpackage_result;
    }

    @Override
    protected float getWidthPercent() {
        return 0.84F;
    }


    RedPakageResultAdapter mAdapter;

    public void setMsg(List<UserRedPacket> redPackets) {
        mAdapter = new RedPakageResultAdapter(mContext);
        mBinding.rvRedpackageRecord.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvRedpackageRecord.setAdapter(mAdapter);
        mAdapter.setNewData(redPackets);
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });
        startObservable();

    }

    private Subscription mSubscription;
    private int defaultTime = 10;

    public void startObservable() {
        mSubscription = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> downTime());
    }

    private void downTime() {
        defaultTime--;
        ViewBindUtils.setText(mBinding.tvDownTime, defaultTime + "S");
        if (defaultTime == 0) {
            dismiss();
        }
    }


    public void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        stopObservable();
    }
}
