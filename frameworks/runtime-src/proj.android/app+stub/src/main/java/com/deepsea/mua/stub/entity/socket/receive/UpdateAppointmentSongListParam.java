package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class UpdateAppointmentSongListParam {
    private List<SongInfo> SongInfos;
    private String Code;
    private int AllPage;
    private int SongCount;

    public int getSongCount() {
        return SongCount;
    }

    public void setSongCount(int songCount) {
        SongCount = songCount;
    }

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public List<SongInfo> getSongInfos() {
        return SongInfos;
    }

    public void setSongInfos(List<SongInfo> songInfos) {
        SongInfos = songInfos;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
