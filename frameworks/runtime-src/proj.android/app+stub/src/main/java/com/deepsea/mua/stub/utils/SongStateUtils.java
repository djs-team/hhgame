package com.deepsea.mua.stub.utils;

import com.deepsea.mua.stub.entity.LocalVoiceReverbPresetVo;

import java.util.ArrayList;
import java.util.List;

import static io.agora.rtc.Constants.AUDIO_REVERB_KTV;

public class SongStateUtils {
    // 指向自己实例的私有静态引用
    private static SongStateUtils singleton2;

    // 私有的构造方法
    private SongStateUtils() {
    }

    private int currentPos;
    private boolean isConnect;
    private int SongState;
    private int downloadState = -1;//3 下载  4 下载完成 5 下载失败
    private boolean isChangeView;
    private String consertUserId = "";
    private int heartCount;
    private int volume=30;
    private boolean hintPlay=false;//播放提示

    public boolean isHintPlay() {
        return hintPlay;
    }

    public void setHintPlay(boolean hintPlay) {
        this.hintPlay = hintPlay;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public void setHeartCount(int heartCount) {
        this.heartCount = heartCount;
    }

    public String getConsertUserId() {
        return consertUserId;
    }

    public void setConsertUserId(String consertUserId) {
        this.consertUserId = consertUserId;
    }

    public void resetSongStateUtils() {
        singleton2 = null;
    }

    public boolean isChangeView() {
        return isChangeView;
    }

    public void setChangeView(boolean changeView) {
        isChangeView = changeView;
    }

    public int getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    public int getSongState() {
        return SongState;
    }

    public void setSongState(int songState) {
        SongState = songState;
    }

    public boolean isConnect() {
        return isConnect;
    }

    public void setConnect(boolean connect) {
        isConnect = connect;
    }

    public int getCurrentPos() {
        return currentPos;
    }

    public void setCurrentPos(int currentPos) {
        this.currentPos = currentPos;
    }

    // 以自己实例为返回值的静态的公有方法，静态工厂方法
    public static SongStateUtils getSingleton2() {
        // 被动创建，在真正需要使用时才去创建
        if (singleton2 == null) {
            singleton2 = new SongStateUtils();
        }
        return singleton2;
    }
    public void  reset(){
        consertUserId="";
        singleton2=null;
    }

    private List<LocalVoiceReverbPresetVo> reverbPresetVoList = new ArrayList<>();

    public List<LocalVoiceReverbPresetVo> getReverbPresetVoList() {
        if (reverbPresetVoList == null || reverbPresetVoList.size() == 0) {
            initReverbPresetList();
        }
        return reverbPresetVoList;
    }

    public void setReverbPresetVoList(List<LocalVoiceReverbPresetVo> reverbPresetVoList) {
        this.reverbPresetVoList = reverbPresetVoList;
    }

    public void initReverbPresetList() {
        reverbPresetVoList.clear();
        LocalVoiceReverbPresetVo Popular = new LocalVoiceReverbPresetVo(80, 76, 64, 1, -1);
        LocalVoiceReverbPresetVo rb = new LocalVoiceReverbPresetVo(80, 76, 44, 1, -1);
        LocalVoiceReverbPresetVo rock = new LocalVoiceReverbPresetVo(95, 170, 58, -7, -7);
        LocalVoiceReverbPresetVo hipHop = new LocalVoiceReverbPresetVo(96, 24, 70, -4, -1);
        LocalVoiceReverbPresetVo vocal_concert = new LocalVoiceReverbPresetVo(48, 57, 68, 2, 1);
        LocalVoiceReverbPresetVo ktv = new LocalVoiceReverbPresetVo(62, 76, 69, -1, -1);
        LocalVoiceReverbPresetVo studio = new LocalVoiceReverbPresetVo(26, 0, 50, 0, 4);
        reverbPresetVoList.add(Popular);
        reverbPresetVoList.add(rb);
        reverbPresetVoList.add(rock);
        reverbPresetVoList.add(hipHop);
        reverbPresetVoList.add(vocal_concert);
        reverbPresetVoList.add(ktv);
        reverbPresetVoList.add(studio);
    }

    private int selectEffect = AUDIO_REVERB_KTV;

    public int getSelectEffect() {
        return selectEffect;
    }

    public void setSelectEffect(int selectEffect) {
        this.selectEffect = selectEffect;
    }

    public void resetReverbPresetList() {
        reverbPresetVoList.clear();
    }

}
