package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomsBean {

    /**
     * banner_list : [{"banner_id":"1","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"www.baidu.com","type":"1"},{"banner_id":"4","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"5","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"6","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"7","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"8","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"9","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"},{"banner_id":"10","image":"pic3.nipic.com/20090527/1242397_102231006_2.jpg","linkurl":"","type":"1"}]
     * room_list : [{"rooms_id":"10","visitor_number":"565003","room_name":"测试","room_image":"localhost.shopss.com/Public/Uploads/2019-03-26/5c99d853b1a3e.png","room_type":"4","room_tags":"男神","nickname":null},{"rooms_id":"9","visitor_number":"60000","room_name":"陪玩","room_image":"localhost.shopss.com/Public/Uploads/2019-03-26/5c99d853b1a3e.png","room_type":"3","room_tags":"男神","nickname":null},{"rooms_id":"4","visitor_number":"60000","room_name":null,"room_image":"localhost.shopss.com","room_type":null,"room_tags":"男神","nickname":null},{"rooms_id":"5","visitor_number":"7030","room_name":"交友交友","room_image":"localhost.shopss.com/Public/Uploads/2019-03-26/5c99d07f4e1e1.png","room_type":"2","room_tags":"男神","nickname":null},{"rooms_id":"2","visitor_number":"5000","room_name":null,"room_image":"localhost.shopss.com","room_type":null,"room_tags":"男神","nickname":null},{"rooms_id":"11","visitor_number":"3800","room_name":"我来了","room_image":"localhost.shopss.com/Public/Uploads/2019-03-25/5c988fbc1756d.png","room_type":"3","room_tags":"男神","nickname":null},{"rooms_id":"1","visitor_number":"1000","room_name":null,"room_image":"localhost.shopss.com","room_type":null,"room_tags":"男神","nickname":null},{"rooms_id":"6","visitor_number":"502","room_name":"交友","room_image":"localhost.shopss.com/Public/Uploads/2019-03-25/5c988fbc1756d.png","room_type":"2","room_tags":"男神","nickname":null},{"rooms_id":"3","visitor_number":"10","room_name":"一起来吧!","room_image":"localhost.shopss.com/Public/Uploads/2019-03-25/5c9881337cb48.png","room_type":"2","room_tags":"男神","nickname":null},{"rooms_id":"8","visitor_number":"5","room_name":"上热门","room_image":"localhost.shopss.com/Public/Uploads/2019-03-26/5c99d0e8c7b53.png","room_type":"2","room_tags":"男神","nickname":null}]
     * pageInfo : {"page":"1","pageNum":10,"totalPage":1}
     */

    private PageInfoBean pageInfo;
    private List<BannerListBean> banner_list;
    private List<HomeInfo.RoomBean> room_list;

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<BannerListBean> getBanner_list() {
        return banner_list;
    }

    public void setBanner_list(List<BannerListBean> banner_list) {
        this.banner_list = banner_list;
    }

    public List<HomeInfo.RoomBean> getRoom_list() {
        return room_list;
    }

    public void setRoom_list(List<HomeInfo.RoomBean> room_list) {
        this.room_list = room_list;
    }

    public static class PageInfoBean {
        /**
         * page : 1
         * pageNum : 10
         * totalPage : 1
         */

        private String page;
        private int pageNum;
        private int totalPage;

        public int getPage() {
            return Integer.valueOf(page);
        }

        public void setPage(String page) {
            this.page = page;
        }

        public int getPageNum() {
            return pageNum;
        }

        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }
    }

    public static class BannerListBean {
        /**
         * banner_id : 1
         * image : pic3.nipic.com/20090527/1242397_102231006_2.jpg
         * linkurl : www.baidu.com
         * type : 1
         */

        private String banner_id;
        private String image;
        private String linkurl;
        private String type;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
