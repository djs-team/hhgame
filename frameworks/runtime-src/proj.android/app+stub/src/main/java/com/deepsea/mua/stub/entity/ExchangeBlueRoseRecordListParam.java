package com.deepsea.mua.stub.entity;

import com.deepsea.mua.stub.entity.socket.receive.BreakEggRecord;

import java.util.List;

public class ExchangeBlueRoseRecordListParam {
    private PageinfoBean pageinfo;
    private List<ExchangeBuleRoseVo> list;

    public PageinfoBean getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageinfoBean pageinfo) {
        this.pageinfo = pageinfo;
    }

    public List<ExchangeBuleRoseVo> getList() {
        return list;
    }

    public void setList(List<ExchangeBuleRoseVo> list) {
        this.list = list;
    }

    public static class PageinfoBean {
        /**
         * page : 1
         * pageNum : 10
         * totalPage : 1
         */

        private String page;
        private String pageNum;
        private int totalPage;

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageNum() {
            return pageNum;
        }

        public void setPageNum(String pageNum) {
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
