package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.RoomManagers;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.voice.repository.RoomManagerRepository;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomManagerViewModel extends ViewModel {

    private final RoomManagerRepository repository;

    @Inject
    public RoomManagerViewModel(RoomManagerRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<RoomManagers.ListBean>>> getManagers(String room_id) {
        return repository.getManagers(room_id, SignatureUtils.signWith(room_id));
    }

    public LiveData<Resource<List<RoomManagers.ListBean>>> searchManagers(String room_id, String search) {
        return repository.searchManagers(room_id, search, SignatureUtils.signWith(room_id));
    }

    /**
     * 设置管理员
     *
     * @param isManager
     * @param uid
     */
    public boolean setRoomManager(boolean isManager, String uid) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 31);
        jsonObject.addProperty("IsManager", isManager);
        jsonObject.addProperty("UserId", uid);
        return RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }
}
