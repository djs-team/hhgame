package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.voice.repository.FullServiceSortRepository;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/8/27
 */
public class FullServiceSortModel extends ViewModel {

    private final FullServiceSortRepository mRepository;

    public int page;
    public int pagenum=10;

    @Inject
    public FullServiceSortModel(FullServiceSortRepository repository) {
        mRepository = repository;
    }

    /**
     *
     * @param sex 性别 1:男 2:女 3:保密
     * @return
     */
    public LiveData<Resource<OnlinesBean>> refresh(String sex) {
        page = 1;
        Map<String,String> map=new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("pagenum",String.valueOf(pagenum));
        map.put("is_online","1");
        if (!sex.equals("0")) {
            map.put("sex", sex);
        }
        map.put("state","0,1");
        return mRepository.OnlineList(map);
    }

    public LiveData<Resource<OnlinesBean>> loadMore(String sex) {
        page++;
        Map<String,String> map=new HashMap<>();
        map.put("page",String.valueOf(page));
        map.put("pagenum",String.valueOf(pagenum));
        map.put("is_online","1");
        if (!sex.equals("0")) {
            map.put("sex", sex);
        }
        map.put("state","0,1");
        return mRepository.OnlineList(map);
    }
}
