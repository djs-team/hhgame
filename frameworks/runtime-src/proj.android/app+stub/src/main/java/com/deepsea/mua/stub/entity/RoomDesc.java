package com.deepsea.mua.stub.entity;

/**
 * Created by JUN on 2019/4/23
 */
public class RoomDesc {
    /**
     * room_desc : {"room_id":"6","room_name":"交友","room_desc":"","room_welcomes":"","room_type":"2"}
     * manager_count : 0
     * manager_total : 20
     * debar_count : 4
     */

    private RoomDescBean room_desc;
    private String manager_count;
    private int manager_total;
    private String debar_count;

    public RoomDescBean getRoom_desc() {
        return room_desc;
    }

    public void setRoom_desc(RoomDescBean room_desc) {
        this.room_desc = room_desc;
    }

    public String getManager_count() {
        return manager_count;
    }

    public void setManager_count(String manager_count) {
        this.manager_count = manager_count;
    }

    public int getManager_total() {
        return manager_total;
    }

    public void setManager_total(int manager_total) {
        this.manager_total = manager_total;
    }

    public String getDebar_count() {
        return debar_count;
    }

    public void setDebar_count(String debar_count) {
        this.debar_count = debar_count;
    }

    public static class RoomDescBean {
        /**
         * room_id : 6
         * room_name : 交友
         * room_desc :
         * room_welcomes :
         * room_type : 2
         */

        private String room_id;
        private String room_name;
        private String room_desc;
        private String room_welcomes;
        private String room_type;

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public String getRoom_desc() {
            return room_desc;
        }

        public void setRoom_desc(String room_desc) {
            this.room_desc = room_desc;
        }

        public String getRoom_welcomes() {
            return room_welcomes;
        }

        public void setRoom_welcomes(String room_welcomes) {
            this.room_welcomes = room_welcomes;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }
    }
}
