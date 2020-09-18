package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.CollectionAccountRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashInfo;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.WalletBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class CollectionAccountModel extends ViewModel {

    private final CollectionAccountRepository mRepository;

    @Inject
    public CollectionAccountModel(CollectionAccountRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<CashInfo>> fetchCashInfo() {
        return mRepository.fetchCashInfo();
    }

    public LiveData<Resource<BaseApiResult>> bindaplipay(String alipayAccount,String type,String justpic,String backpic) {
        return mRepository.bindaplipay(alipayAccount,type, justpic, backpic);
    }
    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return mRepository.getOssConfig();
    }
}
