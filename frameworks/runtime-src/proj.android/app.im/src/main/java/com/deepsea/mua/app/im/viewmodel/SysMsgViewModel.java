package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.SysMsgRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.SystemMsgListBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SysMsgViewModel extends ViewModel {

    private final SysMsgRepository mRepository;
    public int pageNumber;

    @Inject
    public SysMsgViewModel(SysMsgRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<SystemMsgListBean>> refresh() {
        pageNumber = 1;
        return mRepository.getSystemMsgList();
    }

}
