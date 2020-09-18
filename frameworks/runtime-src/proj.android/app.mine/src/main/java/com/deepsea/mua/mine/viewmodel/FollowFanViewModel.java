package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.FollowFanRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FollowFanBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class FollowFanViewModel extends ViewModel {

    private final FollowFanRepository mRepository;
    public int pageNumber;

    @Inject
    public FollowFanViewModel(FollowFanRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<FollowFanBean>> refresh(String type) {
        pageNumber = 1;
        return mRepository.getFollowFans(type, pageNumber);
    }

    public LiveData<Resource<FollowFanBean>> loadMore(String type) {
        pageNumber++;
        return mRepository.getFollowFans(type, pageNumber);
    }

    public LiveData<Resource<BaseApiResult>> attention_member(String uid, String type) {
        return mRepository.attention_member(uid, type);
    }
}
