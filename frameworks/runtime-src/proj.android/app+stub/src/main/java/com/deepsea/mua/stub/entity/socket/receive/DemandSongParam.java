package com.deepsea.mua.stub.entity.socket.receive;

public class DemandSongParam {
    private SongInfo SongInfo;
    private int SongId;// 歌曲Id
    private int Level;// 麦位级别
    private int Number;// 麦位值

    public int getSongId() {
        return SongId;
    }

    public void setSongId(int songId) {
        SongId = songId;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public com.deepsea.mua.stub.entity.socket.receive.SongInfo getSongInfo() {
        return SongInfo;
    }

    public void setSongInfo(com.deepsea.mua.stub.entity.socket.receive.SongInfo songInfo) {
        SongInfo = songInfo;
    }

}
