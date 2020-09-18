package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.ExchangeMDDetailsRepository;
import com.deepsea.mua.stub.entity.ExchangeMdDetailListBean;
import com.deepsea.mua.stub.entity.IncomeListBean;

import javax.inject.Inject;

/**
 *
 */
public class ExchangeMdDetailsModel extends ViewModel {

    private final ExchangeMDDetailsRepository mRepository;
    public int pageNumber;
    public int size=10;

    @Inject
    public ExchangeMdDetailsModel(ExchangeMDDetailsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<ExchangeMdDetailListBean>> refresh(String stime, String etime,boolean isRedpackage) {
        pageNumber = 1;
        return mRepository.getExchangeMDDetailsList(pageNumber, size, stime, etime,isRedpackage);
    }
    public LiveData<Resource<ExchangeMdDetailListBean>> loadMore(String stime, String etime,boolean isRedpackage) {
        pageNumber++;
        return mRepository.getExchangeMDDetailsList(pageNumber, size, stime, etime,isRedpackage);
    }
}
