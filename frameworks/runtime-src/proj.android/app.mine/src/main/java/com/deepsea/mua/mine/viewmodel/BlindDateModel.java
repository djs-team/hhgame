package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.BlindDateRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlindDateBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class BlindDateModel extends ViewModel {

    private final BlindDateRepository mRepository;

    @Inject
    public BlindDateModel(BlindDateRepository repository) {
        mRepository = repository;
    }


    public LiveData<Resource<BlindDateBean>> fetchInfo() {
        return mRepository.fetchInfo();
    }


}
