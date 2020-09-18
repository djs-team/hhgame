package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.GiftChatRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class GiftChatViewModel extends ViewModel {

    private final GiftChatRepository mRepository;


    @Inject
    public GiftChatViewModel(GiftChatRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<GiftInfoBean>> refresh() {
        return mRepository.refresh(SignatureUtils.signByToken());
    }


    /**
     * 发送礼物
     *
     * @return
     */
    public LiveData<Resource<BaseApiResult>> sendGift(Map<String, String> map) {
        return mRepository.sendGift(map);
    }
    public LiveData<Resource<BaseApiResult>> checkSatus() {
        return mRepository.checkSatus();
    }

    public LiveData<Resource<WalletBean>> wallet() {
        return mRepository.wallet();
    }
}
