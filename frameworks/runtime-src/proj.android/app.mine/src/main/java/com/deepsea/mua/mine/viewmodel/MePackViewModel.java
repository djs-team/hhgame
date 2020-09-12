package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.MePackRepository;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.utils.SignatureUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/7/24
 */
public class MePackViewModel extends ViewModel {

    private final MePackRepository mRepository;

    @Inject
    public MePackViewModel(MePackRepository repository) {
        mRepository = repository;
    }

    public LiveData<Resource<List<PackBean>>> getMePacks() {
        return mRepository.getMePacks(SignatureUtils.signByToken());
    }
}
