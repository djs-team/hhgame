package com.deepsea.mua.mine.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.repository.SettingRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class SettingViewModel extends ViewModel {

    private final SettingRepository mRepository;

    @Inject
    public SettingViewModel(SettingRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<BaseApiResult>> logout() {
        return mRepository.logout();
    }

    public LiveData<Resource<BaseApiResult>> cancell() {
        return mRepository.cancell();
    }

    public LiveData<Resource<BaseApiResult>> setFeedback(String content) {
        return mRepository.setFeedback(content, SignatureUtils.signByToken());
    }

    public LiveData<Resource<BaseApiResult>> unBindWx(String wx_id) {
        return mRepository.unBindWx(wx_id);
    }

    public LiveData<Resource<BaseApiResult>> bindPhone(String phone, String pcode) {
        return mRepository.bindPhone(phone, pcode);
    }

    public LiveData<Resource<CheckBindWx>> isBindWx() {
        return mRepository.isBindWx();
    }

    public LiveData<Resource<BaseApiResult<BindWx>>> thirdlogin(String platform, Activity activity) {
        MediatorLiveData<Resource<BaseApiResult<BindWx>>> result = new MediatorLiveData<>();

        LoginApi loginApi = new LoginApi();
        loginApi.setPlatform(platform, activity);
        loginApi.setOnLoginListener(new OnLoginListener() {
            @Override
            public void onLogin(ApiUser apiUser) {
                String extras = JsonConverter.toJson(apiUser.getRes());
                LiveData<Resource<BaseApiResult<BindWx>>> source = mRepository.bindWx(apiUser.getOpenId());
                result.addSource(source, result::postValue);

            }

            @Override
            public void onCancel() {
                result.postValue(Resource.error("取消登录", null));
            }

            @Override
            public void onError(String msg) {
                result.postValue(Resource.error(msg, null));
            }
        });
        loginApi.login();

        return result;
    }
    private CountDownTimer mTimer;
    private static final int COUNT_DOWN_TOTAL_MILLIS = 60 * 1000;
    private static final int COUNT_DOWN_INTERVAL_MILLIS = 1000;
    public void initSendCaptchaTv(final TextView sendCaptchaTv) {
        if (mTimer == null) {
            ViewBindUtils.setEnable(sendCaptchaTv, false);
            mTimer = new CountDownTimer(COUNT_DOWN_TOTAL_MILLIS, COUNT_DOWN_INTERVAL_MILLIS) {
                @Override
                public void onTick(long millisUntilFinished) {
                    ViewBindUtils.setText(sendCaptchaTv, millisUntilFinished / 1000 + "s");
                }

                @Override
                public void onFinish() {
                    ViewBindUtils.setEnable(sendCaptchaTv, true);
                    ViewBindUtils.setText(sendCaptchaTv, "验证码");
                    mTimer = null;
                }

            };
        }
        mTimer.start();
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }
    public LiveData<Resource<BaseApiResult>> sendSMS(String phone, String type) {
        return mRepository.sendSMS(phone, type);
    }

}
