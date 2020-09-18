package com.deepsea.mua.stub.entity;

public class RankVo {

    /**
     * id : 10375
     * nickname : 啧啧
     * avatar : http://face-test-01.oss-cn-beijing.aliyuncs.com//Avatar/register/1578970887789.jpg
     * city : 北京市
     * sex : 1
     * birthday : 1997-01-01 00:00:00
     * blue_rose : 200
     * age : 23
     */

    private String id;
    private String nickname;
    private String avatar;
    private String city;
    private String sex;
    private String birthday;
    private long num;
    private int age;
    private String song_name;
    private String singer_name;
    private long coin;
    private String blue_rose;

    public String getSong_name() {
        return song_name;
    }

    public void setSong_name(String song_name) {
        this.song_name = song_name;
    }

    public String getSinger_name() {
        return singer_name;
    }

    public void setSinger_name(String singer_name) {
        this.singer_name = singer_name;
    }

    public long getCoin() {
        return coin;
    }

    public void setCoin(long coin) {
        this.coin = coin;
    }

    public String getBlue_rose() {
        return blue_rose;
    }

    public void setBlue_rose(String blue_rose) {
        this.blue_rose = blue_rose;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
