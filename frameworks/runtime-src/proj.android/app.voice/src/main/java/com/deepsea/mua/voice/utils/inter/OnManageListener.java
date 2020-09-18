package com.deepsea.mua.voice.utils.inter;


import com.deepsea.mua.stub.entity.InviteOnmicroData;

import java.util.List;

public interface OnManageListener {
    public void onTopMicro(String uid);

    public void onOnWheat(String uid);

    public void onRemove(String uid);

    public void onInviteClick(List<InviteOnmicroData> ids, int free, int type);

    public void onInviteClick(String uid, int free, int type);

    public void onSortInRoomRefreshClick();

    public void onSortInRoomLoadMoreClick();
}