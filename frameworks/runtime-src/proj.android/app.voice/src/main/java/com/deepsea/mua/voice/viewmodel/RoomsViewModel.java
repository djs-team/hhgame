package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.RoomsBean;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RoomsRepository;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomsViewModel extends ViewModel {

    private final RoomsRepository mRepository;
    public int pageNumber;

    @Inject
    public RoomsViewModel(RoomsRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<RoomsBean>> refresh(String room_type,String age,String city,String city_two,String city_three) {
        pageNumber = 1;
        return mRepository.getRooms(room_type, pageNumber, SignatureUtils.signWith(room_type),age,city,city_two,city_three);
    }

    public LiveData<Resource<RoomsBean>> loadMore(String room_type,String age,String city,String city_two,String city_three) {
        pageNumber++;
        return mRepository.getRooms(room_type, pageNumber, SignatureUtils.signWith(room_type),age,city,city_two,city_three);
    }
    public LiveData<Resource<List<VoiceBanner.BannerListBean>>> getBanners(int type) {
        return mRepository.getBanners( type);
    }
}
