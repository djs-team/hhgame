package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityCancellationAccountBinding;
import com.deepsea.mua.mine.databinding.ActivityCodeOfConductBinding;
import com.deepsea.mua.mine.viewmodel.CancellationAccountModel;
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
 * 行为规范
 */
public class CodeOfConductActivity extends BaseActivity<ActivityCodeOfConductBinding>{
    private int timeFlag = 15;
    private CancellationAccountModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_code_of_conduct;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(CancellationAccountModel.class);

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}