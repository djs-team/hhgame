package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.GiftPanelRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class GiftPanelViewModel extends ViewModel {

    private final GiftPanelRepository mRepository;


    @Inject
    public GiftPanelViewModel(GiftPanelRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<GiftListBean>> refresh() {
        return mRepository.refresh(SignatureUtils.signByToken());
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
