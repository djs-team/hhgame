package com.deepsea.mua.app.im.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.app.im.repository.FriendRequestRepository;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.ApplyFriendListBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author zu 2019 11 5
 */
public class FriendRequestViewModel extends ViewModel {

    private final FriendRequestRepository mRepository;

    public int pageNumber;

    @Inject
    public FriendRequestViewModel(FriendRequestRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<ApplyFriendListBean>> refresh(String type) {
        pageNumber = 1;
        if (type.equals("1")) {
            return mRepository.getApplyFriendList(SignatureUtils.signByToken(), pageNumber);
        }else {
            return mRepository.getMyApplyList(SignatureUtils.signByToken(), pageNumber);

        }
    }

    public LiveData<Resource<ApplyFriendListBean>> loadMore(String type) {
        pageNumber++;
        if (type.equals("1")) {
            return mRepository.getApplyFriendList(SignatureUtils.signByToken(), pageNumber);
        }else {
            return mRepository.getMyApplyList(SignatureUtils.signByToken(), pageNumber);

        }
    }

    /**
     * 同意好友请求
     *
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> passFriendly(Map<String, String> map, String isFriend) {
        if (isFriend.equals("2")) {
            return mRepository.passFriendly(map);
        } else {
            return mRepository.agreeFriendly(map);
        }
    }

    /**
     * 拒绝好友请求
     *
     * @param map
     * @return
     */
    public LiveData<Resource<BaseApiResult>> delFriendly(Map<String, String> map) {
        return mRepository.delFriendly(map);
    }
}
