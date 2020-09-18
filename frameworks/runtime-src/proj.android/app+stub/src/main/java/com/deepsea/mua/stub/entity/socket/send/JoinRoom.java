package com.deepsea.mua.stub.entity.socket.send;

import com.deepsea.mua.stub.entity.socket.BaseMsg;

/**
 * Created by JUN on 2019/8/22
 */
public class JoinRoom extends BaseMsg {
    private String RoomId;
    private String RoomName;
    private boolean IsExclusiveRoom;// 是否是专属房
    private int RoomMode;// 房间类型（1为相亲房，2为交友房，3为七人房，4为8人房，9为双人房）
    private boolean IsCloseCamera;//是否允许音频上麦
    private boolean IsOpenRedPacket;// 是否开启红包
    private boolean IsOpenBreakEgg;// 是否开启砸蛋
    private boolean IsOpenPickSong;//  是否开启点歌
    private boolean IsOpenMediaLibrary;//  是否开启媒体库
    private boolean IsOpenVideoFrame;//  是否开启视频框

    public int getRoomMode() {
        return RoomMode;
    }

    public void setRoomMode(int roomMode) {
        RoomMode = roomMode;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public boolean isExclusiveRoom() {
        return IsExclusiveRoom;
    }

    public void setExclusiveRoom(boolean exclusiveRoom) {
        IsExclusiveRoom = exclusiveRoom;
    }


    public boolean isCloseCamera() {
        return IsCloseCamera;
    }

    public void setCloseCamera(boolean closeCamera) {
        IsCloseCamera = closeCamera;
    }

    public boolean isOpenRedPacket() {
        return IsOpenRedPacket;
    }

    public void setOpenRedPacket(boolean openRedPacket) {
        IsOpenRedPacket = openRedPacket;
    }

    public boolean isOpenBreakEgg() {
        return IsOpenBreakEgg;
    }

    public void setOpenBreakEgg(boolean openBreakEgg) {
        IsOpenBreakEgg = openBreakEgg;
    }

    public boolean isOpenPickSong() {
        return IsOpenPickSong;
    }

    public void setOpenPickSong(boolean openPickSong) {
        IsOpenPickSong = openPickSong;
    }

    public boolean isOpenMediaLibrary() {
        return IsOpenMediaLibrary;
    }

    public void setOpenMediaLibrary(boolean openMediaLibrary) {
        IsOpenMediaLibrary = openMediaLibrary;
    }

    public boolean isOpenVideoFrame() {
        return IsOpenVideoFrame;
    }

    public void setOpenVideoFrame(boolean openVideoFrame) {
        IsOpenVideoFrame = openVideoFrame;
    }

    public String getRoomId() {
        return RoomId;
    }

    public void setRoomId(String roomId) {
        RoomId = roomId;
    }
}
