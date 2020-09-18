package com.deepsea.mua.stub.entity;

import java.util.List;

public class TagBean {

    /**
     * user_arr : {"hobby":"1,2,3,5","feature":"2,4,5,8"}
     * hobbys : [{"id":"1","name":"刷快手","status":"1"},{"id":"2","name":"听音乐","status":"1"},{"id":"3","name":"厨艺达人","status":"1"},{"id":"4","name":"小说","status":"1"},{"id":"5","name":"K歌","status":"1"},{"id":"6","name":"王者荣耀","status":"1"},{"id":"7","name":"爱运动","status":"1"},{"id":"8","name":"驴友","status":"1"},{"id":"9","name":"LOL","status":"1"},{"id":"10","name":"吃货","status":"1"}]
     * features : [{"id":"1","name":"成熟稳重","status":"1"},{"id":"2","name":"有责任心","status":"1"},{"id":"3","name":"感性","status":"1"},{"id":"4","name":"性情温和","status":"1"},{"id":"5","name":"靠谱","status":"1"},{"id":"6","name":"顾家","status":"1"},{"id":"7","name":"直爽","status":"1"},{"id":"8","name":"幽默","status":"1"},{"id":"9","name":"孝顺","status":"1"},{"id":"10","name":"有上进心","status":"1"}]
     */

    private UserArrBean user_arr;
    private List<TagItem> hobbys;
    private List<TagItem> features;

    public UserArrBean getUser_arr() {
        return user_arr;
    }

    public void setUser_arr(UserArrBean user_arr) {
        this.user_arr = user_arr;
    }

    public List<TagItem> getHobbys() {
        return hobbys;
    }

    public void setHobbys(List<TagItem> hobbys) {
        this.hobbys = hobbys;
    }

    public List<TagItem> getFeatures() {
        return features;
    }

    public void setFeatures(List<TagItem> features) {
        this.features = features;
    }

    public static class UserArrBean {
        /**
         * hobby : 1,2,3,5
         * feature : 2,4,5,8
         */

        private String hobby;
        private String feature;

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getFeature() {
            return feature;
        }

        public void setFeature(String feature) {
            this.feature = feature;
        }
    }



    public static class TagItem {
        /**
         * id : 1
         * name : 成熟稳重
         * status : 1
         */

        private String id;
        private String name;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
