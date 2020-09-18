package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.AddFriendRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.WalletBean;

import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class AddFriendViewModel extends ViewModel {

    private final AddFriendRepository mRepository;

    public int pageNumber;

    @Inject
    public AddFriendViewModel(AddFriendRepository repository) {
        mRepository = repository;
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
    public LiveData<Resource<BaseApiResult>> checkSatus() {
        return mRepository.checkSatus();
    }

    public LiveData<Resource<WalletBean>> wallet() {
        return mRepository.wallet();
    }
}
