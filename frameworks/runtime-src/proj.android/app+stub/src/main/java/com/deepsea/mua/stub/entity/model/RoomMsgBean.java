package com.deepsea.mua.stub.entity.model;

import android.text.SpannableStringBuilder;

import com.deepsea.mua.core.view.chat.BaseChatMsg;
import com.deepsea.mua.stub.entity.socket.SmashBean;

import java.util.List;

/**
 * Created by JUN on 2019/3/26
 */
public class RoomMsgBean extends BaseChatMsg {

    //(普通消息?)
    private boolean normal;
    //消息体
    private SpannableStringBuilder msg;
    private String url;

    private int start;

    //礼物数量
    private int count;

    //砸蛋消息
    private List<SmashBean> list;

    private String avatar;
    private String uid;
    private String uName;
    private int level;
    private String guardSign;
    private String localMsg;
    private int GuardState;// 0不是主持守护，1是普通守护，2是榜一守护
    private String GuardHeadImage;

    public int getGuardState() {
        return GuardState;
    }

    public void setGuardState(int guardState) {
        GuardState = guardState;
    }

    public String getGuardHeadImage() {
        return GuardHeadImage;
    }

    public void setGuardHeadImage(String guardHeadImage) {
        GuardHeadImage = guardHeadImage;
    }

    public String getLocalMsg() {
        return localMsg;
    }

    public void setLocalMsg(String localMsg) {
        this.localMsg = localMsg;
    }

    public String getGuardSign() {
        return guardSign;
    }

    public void setGuardSign(String guardSign) {
        this.guardSign = guardSign;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public SpannableStringBuilder getMsg() {
        return msg;
    }

    public void setMsg(SpannableStringBuilder msg) {
        this.msg = msg;
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SmashBean> getList() {
        return list;
    }

    public void setList(List<SmashBean> list) {
        this.list = list;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
