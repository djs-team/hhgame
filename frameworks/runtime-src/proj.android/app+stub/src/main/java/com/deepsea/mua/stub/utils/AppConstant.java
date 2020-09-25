package com.deepsea.mua.stub.utils;


import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;

import java.util.ArrayList;
import java.util.List;

public class AppConstant {
    private LocationVo locationVo = null;
    private boolean isRtcEngineDestroy = false;
    private String marquee = "";
    private String inviteCode;
    private String channelCode;
    private JoinRoom joinRoom = null;
    private boolean isLoginOut;

    public boolean isLoginOut() {
        return isLoginOut;
    }

    public void setLoginOut(boolean loginOut) {
        isLoginOut = loginOut;
    }

    public JoinRoom getJoinRoom() {
        return joinRoom;
    }

    public void setJoinRoom(JoinRoom joinRoom) {
        this.joinRoom = joinRoom;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getMarquee() {
        return marquee;
    }

    public void setMarquee(String marquee) {
        this.marquee = marquee;
    }

    public boolean isRtcEngineDestroy() {
        return isRtcEngineDestroy;
    }

    public void setRtcEngineDestroy(boolean rtcEngineDestroy) {
        isRtcEngineDestroy = rtcEngineDestroy;
    }

    public LocationVo getLocationVo() {
        if (locationVo == null) {
            locationVo = new LocationVo();
        }
        return locationVo;
    }

    public void setLocationVo(LocationVo locationVo) {
        this.locationVo = locationVo;
    }


    private static AppConstant mSingleton = null;

    private AppConstant() {
    }

    public static AppConstant getInstance() {
        if (mSingleton == null) {
            synchronized (AppConstant.class) {
                if (mSingleton == null) {
                    mSingleton = new AppConstant();
                }
            }
        }
        return mSingleton;
    }
}
