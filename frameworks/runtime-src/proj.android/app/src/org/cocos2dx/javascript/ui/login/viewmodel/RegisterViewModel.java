package org.cocos2dx.javascript.ui.login.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.OSSConfigBean;

import org.cocos2dx.javascript.repository.RegisterRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/10/16
 */
public class RegisterViewModel extends ViewModel {

    private final RegisterRepository mRepository;

    @Inject
    public RegisterViewModel(RegisterRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return mRepository.getOssConfig();
    }

    public LiveData<Resource<User>> register(String username, String avatar, String nickname, int age, String sex, String inviteCode, String registration_id, String wx_id) {
        return mRepository.register(username, avatar, nickname, age, sex,inviteCode,registration_id,wx_id);
    }
}
