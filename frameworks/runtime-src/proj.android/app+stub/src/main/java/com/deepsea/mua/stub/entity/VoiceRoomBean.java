package com.deepsea.mua.stub.entity;

import java.io.Serializable;

/**
 * Created by JUN on 2019/4/2
 */
public class VoiceRoomBean {

    /**
     * room_info : {"room_id":"19","user_id":"10000","room_name":"测试哦","room_desc":"","room_welcomes":"","room_type":"3","room_tags":"0","room_lock":"0","room_images":"localhost.shopss.com/Public/Uploads/2019-04-01/5ca16fd04a2c6.png"}
     */

    private RoomInfoBean room_info;

    public RoomInfoBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(RoomInfoBean room_info) {
        this.room_info = room_info;
    }

    public static class RoomInfoBean implements Serializable {

        /**
         * room_id : 19
         * user_id : 10000
         * room_name : 测试哦
         * room_desc : null
         * room_welcomes : null
         * room_type : 3
         * room_tags : 0
         * room_lock : 0
         * room_images : localhost.shopss.com/Public/Uploads/2019-04-01/5ca16fd04a2c6.png
         * is_exclusive_room : 1
         * is_close_camera : 0
         * is_open_red_packet : 0
         * is_open_break_egg : 0
         * is_open_pick_song : 1
         * is_open_media_library : 0
         * is_open_video_frame : 1
         */

        private String room_id;
        private String user_id;
        private String room_name;
        private Object room_desc;
        private Object room_welcomes;
        private String room_type;
        private String room_mode;
        private String room_tags;
        private String room_lock;
        private String room_images;
        private String is_exclusive_room;
        private String is_close_camera;
        private String is_open_red_packet;
        private String is_open_break_egg;
        private String is_open_pick_song;
        private String is_open_media_library;
        private String is_open_video_frame;

        public String getRoom_mode() {
            return room_mode;
        }

        public void setRoom_mode(String room_mode) {
            this.room_mode = room_mode;
        }

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

        public String getRoom_name() {
            return room_name;
        }

        public void setRoom_name(String room_name) {
            this.room_name = room_name;
        }

        public Object getRoom_desc() {
            return room_desc;
        }

        public void setRoom_desc(Object room_desc) {
            this.room_desc = room_desc;
        }

        public Object getRoom_welcomes() {
            return room_welcomes;
        }

        public void setRoom_welcomes(Object room_welcomes) {
            this.room_welcomes = room_welcomes;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getRoom_tags() {
            return room_tags;
        }

        public void setRoom_tags(String room_tags) {
            this.room_tags = room_tags;
        }

        public String getRoom_lock() {
            return room_lock;
        }

        public void setRoom_lock(String room_lock) {
            this.room_lock = room_lock;
        }

        public String getRoom_images() {
            return room_images;
        }

        public void setRoom_images(String room_images) {
            this.room_images = room_images;
        }

        public String getIs_exclusive_room() {
            return is_exclusive_room;
        }

        public void setIs_exclusive_room(String is_exclusive_room) {
            this.is_exclusive_room = is_exclusive_room;
        }

        public String getIs_close_camera() {
            return is_close_camera;
        }

        public void setIs_close_camera(String is_close_camera) {
            this.is_close_camera = is_close_camera;
        }

        public String getIs_open_red_packet() {
            return is_open_red_packet;
        }

        public void setIs_open_red_packet(String is_open_red_packet) {
            this.is_open_red_packet = is_open_red_packet;
        }

        public String getIs_open_break_egg() {
            return is_open_break_egg;
        }

        public void setIs_open_break_egg(String is_open_break_egg) {
            this.is_open_break_egg = is_open_break_egg;
        }

        public String getIs_open_pick_song() {
            return is_open_pick_song;
        }

        public void setIs_open_pick_song(String is_open_pick_song) {
            this.is_open_pick_song = is_open_pick_song;
        }

        public String getIs_open_media_library() {
            return is_open_media_library;
        }

        public void setIs_open_media_library(String is_open_media_library) {
            this.is_open_media_library = is_open_media_library;
        }

        public String getIs_open_video_frame() {
            return is_open_video_frame;
        }

        public void setIs_open_video_frame(String is_open_video_frame) {
            this.is_open_video_frame = is_open_video_frame;
        }
    }
}
