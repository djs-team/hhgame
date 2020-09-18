package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.IncomeDetailsRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.CashWListBean;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.IncomeListBean;
import com.deepsea.mua.stub.entity.IncomeListItemBean;

import javax.inject.Inject;

/**
 *
 */
public class IncomeDetailsModel extends ViewModel {

    private final IncomeDetailsRepository mRepository;
    public int pageNumber;
    public int size = 10;

    @Inject
    public IncomeDetailsModel(IncomeDetailsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<IncomeListBean>> refresh(String stime, String etime) {
        pageNumber = 1;
        return mRepository.getIncomeList(pageNumber, size, stime, etime);
    }

    public LiveData<Resource<IncomeListBean>> loadMore(String stime, String etime) {
        pageNumber++;
        return mRepository.getIncomeList(pageNumber, size, stime, etime);
    }

    public LiveData<Resource<IncomeListBean>> redPackageRefresh(String stime, String etime) {
        pageNumber = 1;
        return mRepository.getRedPackageIncomeList(pageNumber, size, stime, etime);
    }

    public LiveData<Resource<IncomeListBean>> redPackageLoadMore(String stime, String etime) {
        pageNumber++;
        return mRepository.getRedPackageIncomeList(pageNumber, size, stime, etime);
    }
}
