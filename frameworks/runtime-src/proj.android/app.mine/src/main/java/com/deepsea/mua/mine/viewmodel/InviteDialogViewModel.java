package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.InviteDialogRepository;
import com.deepsea.mua.mine.repository.WalletRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.InitCash;
import com.deepsea.mua.stub.entity.WalletBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class InviteDialogViewModel extends ViewModel {

    private final InviteDialogRepository mRepository;

    @Inject
    public InviteDialogViewModel(InviteDialogRepository repository) {
        mRepository = repository;
    }

    /**
     * 接口名称: 邀请同意拒绝接口
     *
     * @param inviterId
     * @param inviteeId
     * @param roomId
     * @param status
     * @param id
     * @return
     */
    public LiveData<Resource<BaseApiResult>> inviteHandle(int inviterId, int inviteeId, int roomId, int status, int id) {
        return mRepository.inviteHandle(inviterId, inviteeId, roomId, status, id);
    }

}
