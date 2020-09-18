package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.CashWithdrawalDetailsRepository;
import com.deepsea.mua.stub.entity.CashWListBean;

import javax.inject.Inject;

/**
 *
 */
public class CashWithdrawalDetailsModel extends ViewModel {

    private final CashWithdrawalDetailsRepository mRepository;
    public int pageNumber;
    private int size = 10;

    @Inject
    public CashWithdrawalDetailsModel(CashWithdrawalDetailsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<CashWListBean>> refresh(String stime, String etime, boolean isRedpackage) {
        pageNumber = 1;
        return mRepository.getCashList(pageNumber, size, stime, etime, isRedpackage);
    }

    public LiveData<Resource<CashWListBean>> loadMore(String stime, String etime, boolean isRedpackage) {
        pageNumber++;
        return mRepository.getCashList(pageNumber, size, stime, etime, isRedpackage);
    }

}
