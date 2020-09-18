package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class GetSongListParam {
    private int MsgId;
    private List<SongParam> SongParams;
    private String Code;
    private int AllPage;
    private int SongMode;
    private int Page;

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public int getSongMode() {
        return SongMode;
    }

    public void setSongMode(int songMode) {
        SongMode = songMode;
    }

    public int getPage() {
        return Page;
    }

    public void setPage(int page) {
        Page = page;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public List<SongParam> getSongDatas() {
        return SongParams;
    }

    public void setSongDatas(List<SongParam> songDatas) {
        SongParams = songDatas;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
