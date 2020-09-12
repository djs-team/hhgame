package com.deepsea.mua.stub.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogGiftMsgBinding;
import com.deepsea.mua.stub.databinding.DialogGiftNofriendMsgBinding;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2018/9/27
 */
public class AddFriendWithNoGiftDialog extends BaseDialog<DialogGiftNofriendMsgBinding> {

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * @param v
         * @param dialog
         */
        void onClick(View v, Dialog dialog);
    }

    public AddFriendWithNoGiftDialog(@NonNull Context context) {
        super(context);
//        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
//            dismiss();
//        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_gift_nofriend_msg;
    }

    @Override
    protected float getWidthPercent() {
        return 0.77F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }


    public AddFriendWithNoGiftDialog setRightButton(String str, OnClickListener cli) {
        initButton(mBinding.btnAddFriend, str, cli);
        return this;
    }


    private void initButton(Button btn, String str, OnClickListener cli) {
        btn.setText(str);
        if (cli == null) {
            cli = (v, dia) -> dismiss();
        }
        final OnClickListener c = cli;
        btn.setOnClickListener(v -> c.onClick(v, AddFriendWithNoGiftDialog.this));
    }


    public String getInputMsg(){
        return mBinding.etGiftMessage.getText().toString().trim();
    }

}
