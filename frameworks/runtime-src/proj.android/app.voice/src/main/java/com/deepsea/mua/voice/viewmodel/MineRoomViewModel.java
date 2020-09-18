package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.MineRooms;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.MineRoomsRepository;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/19
 */
public class MineRoomViewModel extends ViewModel {

    private final MineRoomsRepository mRepository;

    @Inject
    public MineRoomViewModel(MineRoomsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<MineRooms>> myRoom() {
        return mRepository.myRoom(SignatureUtils.signByToken());
    }
}
