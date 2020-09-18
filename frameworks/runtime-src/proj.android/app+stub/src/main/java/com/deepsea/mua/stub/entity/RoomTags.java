package com.deepsea.mua.stub.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by JUN on 2019/4/15
 */
public class RoomTags {
    private List<ModeListBean> mode_list;

    public List<ModeListBean> getMode_list() {
        return mode_list;
    }

    public void setMode_list(List<ModeListBean> mode_list) {
        this.mode_list = mode_list;
    }

    public static class TagListBean {
        /**
         * tag_id : 1
         * tag_name : 男神
         */

        @SerializedName("tags_id")
        private String tag_id;
        private String tag_name;

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getTag_name() {
            return tag_name;
        }

        public void setTag_name(String tag_name) {
            this.tag_name = tag_name;
        }
    }

    public static class ModeListBean {
        /**
         * mode_id : 1
         * room_mode : 交友
         * room_type : 1
         * mode_tags : [{"tags_id":"1","tag_name":"虚拟女友"},{"tags_id":"2","tag_name":"虚拟男友"}]
         */

        private String mode_id;
        private String room_mode;
        private String room_type;
        private List<TagListBean> mode_tags;

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

        public List<TagListBean> getMode_tags() {
            return mode_tags;
        }

        public void setMode_tags(List<TagListBean> mode_tags) {
            this.mode_tags = mode_tags;
        }
    }
}
