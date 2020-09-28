package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivitySettingBindPhoneBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class BindPhoneActivity extends BaseActivity<ActivitySettingBindPhoneBinding> {
    @Inject
    ViewModelFactory mViewModelFactory;
    private ProfileViewModel mViewModel;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_bind_phone;
    }


    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel.class);
    }


    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.tvConfirm, o -> {
            if (TextUtils.isEmpty(mBinding.etPhoneNum.getText().toString())) {
                toastShort("请输入手机号");
                return;
            }
            if (mBinding.etPhoneNum.getText().toString().length() < 11) {
                toastShort("手机号长度是11位");
                return;
            }
            if (TextUtils.isEmpty(mBinding.etPhoneNum.getText().toString())) {
                toastShort("请输入验证码");
                return;
            }

            fetchSubmit();
        });
        subscribeClick(mBinding.tvSendCode, o -> {
            sendPcode();
        });

    }

    /**
     * 提交
     */
    private void fetchSubmit() {
        mViewModel.bindPhone(mBinding.etPhoneNum.getText().toString(), mBinding.etPhonePcode.getText().toString())
                .observe(this, new BaseObserver<BaseApiResult>() {
                    @Override
                    public void onError(String msg, int code) {
                        toastShort(msg);
                    }

                    @Override
                    public void onSuccess(BaseApiResult result) {
                        if (result.getCode() == 200) {
                            UserUtils.getUser().setUsername(mBinding.etPhoneNum.getText().toString().trim());
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
    }

    /**
     * 发送验证码
     */
    private void sendPcode() {
        mViewModel.sendSMS(mBinding.etPhoneNum.getText().toString(), "1").observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result != null) {
                    toastShort(result.getDesc());
                }
            }
        });
        mViewModel.initSendCaptchaTv(mBinding.tvSendCode);
        mBinding.etPhonePcode.setFocusable(true);
        mBinding.etPhonePcode.setFocusableInTouchMode(true);
        mBinding.etPhonePcode.requestFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mViewModel.cancelTimer();
    }

}
