package com.deepsea.mua.stub.entity.socket.receive;

/**
 * Created by JUN on 2019/8/22
 */
public class JoinRoomMsg extends BaseReMsg {
    private String HuanxinRoomId;
    private String ShengwangRoomId;
    private int BanTime;
    private int RoomId;
    private String Code;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getRoomId() {
        return RoomId;
    }

    public void setRoomId(int roomId) {
        RoomId = roomId;
    }




    public int getBanTime() {
        return BanTime;
    }

    public void setBanTime(int banTime) {
        BanTime = banTime;
    }

    public String getHuanxinRoomId() {
        return HuanxinRoomId;
    }

    public void setHuanxinRoomId(String HuanxinRoomId) {
        this.HuanxinRoomId = HuanxinRoomId;
    }

    public String getShengwangRoomId() {
        return ShengwangRoomId;
    }

    public void setShengwangRoomId(String ShengwangRoomId) {
        this.ShengwangRoomId = ShengwangRoomId;
    }
}
