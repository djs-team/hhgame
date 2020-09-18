package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.InviteAlertRepository;
import com.deepsea.mua.stub.entity.InviteAlertMemberBean;

import javax.inject.Inject;

import retrofit2.http.Query;

/**
 * Created by JUN on 2019/5/7
 */
public class InviteAlertModel extends ViewModel {

    private final InviteAlertRepository mRepository;

    @Inject
    public InviteAlertModel(InviteAlertRepository repository) {
        mRepository = repository;
    }


    public LiveData<Resource<InviteAlertMemberBean>> fetchInfo(String code) {
        return mRepository.fetchInfo(code);
    }


}
