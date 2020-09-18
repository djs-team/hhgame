package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogRoomPlayBinding;

/**
 * Created by JUN on 2019/3/28
 * 语音直播间玩法介绍
 */
public class RoomPlayDialog extends BaseDialog<DialogRoomPlayBinding> {

    public RoomPlayDialog(@NonNull Context context) {
        super(context);
        mBinding.setDlg(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_room_play;
    }

    public void close() {
        dismiss();
    }

    @Override
    protected float getWidthPercent() {
        return 0.78F;
    }

    public void setPlayIntro(String playIntro) {
        mBinding.setPlayIntro(playIntro);
    }
}
