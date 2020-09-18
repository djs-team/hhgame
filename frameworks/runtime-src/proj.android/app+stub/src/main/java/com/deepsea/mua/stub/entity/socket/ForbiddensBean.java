package com.deepsea.mua.stub.entity.socket;

import java.util.List;

/**
 * Created by JUN on 2019/4/9
 */
public class ForbiddensBean {

    private List<DisableMsgUser> DisableMsgUsers;
    private int MsgId;
    private int Success;

    public List<DisableMsgUser> getDisableMsgUsers() {
        return DisableMsgUsers;
    }

    public void setDisableMsgUsers(List<DisableMsgUser> disableMsgUsers) {
        DisableMsgUsers = disableMsgUsers;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int success) {
        Success = success;
    }

    public static class DisableMsgUser {
        private int Sex;
        private int UserLevel;
        private int VipLevel;
        private WsUser User;

        public int getSex() {
            return Sex;
        }

        public void setSex(int sex) {
            Sex = sex;
        }

        public int getUserLevel() {
            return UserLevel;
        }

        public void setUserLevel(int userLevel) {
            UserLevel = userLevel;
        }

        public int getVipLevel() {
            return VipLevel;
        }

        public void setVipLevel(int vipLevel) {
            VipLevel = vipLevel;
        }

        public WsUser getUser() {
            return User;
        }

        public void setUser(WsUser user) {
            User = user;
        }
    }
}
