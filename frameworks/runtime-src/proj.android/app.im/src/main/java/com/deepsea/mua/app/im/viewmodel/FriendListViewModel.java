package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.FriendListRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.MessageNumVo;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class FriendListViewModel extends ViewModel {

    private final FriendListRepository mRepository;

    public int pageNumber;

    @Inject
    public FriendListViewModel(FriendListRepository repository) {
        mRepository = repository;
    }

    /**
     * 好友列表
     *
     * @return
     */
    public LiveData<Resource<FriendInfoListBean>> getFriendList() {
        return mRepository.getFriendList();
    }
    /**
     * 未读消息数量
     *
     * @return
     */
    public LiveData<Resource<MessageNumVo>> getMessageNum() {
        return mRepository.getMessageNum();
    }
}
