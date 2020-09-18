package com.deepsea.mua.stub.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by JUN on 2019/3/29
 */
public class User {
    @SerializedName("userid")
    private String uid;
    private String username;
    private String sex;
    private String nickname;
    private String intro;
    private String status;
    private String avatar;
    private String birthday;
    private String roomnumber;
    private String firstlogin;
    private String role;
    private String token;
    private String attestation;
    private String is_matchmaker;//是否是红娘 0:不是   1:是

    //是否打开主播开播提醒 0 打开 1关闭
    private String fansmenustatus;
    //是否打开青少年模式
    private boolean isOpenYounger;
    //是否大打开家长模式
    private boolean isOpenParent;
    private int is_receive;//是否可获赠上麦卡，1:可以   2:不可以
    private int card_num;//可领取上麦卡数量
    private int is_apply_match;//是否有申请主持弹窗0:没有   1:有，申请通过   2:有，申请拒绝
    private String refuse_wx;//申请拒绝信息里的微信号
    private String refuse_time;//申请拒绝信息里的再次申请时间

    public int getIs_apply_match() {
        return is_apply_match;
    }

    public void setIs_apply_match(int is_apply_match) {
        this.is_apply_match = is_apply_match;
    }

    public String getRefuse_wx() {
        return refuse_wx;
    }

    public void setRefuse_wx(String refuse_wx) {
        this.refuse_wx = refuse_wx;
    }

    public String getRefuse_time() {
        return refuse_time;
    }

    public void setRefuse_time(String refuse_time) {
        this.refuse_time = refuse_time;
    }

    public int getIs_receive() {
        return is_receive;
    }

    public void setIs_receive(int is_receive) {
        this.is_receive = is_receive;
    }

    public int getCard_num() {
        return card_num;
    }

    public void setCard_num(int card_num) {
        this.card_num = card_num;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRoomnumber() {
        return roomnumber;
    }

    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
    }

    public String getFirstlogin() {
        return firstlogin;
    }

    public void setFirstlogin(String firstlogin) {
        this.firstlogin = firstlogin;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIs_matchmaker() {
        return is_matchmaker;
    }

    public void setIs_matchmaker(String is_matchmaker) {
        this.is_matchmaker = is_matchmaker;
    }

    public String getAttestation() {
        return attestation;
    }

    public void setAttestation(String attestation) {
        this.attestation = attestation;
    }

    public String getFansmenustatus() {
        return fansmenustatus;
    }

    public void setFansmenustatus(String fansmenustatus) {
        this.fansmenustatus = fansmenustatus;
    }

    public boolean isOpenYounger() {
        return isOpenYounger;
    }

    public void setOpenYounger(boolean openYounger) {
        isOpenYounger = openYounger;
    }

    public boolean isOpenParent() {
        return isOpenParent;
    }

    public void setOpenParent(boolean openParent) {
        isOpenParent = openParent;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", nickname='" + nickname + '\'' +
                ", intro='" + intro + '\'' +
                ", status='" + status + '\'' +
                ", avatar='" + avatar + '\'' +
                ", birthday='" + birthday + '\'' +
                ", roomnumber='" + roomnumber + '\'' +
                ", firstlogin='" + firstlogin + '\'' +
                ", role='" + role + '\'' +
                ", token='" + token + '\'' +
                ", attestation='" + attestation + '\'' +
                ", is_matchmaker='" + is_matchmaker + '\'' +
                ", fansmenustatus='" + fansmenustatus + '\'' +
                ", isOpenYounger=" + isOpenYounger +
                ", isOpenParent=" + isOpenParent +
                '}';
    }
}
