package com.deepsea.mua.stub.entity;

/**
 * 作者：liyaxing  2019/9/5 11:45
 * 类别 ：
 */
public class PresentWallBean {


        /**
         * gift_image : http://img.57xun.com/gift/9580645fa645116b6e5538e17e92fc2e.png
         * num : 2
         * gift_name : test
         * gift_type : 1
         * gift_animation : http://img.57xun.com/gift/aed8b8503e9bcd5d10dcefab697da153.svga
         * animation : http://img.57xun.com/gift/ddb9f3bd15d7ef76ab26e60837e5f3dd.gif
         */
        public String gift_image;
        public String num;
        public String gift_name;
        public String gift_type;
        public String gift_animation;
        public String animation;

        @Override
        public String toString() {
                return "PresentWallBean{" +
                        "gift_image='" + gift_image + '\'' +
                        ", num='" + num + '\'' +
                        ", gift_name='" + gift_name + '\'' +
                        ", gift_type='" + gift_type + '\'' +
                        ", gift_animation='" + gift_animation + '\'' +
                        ", animation='" + animation + '\'' +
                        '}';
        }
}
