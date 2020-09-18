package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.activity.CancellationActivity;
import com.deepsea.mua.mine.repository.CancellationAccountRepository;
import com.deepsea.mua.mine.repository.SettingRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class CancellationAccountModel extends ViewModel {

    private final CancellationAccountRepository mRepository;

    @Inject
    public CancellationAccountModel(CancellationAccountRepository repository) {
        mRepository = repository;
    }


    public LiveData<Resource<BaseApiResult>> cancell() {
        return mRepository.cancell();
    }


}
