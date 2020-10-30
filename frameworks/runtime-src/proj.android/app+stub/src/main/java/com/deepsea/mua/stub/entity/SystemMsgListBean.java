package com.deepsea.mua.stub.entity;

import java.util.List;

public class SystemMsgListBean {
    private List<SystemMsgBean> list;
    private PageInfoBean pageInfo;

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<SystemMsgBean> getList() {
        return list;
    }

    public void setList(List<SystemMsgBean> list) {
        this.list = list;
    }

    public static class PageInfoBean {
        /**
         * page : 1
         * pageNum : 20
         * totalPage : 1
         */

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
