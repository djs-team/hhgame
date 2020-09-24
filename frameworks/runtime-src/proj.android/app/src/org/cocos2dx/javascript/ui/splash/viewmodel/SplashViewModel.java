package org.cocos2dx.javascript.ui.splash.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.client.hyphenate.HyphenateClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.ChessLoginParam;

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
     * @return
     */
    public void loginHx(String uid) {
        HyphenateClient.getInstance().login(uid, uid, null);
    }
}
