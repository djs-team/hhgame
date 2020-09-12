package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.GiftKnapsackRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class GiftKnapsackModel extends ViewModel {

    private final GiftKnapsackRepository mRepository;

    @Inject
    public GiftKnapsackModel(GiftKnapsackRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<List<PackBean>>> getMePacks() {
        return mRepository.getMePacks(SignatureUtils.signByToken());
    }
    /**
     * 加好友请求
     *
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> addFriendly(Map<String, String> map) {
        return mRepository.addFriendly(map);
    }
}
