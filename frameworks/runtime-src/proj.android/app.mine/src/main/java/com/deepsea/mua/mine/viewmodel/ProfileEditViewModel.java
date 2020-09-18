package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.ProfileEditRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.OSSConfigBean;
import com.deepsea.mua.stub.entity.ProfileModel;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/16
 */
public class ProfileEditViewModel extends ViewModel {

    private final ProfileEditRepository mRepository;

    @Inject
    public ProfileEditViewModel(ProfileEditRepository editRepository) {
        mRepository = editRepository;
    }

    public LiveData<Resource<BaseApiResult>> uploadAvatar(String avatar) {
        return mRepository.uploadAvatar(avatar, SignatureUtils.signByToken());
    }

    public LiveData<Resource<OSSConfigBean>> getOssConfig() {
        return mRepository.getOssConfig();
    }


    public LiveData<Resource<BaseApiResult>> setavatar(String avatar) {
        return mRepository.setavatar(avatar);
    }

    public LiveData<Resource<ProfileModel>> getMenusList() {
        return mRepository.getMenusList();
    }

    public LiveData<Resource<List<ProfileModel.ProfileMenu>>> getConditionList() {
        return mRepository.getConditionList();
    }

    /**
     * 修改用户信息
     *
     * @param info
     * @return
     */
    public LiveData<Resource<BaseApiResult>> updateInfo(String info) {
        return mRepository.updateUserInfo(info, SignatureUtils.signByToken());
    }
}
