package com.deepsea.mua.stub.entity.socket;

import java.util.List;

/**
 * Created by JUN on 2019/4/25
 */
public class OnlineUser {

    private List<UserBasis> OnlineUsers;
    private int Sex;

    public List<UserBasis> getOnlineUsers() {
        return OnlineUsers;
    }

    public void setOnlineUsers(List<UserBasis> onlineUsers) {
        OnlineUsers = onlineUsers;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public static class UserBasis {
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
