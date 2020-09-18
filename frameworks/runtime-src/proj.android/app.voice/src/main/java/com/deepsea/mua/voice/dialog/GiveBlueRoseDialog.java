package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.NotifyHelpAdapter;
import com.deepsea.mua.voice.databinding.DialogBlueRoseGiveBinding;
import com.deepsea.mua.voice.databinding.DialogNotifyHelpBinding;

import java.util.List;

/**
 * Created by JUN on 2018/9/27
 */
public class GiveBlueRoseDialog extends BaseDialog<DialogBlueRoseGiveBinding> {

    public GiveBlueRoseDialog(@NonNull Context context) {
        super(context);
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.tvOk, o -> {
            if (TextUtils.isEmpty(mBinding.etGiveNum.getText().toString().trim())) {
                ToastUtils.showToast("请输入数量");
                return;
            }
            if (onClickListener != null) {
                onClickListener.onClick(Integer.valueOf(mBinding.etGiveNum.getText().toString().trim()));
            }

        });
    }

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick(int num);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_blue_rose_give;
    }

    @Override
    protected float getWidthPercent() {
        return 0.85F;
    }


}
