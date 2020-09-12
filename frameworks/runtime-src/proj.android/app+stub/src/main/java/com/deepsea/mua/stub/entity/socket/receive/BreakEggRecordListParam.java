package com.deepsea.mua.stub.entity.socket.receive;

import java.util.List;

public class BreakEggRecordListParam {
    private int MsgId;
    private List<BreakEggRecord> BreakEggRecords;
    private String Code;
    private int AllPage;

    public int getAllPage() {
        return AllPage;
    }

    public void setAllPage(int allPage) {
        AllPage = allPage;
    }

    public int getMsgId() {
        return MsgId;
    }

    public void setMsgId(int msgId) {
        MsgId = msgId;
    }

    public List<BreakEggRecord> getBreakEggRecords() {
        return BreakEggRecords;
    }

    public void setBreakEggRecords(List<BreakEggRecord> breakEggRecords) {
        BreakEggRecords = breakEggRecords;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

}
