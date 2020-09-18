package com.deepsea.mua.voice.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogShareResultBinding;
import com.deepsea.mua.voice.databinding.DialogSongAlertBinding;

/**
 * Created by JUN on 2018/9/27
 */
public class ShareResultDialog extends BaseDialog<DialogShareResultBinding> {

    public ShareResultDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            dismiss();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_share_result;
    }

    @Override
    protected float getWidthPercent() {
        return 0.66F;
    }

    @Override
    protected float getDimAmount() {
        return 0.8F;
    }


}
