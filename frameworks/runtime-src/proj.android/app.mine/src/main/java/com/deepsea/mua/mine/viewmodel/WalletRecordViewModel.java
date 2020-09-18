package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.WalletRecordRepository;
import com.deepsea.mua.stub.entity.MDRecord;
import com.deepsea.mua.stub.utils.Constant;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/6
 */
public class WalletRecordViewModel extends ViewModel {

    private final WalletRecordRepository mRepository;
    public int pageNumber;

    @Inject
    public WalletRecordViewModel(WalletRecordRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<MDRecord>> refresh(String type) {
        pageNumber = 1;
        if (Constant.MD.equals(type)) {
            return mRepository.mbdetails(pageNumber);
        }
        return mRepository.diamondtails(pageNumber);
    }

    public LiveData<Resource<MDRecord>> loadMore(String type) {
        pageNumber++;
        if (Constant.MD.equals(type)) {
            return mRepository.mbdetails(pageNumber);
        }
        return mRepository.diamondtails(pageNumber);
    }
}
