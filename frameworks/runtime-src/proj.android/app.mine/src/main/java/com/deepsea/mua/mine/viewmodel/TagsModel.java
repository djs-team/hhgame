package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.TagsRepository;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.TagBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class TagsModel extends ViewModel {

    private final TagsRepository mRepository;

    @Inject
    public TagsModel(TagsRepository repository) {
        mRepository = repository;
    }


    public LiveData<Resource<TagBean>> fetchInfo() {
        return mRepository.fetchInfo();
    }

    public LiveData<Resource<BaseApiResult>> edit(String hobby_id, String feature_id) {
        return mRepository.edit(hobby_id, feature_id);
    }

}
