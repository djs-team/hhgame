package com.deepsea.mua.stub.entity;

/**
 * 作者：liyaxing  2019/8/23 19:15
 * 类别 ：
 */
public class DynamicDetailBean {


    /**
     * createtime : 1970-01-01 08:00:00
     * forum_uid : 1000978
     * forum_voice : aaa.com/a.voice
     * forum_image : http://img.muayy.comdir/1.jpg,http://img.muayy.comdir2/2.jpg
     * forum_content : aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
     * enjoy_num : 2
     * nickname : 13200000005
     * reply_num : 3
     * avatar : http://img.muayy.com/dir1/1000978_5d50c91ac7b8f.jpg
     * forum_id : 1
     * sex : 1
     * is_attention : 1
     */
    public String createtime;
    public String forum_uid;
    public String forum_voice;
    public String forum_image;
    public String forum_content;
    public String enjoy_num;
    public String nickname;
    public String reply_num;
    public String avatar;
    public String forum_id;
    public String sex;
    public String forum_voice_time;
    public int is_attention;
    public int is_enjoy;
    public int img_w;
    public int img_h;
    @Override
    public String toString() {
        return "DynamicDetailBean{" +
                "createtime='" + createtime + '\'' +
                ", forum_uid='" + forum_uid + '\'' +
                ", forum_voice='" + forum_voice + '\'' +
                ", forum_image='" + forum_image + '\'' +
                ", forum_content='" + forum_content + '\'' +
                ", enjoy_num='" + enjoy_num + '\'' +
                ", is_enjoy='" + is_enjoy + '\'' +
                ", is_attention='" + is_attention + '\'' +
                ", nickname='" + nickname + '\'' +
                ", reply_num='" + reply_num + '\'' +
                ", avatar='" + avatar + '\'' +
                ", forum_id='" + forum_id + '\'' +
                ", forum_voice_time='" + forum_voice_time + '\'' +
                '}';
    }
}
