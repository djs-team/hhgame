package com.deepsea.mua.stub.entity;

public class BlockVo {

    /**
     * userid : 1001644
     * sex : 2
     * nickname : Sz.夏夏
     * avatar : /Public/Uploads/user/2019-07-06/5d20a24750ddd.jpg
     * intro : null
     */

    private String userid;
    private String sex;
    private String nickname;
    private String avatar;
    private String intro;
    private String createtime;

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }
}
