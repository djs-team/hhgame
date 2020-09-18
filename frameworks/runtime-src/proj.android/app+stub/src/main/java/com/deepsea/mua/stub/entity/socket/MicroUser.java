package com.deepsea.mua.stub.entity.socket;

/**
 * Created by JUN on 2019/4/20
 */
public class MicroUser {
    private WsUser User;
    private String Sex;
    private int Level;
    private int UserIdentity;//0是普通身份，1是管理员，2是房主，3是超级管理员
    private boolean IsOnMicro;
    private boolean IsDisableMsg;
    private boolean IsDisabledMicro;
    private boolean IsAttention;
    private int MsgId;
    private int Success;
    //麦位信息
    private int type;
    private int number;

    private boolean IsBlock;

    public boolean isBlock() {
        return IsBlock;
    }

    public void setBlock(boolean block) {
        IsBlock = block;
    }

    public WsUser getUser() {
        return User;
    }

    public void setUser(WsUser User) {
        this.User = User;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public int getUserIdentity() {
        return UserIdentity;
    }

    public void setUserIdentity(int UserIdentity) {
        this.UserIdentity = UserIdentity;
    }

    public boolean isIsOnMicro() {
        return IsOnMicro;
    }

    public void setIsOnMicro(boolean IsOnMicro) {
        this.IsOnMicro = IsOnMicro;
    }

    public boolean isIsDisableMsg() {
        return IsDisableMsg;
    }

    public void setIsDisableMsg(boolean IsDisableMsg) {
        this.IsDisableMsg = IsDisableMsg;
    }

    public boolean isIsDisabledMicro() {
        return IsDisabledMicro;
    }

    public void setIsDisabledMicro(boolean IsDisabledMicro) {
        this.IsDisabledMicro = IsDisabledMicro;
    }

    public boolean isIsAttention() {
        return IsAttention;
    }

    public void setIsAttention(boolean IsAttention) {
        this.IsAttention = IsAttention;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int MsgId) {
        this.MsgId = MsgId;
    }

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int Success) {
        this.Success = Success;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
