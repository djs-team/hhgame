package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogSongAlertBinding;

/**
 * Created by JUN on 2018/9/27
 */
public class SongAlertDialog extends BaseDialog<DialogSongAlertBinding> {

    public SongAlertDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_song_alert;
    }

    @Override
    protected float getWidthPercent() {
        return 0.9F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public void setMsg(String content) {
        ViewBindUtils.setText(mBinding.tvContent, content);
        ViewBindUtils.RxClicks(mBinding.ivConfirm, o -> {
            dismiss();

        });
    }
}
