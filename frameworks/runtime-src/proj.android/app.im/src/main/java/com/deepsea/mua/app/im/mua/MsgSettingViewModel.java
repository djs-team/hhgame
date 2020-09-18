package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.core.network.response.ApiResponse;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CheckBlackVo;
import com.deepsea.mua.stub.network.HttpCallback;
import com.deepsea.mua.stub.network.HttpUtils;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/9/12
 */
public class MsgSettingViewModel extends ViewModel {

    private final MsgSettingRepository mRepository;

    @Inject
    public MsgSettingViewModel(MsgSettingRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<BaseApiResult>> fansmenusatatus(String type) {
        return mRepository.fansmenusatatus(type);
    }

    public LiveData<Resource<BaseApiResult>> defriend(String uid) {
        return mRepository.defriend(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<BaseApiResult>> blackout(String uid) {
        return mRepository.blackout(uid, SignatureUtils.signWith(uid));
    }

    public LiveData<Resource<CheckBlackVo>> isBlack(String uid) {
        return mRepository.isBlack(uid);
    }

}
