package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogAlertBinding;
import com.deepsea.mua.stub.databinding.DialogInviteInRoomBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2018/9/27
 */
public class InviteInRoomDialog extends BaseDialog<DialogInviteInRoomBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onDisagreeClick(View v, Dialog dialog);

        void onAgreeClick(View v, Dialog dialog);
    }

    public InviteInRoomDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.tvInviteDisagree, o -> {
            dismiss();
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_invite_in_room;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public InviteInRoomDialog setButton(String str, OnClickListener cli) {
        initButton(mBinding.tvInviteDisagree, mBinding.tvInviteAgree, cli);
        return this;
    }

    private void initButton(TextView diaAgree, TextView agree, OnClickListener cli) {
        if (cli != null) {
            final OnClickListener c = cli;
            diaAgree.setOnClickListener(v -> c.onDisagreeClick(v, InviteInRoomDialog.this));
            agree.setOnClickListener(v -> c.onAgreeClick(v, InviteInRoomDialog.this));
        }

    }


}
