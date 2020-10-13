package org.cocos2dx.javascript.ui.splash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.ChessLoginParam;
import com.hyphenate.EMCallBack;

import org.cocos2dx.javascript.repository.SplashRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class SplashViewModel extends ViewModel {

    private final SplashRepository mRepository;

    @Inject
    public SplashViewModel(SplashRepository mRepository) {
        this.mRepository = mRepository;
    }

    public LiveData<Resource<User>> autologin() {
        return mRepository.autologin();
    }

    //platform 2手机 3微信
    public LiveData<Resource<User>> login(ChessLoginParam param) {

            return mRepository.login(param);

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
}
