package com.deepsea.mua.stub.entity;

/**
 * Created by JUN on 2019/8/27
 */
public class DynamicMsgExt extends CustomMsgExt {
    private DynamicLisistBean.ListEntity forum;
    private DynamicDetailReplylistBean.ListEntity reply;
    private DynamicDetailReplylistBean.ListEntity addReply;

    public DynamicLisistBean.ListEntity getForum() {
        return forum;
    }

    public void setForum(DynamicLisistBean.ListEntity forum) {
        this.forum = forum;
    }

    public DynamicDetailReplylistBean.ListEntity getReply() {
        return reply;
    }

    public void setReply(DynamicDetailReplylistBean.ListEntity reply) {
        this.reply = reply;
    }

    public DynamicDetailReplylistBean.ListEntity getAddReply() {
        return addReply;
    }

    public void setAddReply(DynamicDetailReplylistBean.ListEntity addReply) {
        this.addReply = addReply;
    }
}
