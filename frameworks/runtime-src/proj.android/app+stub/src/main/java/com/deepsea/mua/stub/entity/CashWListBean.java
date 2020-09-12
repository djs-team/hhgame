package com.deepsea.mua.stub.entity;

import java.util.List;

public class CashWListBean {

    /**
     * cash_list : [{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"}]
     * pageinfo : {"page":"1","pageNum":10,"totalPage":1}
     * total_price : 240
     */
    private PageinfoBean pageinfo;
    private double total_price;
    private List<CashWListItemBean> cash_list;

    public PageinfoBean getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageinfoBean pageinfo) {
        this.pageinfo = pageinfo;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public List<CashWListItemBean> getCash_list() {
        return cash_list;
    }

    public void setCash_list(List<CashWListItemBean> cash_list) {
        this.cash_list = cash_list;
    }

    public static class PageinfoBean {
        /**
         * page : 1
         * pageNum : 10
         * totalPage : 1
         */

        private String page;
        private int pageNum;
        private int totalPage;

        public String getPage() {
            return page;
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


}
