package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.RoomDesc;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RoomSetRepository;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomSetViewModel extends ViewModel {

    private final RoomSetRepository roomSetRepository;

    @Inject
    public RoomSetViewModel(RoomSetRepository roomSetRepository) {
        this.roomSetRepository = roomSetRepository;
    }

    public LiveData<Resource<RoomDesc>> roomDesc(String room_id) {
        return roomSetRepository.roomDesc(room_id, SignatureUtils.signWith(room_id));
    }

    /**
     * 设置房间名称
     *
     * @param name
     */
    public void setRoomName(String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 49);
        jsonObject.addProperty("Name", name);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
        Log.d("AG_EX_AV","setRoomName"+name);
    }

    /**
     * 设置房间玩法
     *
     * @param desc
     */
    public void setRoomPlay(String desc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 51);
        jsonObject.addProperty("Desc", desc);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 设置欢迎语
     *
     * @param desc
     */
    public void setRoomWel(String desc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 34);
        jsonObject.addProperty("WelcomMsg", desc);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }
}
