package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.IsCreateRoomVo;
import com.deepsea.mua.stub.entity.RankList;
import com.deepsea.mua.stub.entity.RoomModes;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.HomeRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/17
 */
public class HomeViewModel extends ViewModel {

    private final HomeRepository mRepository;

    @Inject
    public HomeViewModel(HomeRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<RoomModes>> getRoomModes() {
        return mRepository.getRoomModes(SignatureUtils.signByToken());
    }

    public LiveData<Resource<RankList>> indexrank() {
        return mRepository.indexrank();
    }

    public LiveData<Resource<IsCreateRoomVo>> iscreateroom() {
        return mRepository.iscreateroom();
    }

    public LiveData<Resource<VoiceRoomBean.RoomInfoBean>> create() {

        return mRepository.create();
    }
    public LiveData<Resource<List<VoiceBanner.BannerListBean>>> getBanners() {
        return mRepository.getBanners();
    }
}
