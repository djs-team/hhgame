package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogRoomCloseBinding;

/**
 * Created by JUN on 2019/3/28
 */
public class RoomCloseDialog extends BaseDialog<DialogRoomCloseBinding> {

    public interface RoomCloseListener {
        /**
         * 最小化
         */
        void onMiniRoom();

        /**
         * 关闭
         */
        void onCloseRoom();
    }

    private RoomCloseListener mListener;
    private String mRoomId;

    public RoomCloseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected float getWidthPercent() {
        return 0.78F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_room_close;
    }

    public void setRoomCloseListener(RoomCloseListener listener) {
        this.mListener = listener;
    }

    public void show(String roomId) {
        this.mRoomId = roomId;
        super.show();
    }

    @Override
    protected void initListener() {
        ViewBindUtils.RxClicks(mBinding.closeIv, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.reportTv, o -> {
            dismiss();
            ArouterUtils.build(ArouterConst.PAGE_REPORT)
                    .withString("roomId", mRoomId)
                    .navigation();
        });
        ViewBindUtils.RxClicks(mBinding.closeRoomTv, o -> {
            dismiss();
            if (mListener != null) {
                mListener.onCloseRoom();
            }
        });
        ViewBindUtils.RxClicks(mBinding.miniRoomTv, o -> {
            dismiss();
            if (mListener != null) {
                mListener.onMiniRoom();
            }
        });
    }
}
