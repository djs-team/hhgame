package com.deepsea.mua.voice.utils;


import com.android.beauty.faceunity.entity.FaceBean;
import com.deepsea.mua.stub.entity.LocationVo;
import com.deepsea.mua.stub.entity.socket.receive.JoinRoomMsg;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;

public class AppConstant {
    private boolean isFirstRun=false;
    private FaceBean faceBean=null;
    private LocationVo locationVo=null;
    private int tabType=0;
    private int microCost;

    public int getMicroCost() {
        return microCost;
    }

    public void setMicroCost(int microCost) {
        this.microCost = microCost;
    }

    public int getTabType() {
        return tabType;
    }

    public void setTabType(int tabType) {
        this.tabType = tabType;
    }

    public LocationVo getLocationVo() {
        return locationVo;
    }

    public void setLocationVo(LocationVo locationVo) {
        this.locationVo = locationVo;
    }

    public FaceBean getFaceBean() {
        return faceBean;
    }

    public void setFaceBean(FaceBean faceBean) {
        this.faceBean = faceBean;
    }

    public boolean isFirstRun() {
        return isFirstRun;
    }

    public void setFirstRun(boolean firstRun) {
        isFirstRun = firstRun;
    }

    public static AppConstant getmSingleton() {
        return mSingleton;
    }

    public static void setmSingleton(AppConstant mSingleton) {
        AppConstant.mSingleton = mSingleton;
    }

    private static AppConstant mSingleton = null;
    private AppConstant() {}

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
