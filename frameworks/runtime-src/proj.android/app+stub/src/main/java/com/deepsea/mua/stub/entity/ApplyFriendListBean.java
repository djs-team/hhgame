package com.deepsea.mua.stub.entity;

import java.util.List;

public class ApplyFriendListBean {

    private List<ApplyFriendBean> list;
    private PageinfoBean pageInfo;

    public PageinfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageinfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ApplyFriendBean> getList() {
        return list;
    }

    public void setList(List<ApplyFriendBean> list) {
        this.list = list;
    }

    public static class PageinfoBean {
        private int page;
        private int pageNum;
        private int totalPage;

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
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


}
