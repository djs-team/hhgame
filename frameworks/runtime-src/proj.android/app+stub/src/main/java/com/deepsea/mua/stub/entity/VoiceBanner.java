package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/6/27
 */
public class VoiceBanner {
    private List<BannerListBean> bannerList;

    public List<BannerListBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerListBean> bannerList) {
        this.bannerList = bannerList;
    }

    public static class BannerListBean {
        /**
         * banner_id : 1
         * image : pic3.nipic.com/20090527/1242397_102231006_2.jpg
         * linkurl : www.baidu.com
         */

        private String banner_id;
        private String image;
        private String linkurl;
        private String title;
        private int imageId;
        private String link_type;
        private String ui_type;
        private boolean isLocalImage;

        public boolean isLocalImage() {
            return isLocalImage;
        }

        public void setLocalImage(boolean localImage) {
            isLocalImage = localImage;
        }

        public String getLink_type() {
            return link_type;
        }

        public void setLink_type(String link_type) {
            this.link_type = link_type;
        }

        public String getUi_type() {
            return ui_type;
        }

        public void setUi_type(String ui_type) {
            this.ui_type = ui_type;
        }

        public BannerListBean(int imageId) {
            this.imageId = imageId;
        }

        public int getImageId() {
            return imageId;
        }

        public void setImageId(int imageId) {
            this.imageId = imageId;
        }

        public String getBanner_id() {
            return banner_id;
        }

        public void setBanner_id(String banner_id) {
            this.banner_id = banner_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getLinkurl() {
            return linkurl;
        }

        public void setLinkurl(String linkurl) {
            this.linkurl = linkurl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
