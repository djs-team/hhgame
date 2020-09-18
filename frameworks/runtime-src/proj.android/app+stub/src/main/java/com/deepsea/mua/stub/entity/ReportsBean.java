package com.deepsea.mua.stub.entity;

import java.util.List;

/**
 * Created by JUN on 2019/4/8
 */
public class ReportsBean {

    private List<ProblemListBean> problem_list;

    public List<ProblemListBean> getProblem_list() {
        return problem_list;
    }

    public void setProblem_list(List<ProblemListBean> problem_list) {
        this.problem_list = problem_list;
    }

    public static class ProblemListBean {
        /**
         * problem_id : 1
         * btypeid : 1
         * title : 淫秽色情
         * createtime : 1553505174
         */

        private String problem_id;
        private String btypeid;
        private String title;
        private String createtime;

        public String getProblem_id() {
            return problem_id;
        }

        public void setProblem_id(String problem_id) {
            this.problem_id = problem_id;
        }

        public String getBtypeid() {
            return btypeid;
        }

        public void setBtypeid(String btypeid) {
            this.btypeid = btypeid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }
    }
}
