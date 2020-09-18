package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.SafetyRepository;
import com.deepsea.mua.stub.data.BaseApiResult;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/15
 */
public class SafetyViewModel extends ViewModel {

    private final SafetyRepository mRepository;

    @Inject
    public SafetyViewModel(SafetyRepository repository) {
        mRepository = repository;
    }

    /*
    青少年模式-------------------------------
     */

    public LiveData<Resource<BaseApiResult>> checkMoStatus() {
        return mRepository.checkMoStatus();
    }

    public LiveData<Resource<BaseApiResult>> checkMoPwd(String pwd) {
        return mRepository.checkMoPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> upCheckMoPwd(String pwd) {
        return mRepository.upCheckMoPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> closeMonitoring(String pwd) {
        return mRepository.closeMonitoring(pwd);
    }

    public LiveData<Resource<BaseApiResult>> setChildPwd(String pwd) {
        return mRepository.setChildPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> upChildPwd(String pwd) {
        return mRepository.upChildPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> startTeenagers(String pwd) {
        return mRepository.startTeenagers(pwd);
    }

    /*
    家长模式------------------------------------
     */
    public LiveData<Resource<BaseApiResult>> checkParStatus() {
        return mRepository.checkParStatus();
    }

    public LiveData<Resource<BaseApiResult>> checkPaPwd(String pwd) {
        return mRepository.checkPaPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> upCheckPaPwd(String pwd) {
        return mRepository.upCheckPaPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> closeParent(String pwd) {
        return mRepository.closeParent(pwd);
    }

    public LiveData<Resource<BaseApiResult>> setParentPwd(String pwd) {
        return mRepository.setParentPwd(pwd);
    }

    public LiveData<Resource<BaseApiResult>> upParentPwd(String pwd) {
        return mRepository.upParentPwd(pwd);
    }
}
