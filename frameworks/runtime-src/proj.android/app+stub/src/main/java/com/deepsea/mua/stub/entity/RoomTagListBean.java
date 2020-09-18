package com.deepsea.mua.stub.entity;

import java.util.List;

public class RoomTagListBean {

    private List<ModeListBean> mode_list;
    private List<RoomConfBean> room_conf;
    private List<RoomFuncBean> room_func;

    public List<ModeListBean> getMode_list() {
        return mode_list;
    }

    public void setMode_list(List<ModeListBean> mode_list) {
        this.mode_list = mode_list;
    }

    public List<RoomConfBean> getRoom_conf() {
        return room_conf;
    }

    public void setRoom_conf(List<RoomConfBean> room_conf) {
        this.room_conf = room_conf;
    }

    public List<RoomFuncBean> getRoom_func() {
        return room_func;
    }

    public void setRoom_func(List<RoomFuncBean> room_func) {
        this.room_func = room_func;
    }

    public static class ModeListBean {
        /**
         * mode_id : 5
         * room_mode : 相亲房
         * room_type : 2
         * mode_tags : [{"tags_id":"6","tag_name":"相亲"}]
         */

        private String mode_id;
        private String room_mode;
        private String room_type;
        private List<ModeTagsBean> mode_tags;

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

        public List<ModeTagsBean> getMode_tags() {
            return mode_tags;
        }

        public void setMode_tags(List<ModeTagsBean> mode_tags) {
            this.mode_tags = mode_tags;
        }

        public static class ModeTagsBean {
            /**
             * tags_id : 6
             * tag_name : 相亲
             */

            private String tags_id;
            private String tag_name;

            public String getTags_id() {
                return tags_id;
            }

            public void setTags_id(String tags_id) {
                this.tags_id = tags_id;
            }

            public String getTag_name() {
                return tag_name;
            }

            public void setTag_name(String tag_name) {
                this.tag_name = tag_name;
            }
        }
    }

    public static class RoomConfBean {
        /**
         * conf : is_exclusive_room
         * conf_name : 专属房间
         */

        private String conf;
        private String conf_name;

        public String getConf() {
            return conf;
        }

        public void setConf(String conf) {
            this.conf = conf;
        }

        public String getConf_name() {
            return conf_name;
        }

        public void setConf_name(String conf_name) {
            this.conf_name = conf_name;
        }
    }

    public static class RoomFuncBean {
        /**
         * func : is_open_red_packet
         * func_name : 红包
         */

        private String func;
        private String func_name;
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public String getFunc() {
            return func;
        }

        public void setFunc(String func) {
            this.func = func;
        }

        public String getFunc_name() {
            return func_name;
        }

        public void setFunc_name(String func_name) {
            this.func_name = func_name;
        }
    }
}
