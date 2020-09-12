package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.VisitorsRepository;
import com.deepsea.mua.stub.entity.VisitorBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class VisitorsViewModel extends ViewModel {

    private final VisitorsRepository mRepository;

    public int pageNumber;

    @Inject
    public VisitorsViewModel(VisitorsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<VisitorBean>> refresh() {
        pageNumber = 1;
        return mRepository.visitorMember(pageNumber, SignatureUtils.signWith(pageNumber + ""));
    }

    public LiveData<Resource<VisitorBean>> loadMore() {
        pageNumber++;
        return mRepository.visitorMember(pageNumber, SignatureUtils.signWith(pageNumber + ""));
    }
}
