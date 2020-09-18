package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityAssistBinding;
import com.deepsea.mua.mine.databinding.ActivityCancellationAccountBinding;
import com.deepsea.mua.mine.viewmodel.CancellationAccountModel;
import com.deepsea.mua.mine.viewmodel.SettingViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.app.AppClient;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.utils.LogoutUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by JUN on 2019/8/9
 * 注销账户
 */
public class CancellationActivity extends BaseActivity<ActivityCancellationAccountBinding> {
    private int timeFlag = 15;
    private CancellationAccountModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cancellation_account;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CancellationAccountModel.class);
        startObservable();
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.cancellationAccountTv, o -> {
            AAlertDialog dialog = new AAlertDialog(mContext);
            dialog.setMessage("确认注销账户", R.color.black, 18);
            dialog.setLeftButton("确定", R.color.light_orange, (v, dialog1) -> {
                cancell();
                dialog1.dismiss();
            });
            dialog.setRightButton("取消", R.color.light_orange, null);
            dialog.show();
        });
    }

    private void cancell() {
        AppClient.getInstance().logout(p ->
                mViewModel.cancell().observe(this, new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        /**
                         * @link {SharePDataBaseUtils # saveAgreement , SharePDataBaseUtils # delDynamicEdit }
                         */
                        SharedPrefrencesUtil.saveData(mContext, "user", "Agreement", false);
                        SharedPrefrencesUtil.deleteData(mContext, "user", "DynamicEditSaveBean");
                        p.apply();
                        LogoutUtils.logout(mContext);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        p.apply();
                        LogoutUtils.logout(mContext);
                    }
                }));
    }

    private Subscription mSubscription;

    public void startObservable() {
        mSubscription = Observable.interval(1000, 1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .subscribe(aLong -> counting());
    }

    public void stopObservable() {
        if (mSubscription != null && (!mSubscription.isUnsubscribed())) {
            mSubscription.unsubscribe();
        }
    }

    private void counting() {
        mBinding.titleBar.post(new Runnable() {
            @Override
            public void run() {
                if (timeFlag <= 0) {
                    mBinding.cancellationAccountTv.setText("申请注销");
                    ViewBindUtils.setEnable(mBinding.cancellationAccountTv, true);
                    stopObservable();
                } else {
                    mBinding.cancellationAccountTv.setText("申请注销" + timeFlag + "s");
                    timeFlag--;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        startObservable();
    }
}
