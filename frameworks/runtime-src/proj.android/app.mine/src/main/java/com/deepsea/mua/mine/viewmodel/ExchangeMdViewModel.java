package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.ExchangeMdRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.WalletBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 */
public class ExchangeMdViewModel extends ViewModel {

    private final ExchangeMdRepository mRepository;

    @Inject
    public ExchangeMdViewModel(ExchangeMdRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<WalletBean>> initExchange(boolean isRedpackage) {
        if (isRedpackage) {
            return mRepository.redpacketInitExchange();
        } else {
            return mRepository.initExchange();
        }
    }

    public LiveData<Resource<BaseApiResult>> mdExchange(String amount,boolean isRedpackage) {
        return mRepository.mdExchange(amount,isRedpackage);
    }
}
