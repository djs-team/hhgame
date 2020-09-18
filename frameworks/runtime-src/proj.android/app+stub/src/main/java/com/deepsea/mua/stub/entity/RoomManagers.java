package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/8
 */
public class RoomManagers {

    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * room_id : 5
         * user_id : 10000
         * avatar : http://192.168.1.210/Public/Uploads/2019-03-25/5c9881337cb48.png
         * nickname : 李 E
         * is_manager : 1
         */

        private String room_id;
        private String user_id;
        private String avatar;
        private String nickname;
        private String is_manager;
        private String pretty_id;
        private String pretty_avatar;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getIs_manager() {
            return is_manager;
        }

        public void setIs_manager(String is_manager) {
            this.is_manager = is_manager;
        }

        public String getPretty_id() {
            return pretty_id;
        }

        public void setPretty_id(String pretty_id) {
            this.pretty_id = pretty_id;
        }

        public String getPretty_avatar() {
            return pretty_avatar;
        }

        public void setPretty_avatar(String pretty_avatar) {
            this.pretty_avatar = pretty_avatar;
        }
    }
}
