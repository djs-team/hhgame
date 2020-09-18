package com.deepsea.mua.stub.entity;

import java.util.List;

public class IncomeListBean {

    /**
     * list : [{"id":"324","action":"2","initiatorId":"1000","coin":"20","recipientId":"1002","recipient_income":"0.00","introducer_income":"1.00","initiator_referrer_income":"3.00","recipient_referrer_income":"3.00","time":"2019-11-14 16:06:37","type":4,"divide":"0.00"},{"id":"323","action":"2","initiatorId":"1000","coin":"198","recipientId":"1002","recipient_income":"0.00","introducer_income":"9.00","initiator_referrer_income":"29.00","recipient_referrer_income":"29.00","time":"2019-11-14 14:43:48","type":4,"divide":"0.00"},{"id":"322","action":"2","initiatorId":"1000","coin":"15","recipientId":"1002","recipient_income":"0.00","introducer_income":"0.00","initiator_referrer_income":"2.00","recipient_referrer_income":"2.00","time":"2019-11-14 14:43:37","type":4,"divide":"0.00"},{"id":"321","action":"2","initiatorId":"1000","coin":"888","recipientId":"1002","recipient_income":"0.00","introducer_income":"0.00","initiator_referrer_income":"0.00","recipient_referrer_income":"0.00","time":"2019-11-14 14:37:28","type":4,"divide":"0.00"},{"id":"320","action":"2","initiatorId":"1000","coin":"15","recipientId":"1002","recipient_income":"0.00","introducer_income":"0.00","initiator_referrer_income":"0.00","recipient_referrer_income":"0.00","time":"2019-11-14 14:25:15","type":4,"divide":"0.00"}]
     * pageinfo : {"page":"1","pageNum":"10","totalPage":1}
     * total_price : 0
     */

    private PageinfoBean pageinfo;
    private double total_price;
    private List<IncomeListItemBean> list;

    public PageinfoBean getPageinfo() {
        return pageinfo;
    }

    public void setPageinfo(PageinfoBean pageinfo) {
        this.pageinfo = pageinfo;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public List<IncomeListItemBean> getList() {
        return list;
    }

    public void setList(List<IncomeListItemBean> list) {
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

/**
 * cash_list : [{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"},{"cashid":"6","cash":"100","status":"1","cashtime":"2019-04-19 17:18:43","type":"1","apliuserid":"15855847758","cash_price":"10","cash_tax":"50","real_cash":"40"}]
 * pageinfo : {"page":"1","pageNum":10,"totalPage":1}
 * total_price : 240
 */




