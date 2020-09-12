package com.deepsea.mua.stub.entity.socket.receive;

public class SongParam {
    private int  Id;
    private String  SongName;//歌名
    private String  SongType;//歌曲类型
    private String  SongPath;//歌曲路径
    private String  LyricPath;//歌词路径
    private String  OriginalSingerName ;//原唱名
    private String  SingerName ;//歌手
    private String  Coin ;// 消耗玫瑰
    private int  SongMode ;// 歌曲类型（1为原唱，2为伴唱）
    private int  State ;// 状态（0为失效,1为正常）
    private long  CreateTime ;// 创建时间

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongType() {
        return SongType;
    }

    public void setSongType(String songType) {
        SongType = songType;
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

    public String getOriginalSingerName() {
        return OriginalSingerName;
    }

    public void setOriginalSingerName(String originalSingerName) {
        OriginalSingerName = originalSingerName;
    }

    public String getSingerName() {
        return SingerName;
    }

    public void setSingerName(String singerName) {
        SingerName = singerName;
    }

    public String getCoin() {
        return Coin;
    }

    public void setCoin(String coin) {
        Coin = coin;
    }

    public int getSongMode() {
        return SongMode;
    }

    public void setSongMode(int songMode) {
        SongMode = songMode;
    }

    public int getState() {
        return State;
    }

    public void setState(int state) {
        State = state;
    }

    public long getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(long createTime) {
        CreateTime = createTime;
    }
}
