package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/26
 */
public class EmojiBean {
    private List<EmoticonBean> Emoticon_list;

    public List<EmoticonBean> getEmoticon_list() {
        return Emoticon_list;
    }

    public void setEmoticon_list(List<EmoticonBean> Emoticon_list) {
        this.Emoticon_list = Emoticon_list;
    }

    public static class EmoticonBean {
        /**
         * face_id : 600
         * face_name : 爱心
         * face_image : /Public/Uploads/2019-03-25/5c9881337cb48.png
         * is_lock : 1
         */

        private String face_id;
        private String face_name;
        private String face_image;
        private String is_lock;
        private String animation;
        private String game_image;
        private int addtime;
        private String type;

        public String getFace_id() {
            return face_id;
        }

        public void setFace_id(String face_id) {
            this.face_id = face_id;
        }

        public String getFace_name() {
            return face_name;
        }

        public void setFace_name(String face_name) {
            this.face_name = face_name;
        }

        public String getFace_image() {
            return face_image;
        }

        public void setFace_image(String face_image) {
            this.face_image = face_image;
        }

        public String getIs_lock() {
            return is_lock;
        }

        public void setIs_lock(String is_lock) {
            this.is_lock = is_lock;
        }

        public String getAnimation() {
            return animation;
        }

        public void setAnimation(String animation) {
            this.animation = animation;
        }

        public String getGame_image() {
            return game_image;
        }

        public void setGame_image(String game_image) {
            this.game_image = game_image;
        }

        public int getAddtime() {
            return addtime;
        }

        public void setAddtime(int addtime) {
            this.addtime = addtime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
