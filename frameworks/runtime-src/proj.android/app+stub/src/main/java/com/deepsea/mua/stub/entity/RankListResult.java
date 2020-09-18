package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * 作者：liyaxing  2019/8/23 19:15
 * 类别 ：
 */
public class RankListResult {
        /**
         * list : [{"id":"1043","birthday":"2001-01-01 00:00:00","is_online":"1","nickname":"张三","avatar":"http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/register/1573872046643.jpg","sex":"1","register_time":"2019-11-16 10:45:12","age":18}]
         * pageInfo : {"page":"1","pageNum":"1","totalPage":1764}
         */

        private PageInfoBean pageInfo;
        private List<RankVo> list;

        public PageInfoBean getPageInfo() {
            return pageInfo;
        }

        public void setPageInfo(PageInfoBean pageInfo) {
            this.pageInfo = pageInfo;
        }

    public List<RankVo> getList() {
        return list;
    }

    public void setList(List<RankVo> list) {
        this.list = list;
    }

    public static class PageInfoBean {
            /**
             * page : 1
             * pageNum : 1
             * totalPage : 1764
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

