package com.deepsea.mua.stub.model;

import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.entity.socket.RoomData;

import java.util.List;

/**
 * Created by JUN on 2019/8/22
 * 房间信息
 */
public class RoomModel {

    //房间ID
    private String roomId;
    //房间信息
    private RoomData mRoomData;
    //当前用户信息
    private JoinUser mUser;
    //当前用户是否在麦
    private boolean isOnMp;
    //当前用户是否静音
    private boolean isMute;
    //消息
    private List<RoomMsgBean> mMsgs;
    //主麦位
    private RoomData.MicroInfosBean mHostMicro;
    //主麦位
    private RoomData.MicroInfosBean mSofaMicro;
    //8麦
    private List<RoomData.MicroInfosBean> mMicros;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public RoomData getRoomData() {
        return mRoomData;
    }

    public void setRoomData(RoomData roomData) {
        mRoomData = roomData;
    }

    public JoinUser getUser() {
        return mUser;
    }

    public void setUser(JoinUser user) {
        mUser = user;
    }

    public boolean isOnMp() {
        return isOnMp;
    }

    public void setOnMp(boolean onMp) {
        isOnMp = onMp;
    }

    public boolean isMute() {
        return isMute;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public List<RoomMsgBean> getMsgs() {
        return mMsgs;
    }

    public void setMsgs(List<RoomMsgBean> msgs) {
        mMsgs = msgs;
    }

    public RoomData.MicroInfosBean getHostMicro() {
        return mHostMicro;
    }

    public void setHostMicro(RoomData.MicroInfosBean hostMicro) {
        mHostMicro = hostMicro;
    }

    public List<RoomData.MicroInfosBean> getMicros() {
        return mMicros;
    }

    public void setMicros(List<RoomData.MicroInfosBean> micros) {
        mMicros = micros;
    }

    public RoomData.MicroInfosBean getmSofaMicro() {
        return mSofaMicro;
    }

    public void setmSofaMicro(RoomData.MicroInfosBean mSofaMicro) {
        this.mSofaMicro = mSofaMicro;
    }
}
