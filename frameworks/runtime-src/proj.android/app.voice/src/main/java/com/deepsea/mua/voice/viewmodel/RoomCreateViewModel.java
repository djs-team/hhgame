package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.RoomModeHelpBean;
import com.deepsea.mua.stub.entity.RoomModeHelpVo;
import com.deepsea.mua.stub.entity.RoomTagListBean;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RoomCreateRepository;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/2
 */
public class RoomCreateViewModel extends ViewModel {

    private final RoomCreateRepository mRepository;

    @Inject
    public RoomCreateViewModel(RoomCreateRepository mRepository) {
        this.mRepository = mRepository;
    }

//    public LiveData<Resource<VoiceRoomBean.RoomInfoBean>> create(String room_name, String room_type, String room_tags) {
    public LiveData<Resource<VoiceRoomBean.RoomInfoBean>> create() {

        return mRepository.create();
    }

    public LiveData<Resource<RoomTags>> getTagsList() {
        return mRepository.getOldTagsList(SignatureUtils.signByToken());
    }

    public LiveData<Resource<RoomTagListBean>> getNewTagsList() {
        return mRepository.getTagsList(SignatureUtils.signByToken());
    }
    public LiveData<Resource<RoomModeHelpBean>> getRoomModeHelp() {
        return mRepository.getRoomTypeHelp();
    }

    /**
     * 设置tag+model
     *
     * @param tagId
     */
    public void setRoomTagAndModel(String tagId, String modleId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 43);
        jsonObject.addProperty("TagId", tagId);
        jsonObject.addProperty("ModelId", modleId);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }
}
