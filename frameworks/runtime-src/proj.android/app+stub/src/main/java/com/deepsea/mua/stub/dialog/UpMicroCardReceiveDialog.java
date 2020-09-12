package com.deepsea.mua.stub.dialog;

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
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAlertBinding;
import com.deepsea.mua.stub.databinding.DialogUpmicroCardReceiveBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * @zu
 */
public class UpMicroCardReceiveDialog extends BaseDialog<DialogUpmicroCardReceiveBinding> {

    public interface OnClickListener {
        /**
         * 回调
         */
        void onDismiss();

        void onConfirm();
    }

    private OnClickListener onClickListener;


    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public UpMicroCardReceiveDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            if (onClickListener != null) {
                onClickListener.onConfirm();
            }
            dismiss();
        });
    }


    @Override
    public void dismiss() {
        if (onClickListener != null) {
            onClickListener.onDismiss();
        }
        super.dismiss();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog__upmicro_card_receive;
    }

    @Override
    protected float getWidthPercent() {
        return 0.8F;
    }

    @Override
    protected float getDimAmount() {
        return 0.4F;
    }


}
