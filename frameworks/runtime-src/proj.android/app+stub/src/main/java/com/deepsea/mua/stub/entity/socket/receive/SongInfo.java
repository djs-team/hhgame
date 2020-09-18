package com.deepsea.mua.stub.entity.socket.receive;

public class SongInfo {
    private String Id;
    private String SongName;//歌曲名称
    private String SingerName;// 原唱歌手
    private String SongPath;// 歌曲路径
    private String LyricPath ;// 歌词路径
    private String DemandUserName ;// 点播用户
    private String DemandUserId;
    private String ConsertUserName ;// 演唱用户
    private String ConsertUserId ;// 演唱用户id

    public String getDemandUserId() {
        return DemandUserId;
    }

    public void setDemandUserId(String demandUserId) {
        DemandUserId = demandUserId;
    }

    public String getConsertUserId() {
        return ConsertUserId;
    }

    public void setConsertUserId(String consertUserId) {
        ConsertUserId = consertUserId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSingerName() {
        return SingerName;
    }

    public void setSingerName(String singerName) {
        SingerName = singerName;
    }

    public String getSongPath() {
        return SongPath;
    }

    public void setSongPath(String songPath) {
        SongPath = songPath;
    }

    public String getLyricPath() {
        return LyricPath;
    }

    public void setLyricPath(String lyricPath) {
        LyricPath = lyricPath;
    }

    public String getDemandUserName() {
        return DemandUserName;
    }

    public void setDemandUserName(String demandUserName) {
        DemandUserName = demandUserName;
    }

    public String getConsertUserName() {
        return ConsertUserName;
    }

    public void setConsertUserName(String consertUserName) {
        ConsertUserName = consertUserName;
    }
}
