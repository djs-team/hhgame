package com.deepsea.mua.mine.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.mine.repository.BlindDateRepository;
import com.deepsea.mua.mine.repository.TaskProcessRepository;
import com.deepsea.mua.stub.entity.BlindDateBean;
import com.deepsea.mua.stub.entity.TaskProgressBean;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class TaskProcessModel extends ViewModel {

    private final TaskProcessRepository mRepository;

    @Inject
    public TaskProcessModel(TaskProcessRepository repository) {
        mRepository = repository;
    }


    public LiveData<Resource<TaskProgressBean>> fetchInfo() {
        return mRepository.fetchInfo();
    }


}
