package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomModes {

    private List<RoomModeBean> room_mode;

    public List<RoomModeBean> getRoom_mode() {
        return room_mode;
    }

    public void setRoom_mode(List<RoomModeBean> room_mode) {
        this.room_mode = room_mode;
    }

    public static class RoomModeBean {
        /**
         * mode_id : 1
         * room_mode : 热门
         * room_type : 2
         * status : 1
         */

        private String mode_id;
        private String room_mode;
        private String room_type;
        private String status;

        public String getMode_id() {
            return mode_id;
        }

        public void setMode_id(String mode_id) {
            this.mode_id = mode_id;
        }

        public String getRoom_mode() {
            return room_mode;
        }

        public void setRoom_mode(String room_mode) {
            this.room_mode = room_mode;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
