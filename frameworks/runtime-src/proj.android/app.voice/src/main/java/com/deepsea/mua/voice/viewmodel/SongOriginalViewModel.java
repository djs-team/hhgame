package com.deepsea.mua.voice.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.deepsea.mua.core.network.resource.Resource;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.RankListResult;
import com.deepsea.mua.voice.repository.SongRepository;
import com.google.gson.JsonObject;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/8
 */
public class SongOriginalViewModel extends ViewModel {

    //    private final RoomSetRepository roomSetRepository;
    private final SongRepository mRepository;

    public int pageNumber;
    public int num = 10;

    //    @Inject
//    public NearbyViewModel(NearbyRepository repository) {
//        mRepository = repository;
//    }
    @Inject
    public SongOriginalViewModel(SongRepository songRepository) {
        this.mRepository = songRepository;
    }

//    public LiveData<Resource<RoomDesc>> roomDesc(String room_id) {
//        return roomSetRepository.roomDesc(room_id, SignatureUtils.signWith(room_id));
//    }

    /**
     * 获取原唱/伴唱列表
     *
     * @param SongMode
     */
    public void getSongListParam(int SongMode, int Page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 89);
        jsonObject.addProperty("SongMode", SongMode);
        jsonObject.addProperty("Page", Page);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());


    }

    /**
     * 预约
     *
     * @param SongId
     * @param Level
     * @param Number
     */
    public void demandSongParam(int SongId, int Level, int Number) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 90);
        jsonObject.addProperty("SongId", SongId);
        if (Level != -1) {
            jsonObject.addProperty("Level", Level);
        }
        if (Number != -1) {
            jsonObject.addProperty("Number", Number);
        }
        Log.d("AG_EX_AV", jsonObject.toString());
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 置顶 预约
     *
     * @param
     */
    public void StickSongParam(String Id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 92);
        jsonObject.addProperty("Id", Id);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 切歌
     *
     * @param
     */
    public void changeSongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 93);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 删除 预约
     *
     * @param
     */
    public void deleteSongParam(String Id) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 95);
        jsonObject.addProperty("Id", Id);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 获取 预约列表
     *
     * @param
     */
    public void getAppointmentSongListParam(int Page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 96);
        jsonObject.addProperty("Page", Page);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 正在播放参数
     *
     * @param
     */
    public void getPlaySongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 97);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 搜索
     *
     * @param
     */
    public void searchSongParam(int SongMode, String SearchName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 98);
        jsonObject.addProperty("SongMode", SongMode);
        jsonObject.addProperty("SearchName", SearchName);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 同步嘉宾唱歌并且下载歌曲
     *
     * @param
     */
    public void startPlaySongParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 99);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 103变音量，106同步音量
     *
     * @param
     */
    public void sysnSongVoice(int Volume) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 103);
        jsonObject.addProperty("Volume", Volume);
        Log.d("sysnSongVoice", "103发送:" + jsonObject.toString());

        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 104重唱，107同步重唱
     *
     * @param
     */
    public void sysnSongRePlay() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 104);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 102暂停，105同步暂停
     *
     * @param
     */
    public void sysnSongPause(boolean isPause) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 102);
        jsonObject.addProperty("IsPause", isPause);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    /**
     * 109是你请求的界面按钮数据
     *
     * @param
     */
    public void syncUpdateSongWindowParam() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 109);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

    public LiveData<Resource<RankListResult>> rankRefresh(int type, int date, String roomId) {
        pageNumber = 1;
        return mRepository.rankingList(pageNumber, type, date, roomId);
    }

    public LiveData<Resource<RankListResult>> rankLoadMore(int type, int date, String roomId) {
        pageNumber++;
        return mRepository.rankingList(pageNumber, type, date, roomId);
    }
    /**
     * 获取 预约列表
     *
     * @param
     */
    public void getRoomRanksParam(int Page) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("MsgId", 118);
        jsonObject.addProperty("Page", Page);
        RoomController.getInstance().sendRoomMsg(jsonObject.toString());
    }

}
