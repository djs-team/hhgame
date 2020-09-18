package com.deepsea.mua.stub.entity.socket;

import com.deepsea.mua.stub.entity.socket.receive.JoinUser;

/**
 * Created by JUN on 2019/4/22
 */
public class ReceiveMessage {

    private String msg;
    private String emojiurl;
    private String emojianim;
    private JoinUser user;
    private String GuardSign;

    public String getGuardSign() {
        return GuardSign;
    }

    public void setGuardSign(String guardSign) {
        GuardSign = guardSign;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getEmojiurl() {
        return emojiurl;
    }

    public void setEmojiurl(String emojiurl) {
        this.emojiurl = emojiurl;
    }

    public String getEmojianim() {
        return emojianim;
    }

    public void setEmojianim(String emojianim) {
        this.emojianim = emojianim;
    }

    public JoinUser getUser() {
        return user;
    }

    public void setUser(JoinUser user) {
        this.user = user;
    }
}
