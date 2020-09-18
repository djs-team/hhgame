package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/5/6
 */
public class MDRecord {

    private List<WalletRecord> money_record;
    private PageInfoBean pageInfo;

    public List<WalletRecord> getMoney_record() {
        return money_record;
    }

    public void setMoney_record(List<WalletRecord> money_record) {
        this.money_record = money_record;
    }

    public PageInfoBean getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfoBean pageInfo) {
        this.pageInfo = pageInfo;
    }

    public static class PageInfoBean {
        /**
         * page : 1
         * pageNum : 5
         * totalPage : 0
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
