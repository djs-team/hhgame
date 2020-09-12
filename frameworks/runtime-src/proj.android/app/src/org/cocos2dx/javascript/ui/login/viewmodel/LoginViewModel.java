package org.cocos2dx.javascript.ui.login.viewmodel;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.core.login.ApiUser;
import com.deepsea.mua.core.login.LoginApi;
import com.deepsea.mua.core.login.OnLoginListener;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.UserBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.hyphenate.EMCallBack;

import org.cocos2dx.javascript.repository.LoginRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/28
 */
public class LoginViewModel extends ViewModel {

    private static final int COUNT_DOWN_TOTAL_MILLIS = 60 * 1000;
    private static final int COUNT_DOWN_INTERVAL_MILLIS = 1000;

    private LoginRepository mRepository;

    private CountDownTimer mTimer;
    //三方回调数据
    private ApiUser openUser;

    @Inject
    public LoginViewModel(LoginRepository repository) {
        this.mRepository = repository;
    }

    public LiveData<Resource<User>> login(String account, String vertify, String registration_id) {
        return mRepository.login(account, vertify, registration_id);
    }

    public LiveData<Resource<User>> oneLogin(String registration_id, String token) {
        return mRepository.oneLogin(registration_id, token);
    }

    /**
     * 登录环信
     *
     * @param account
     * @param password
     * @return
     */
    public LiveData<Resource<Object>> loginHx(String account, String password) {
        HxHelper.getInstance().setCurrentUserName(account);
        MediatorLiveData<Resource<Object>> liveData = new MediatorLiveData<>();
        liveData.postValue(Resource.loading(null));
        HyphenateClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                loginHx(account, password, liveData);
            }

            @Override
            public void onError(int i, String s) {
                loginHx(account, password, liveData);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
        return liveData;
    }

    private void loginHx(String account, String password, MediatorLiveData<Resource<Object>> liveData) {
        HyphenateClient.getInstance().login(account, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                liveData.postValue(Resource.success(null));
            }

            @Override
            public void onError(int i, String s) {
//                liveData.postValue(Resource.error(s, null));
                liveData.postValue(Resource.error("环信登录失败", null));
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    public LiveData<Resource<BaseApiResult>> sendSMS(String phone, String type) {
        return mRepository.sendSMS(phone, type);
    }

    public LiveData<Resource<BaseApiResult>> isWxPhoneRegister(String username, String vertify) {
        return mRepository.isWxPhoneRegister(username, vertify);
    }

    public LiveData<Resource<UserBean>> thirdlogin(String platform, Activity activity, String latitude, String longitude, String state, String city, String locality, String registration_id) {
        MediatorLiveData<Resource<UserBean>> result = new MediatorLiveData<>();

        LoginApi loginApi = new LoginApi();
        loginApi.setPlatform(platform, activity);
        loginApi.setOnLoginListener(new OnLoginListener() {
            @Override
            public void onLogin(ApiUser apiUser) {
                openUser = apiUser;
                String extras = JsonConverter.toJson(apiUser.getRes());
                LiveData<Resource<UserBean>> source = mRepository.thirdlogin(apiUser.getOpenId(), latitude, longitude, state, city, locality, registration_id);
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

    public String getOpenRes() {
        if (openUser != null) {
            return JsonConverter.toJson(openUser);
        }
        return "";
    }

    public LiveData<Resource<UserBean>> bindmobile(String openid, String type, String payload, String username, String vertify) {
        return mRepository.bindmobile(openid, type, payload, username, vertify);
    }

    public LiveData<Resource<User>> removebind(String openid, String type, String payload, String username) {
        return mRepository.removebind(openid, type, payload, username);
    }

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
                    ViewBindUtils.setText(sendCaptchaTv, "获取验证码");
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
}
