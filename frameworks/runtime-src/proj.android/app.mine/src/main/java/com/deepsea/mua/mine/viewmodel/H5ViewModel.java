package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.H5Repository;
import com.deepsea.mua.mine.repository.ProfileRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.AuthenticationBean;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/12
 */
public class H5ViewModel extends ViewModel {

    private final H5Repository repository;

    @Inject
    public H5ViewModel(H5Repository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<AuditBean>> getVerifyToken() {
        String signature = SignatureUtils.signByToken();
        return repository.getVerifyToken(signature);
    }

    public LiveData<Resource<BaseApiResult>> createapprove() {
        String signature = SignatureUtils.signByToken();
        return repository.createapprove(signature);
    }
}
