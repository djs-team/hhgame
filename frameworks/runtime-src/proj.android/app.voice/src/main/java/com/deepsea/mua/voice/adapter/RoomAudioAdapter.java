package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;

import java.util.List;

/**
 * Created by JUN on 2019/8/6
 */
public class RoomAudioAdapter extends RoomMpAdapter implements IMicroEvent {

    public RoomAudioAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemPos(int type, int number) {
        List<RoomData.MicroInfosBean> data = getData();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                RoomData.MicroInfosBean bean = data.get(i);
                if (bean.getType() == type && bean.getNumber() == number) {
                    return i;
                }
            }
        }
        return 0;
    }

    public int getItemPosForUid(String uid) {
        List<RoomData.MicroInfosBean> data = getData();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                RoomData.MicroInfosBean bean = data.get(i);
                if (bean.getUser() != null && TextUtils.equals(bean.getUser().getName(), uid)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int getItemPosForMany(int type, int number) {
        List<RoomData.MicroInfosBean> data = getData();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                RoomData.MicroInfosBean bean = data.get(i);
                if (bean.getType() == type && bean.getNumber() == number) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public void onVoiceWave(int pos) {
        notifyItemChanged(pos, VoiceWave);
    }

    @Override
    public void onNormalEmojiAnima(int pos, String url) {
        getItem(pos).setAnimUrl(url);
        notifyItemChanged(pos, NormalEmojiAnima);
    }

    @Override
    public void onGameEmojiAnima(int pos, String url) {
        getItem(pos).setActionUrl(url);
        notifyItemChanged(pos, GameEmojiAnima);
    }

    @Override
    public void onGameResult(int pos, String url) {
        getItem(pos).setResultUrl(url);
        notifyItemChanged(pos, GameResult);
    }

    public void releaseLayout(int pos) {
        notifyItemChanged(pos, IMicroEvent.ReleaseLayout);
    }

    @Override
    public void onGiftAnima(int pos, String url) {
        getItem(pos).setAnimUrl(url);
        notifyItemChanged(pos, GiftAnima);
    }

    @Override
    public void onMicroUserChanged(int pos, WsUser user) {
        notifyItemChanged(pos, MicroUserChanged);
    }

    @Override
    public void onCountDown(int pos, String countDown, int duration) {
        notifyItemChanged(pos, CountDown);
    }

    @Override
    public void onLockMicro(int pos, boolean lock) {
        notifyItemChanged(pos, LockMicro);
    }

    @Override
    public void onForbiddenMicro(int pos, boolean forbidden) {
        notifyItemChanged(pos, ForbiddenMicro);
    }

    @Override
    public void onHeartValue(int pos, int heartValue) {
        notifyItemChanged(pos, HeartValue);
    }

    //    @Override
    public void onUpdateMicro(RoomData.MicroInfosBean bean) {
        if (bean == null)
            return;
        int pos = getItemPos(bean.getType(), bean.getNumber());
        notifyItemChanged(pos);
    }

    @Override
    public void updateFriendState(int pos, RoomData.MicroInfosBean bean) {
        if (bean == null)
            return;
        notifyItemChanged(pos, AddFriendState);
    }

    @Override
    public void updateRanks(int pos, List<String> ranks) {
        notifyItemChanged(pos, UpdateRanks);
    }

    @Override
    public void upDateMpMicro(int level, int num, boolean isOpenMp) {
        int pos = getItemPos(level, num);
        getItem(pos).setOpenMp(isOpenMp);
        notifyItemChanged(pos, UpMpMicro);

    }


}
