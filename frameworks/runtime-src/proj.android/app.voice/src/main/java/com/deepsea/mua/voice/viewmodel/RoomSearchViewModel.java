package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.voice.repository.RoomSearchRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/19
 */
public class RoomSearchViewModel extends ViewModel {

    private final RoomSearchRepository mRepository;

    @Inject
    public RoomSearchViewModel(RoomSearchRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<RoomSearchs>> visited() {
        return mRepository.visited();
    }

    public LiveData<Resource<RoomSearchs>> getmoremsg(String is_more) {
        return mRepository.getmoremsg(is_more);
    }

    public LiveData<Resource<RoomSearchs>> roomSearch(String search, String type) {
        return mRepository.roomSearch(search, type);
    }
}
