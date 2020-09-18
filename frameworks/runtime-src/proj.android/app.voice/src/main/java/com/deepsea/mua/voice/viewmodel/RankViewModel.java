package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.FansRankBean;
import com.deepsea.mua.stub.entity.TotalRank;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RankRepository;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/30
 */
public class RankViewModel extends ViewModel {

    private final RankRepository mRankRepository;

    @Inject
    public RankViewModel(RankRepository rankRepository) {
        mRankRepository = rankRepository;
    }

    public LiveData<Resource<List<TotalRank>>> totalRanks(String type, String status) {
        return mRankRepository.ranklist(type, status, "");
    }

    public LiveData<Resource<List<FansRankBean>>> fansRanks(String status) {
        return mRankRepository.fansRanks(SignatureUtils.signByToken(), status);
    }

    public boolean ranklist(String type, String status, int page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 59);
        jsonObject.addProperty("Type", type);
        jsonObject.addProperty("State", status);
        jsonObject.addProperty("Page", page);
        return RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }
}
