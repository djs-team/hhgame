package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogSingingAlertBinding;
import com.deepsea.mua.voice.databinding.DialogSongChooseAlertBinding;

/**
 * 点歌提示
 * Created by JUN on 2018/9/27
 */
public class SongSingingAlertDialog extends BaseDialog<DialogSingingAlertBinding> {

    public SongSingingAlertDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.ivConfirm, o -> {
            dismiss();
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_singing_alert;
    }

    @Override
    protected float getWidthPercent() {
        return 0.76F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public void setMsg(String content) {
        ViewBindUtils.setText(mBinding.tvContent, content);
    }
}
