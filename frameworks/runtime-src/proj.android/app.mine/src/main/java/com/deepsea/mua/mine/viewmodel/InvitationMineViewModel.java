package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.repository.FollowFanRepository;
import com.deepsea.mua.mine.repository.InvitationMineRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.FollowFanBean;
import com.deepsea.mua.stub.entity.InviteListBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class InvitationMineViewModel extends ViewModel {

    private final InvitationMineRepository mRepository;
    public int page;
    public int pageNumber=10;


    @Inject
    public InvitationMineViewModel(InvitationMineRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<InviteListBean>> refresh() {
        page = 1;
        return mRepository.getMyInviteUserList(page, pageNumber);
    }

    public LiveData<Resource<InviteListBean>> loadMore() {
        page++;
        return mRepository.getMyInviteUserList(page, pageNumber);
    }

}
