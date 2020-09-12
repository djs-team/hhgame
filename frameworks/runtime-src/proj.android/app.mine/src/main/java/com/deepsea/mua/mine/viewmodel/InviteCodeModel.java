package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.CashWithdrawalRepository;
import com.deepsea.mua.mine.repository.InviteCodeRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.InviteCodeBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class InviteCodeModel extends ViewModel {

    private final InviteCodeRepository mRepository;

    @Inject
    public InviteCodeModel(InviteCodeRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<InviteCodeBean>> getInviteDownUrl() {
        return mRepository.getInviteDownUrl();
    }

    public LiveData<Resource<BaseApiResult>> shareCallback() {
        return mRepository.shareCallback();
    }

}
