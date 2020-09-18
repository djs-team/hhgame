package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.activity.RoomSetActivity;
import com.deepsea.mua.voice.databinding.DialogRoomOwnerBinding;

/**
 * Created by JUN on 2019/3/28
 */
public class RoomOwnerDialog extends BaseDialog<DialogRoomOwnerBinding> {

    public interface OnOwnerClickListener {
        /**
         * 锁定房间
         */
        void onLockRoom();
    }

    private OnOwnerClickListener mListener;
    private RoomData mRoomData;
    private String mRoomId;

    public RoomOwnerDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected float getWidthPercent() {
        return 0.78F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_room_owner;
    }

    public void setData(RoomData roomData, MicroUser user) {
        this.mRoomData = roomData;
        this.mRoomId = mRoomData.getRoomData().getId();
        mBinding.lockRoomTv.setText(roomData.getRoomData().isRoomLock() ? "解锁房间" : "锁定房间");
        mBinding.ownerIdTv.setText("ID:  " + roomData.getRoomData().getId());
        mBinding.ownerNameTv.setText(user.getUser().getName());
        GlideUtils.circleImage(mBinding.ownerIv, user.getUser().getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
    }

    @Override
    protected void initListener() {
        ViewBindUtils.RxClicks(mBinding.lockRoomTv, o -> {
            if (mListener != null) {
                mListener.onLockRoom();
            }
            dismiss();
        });

        ViewBindUtils.RxClicks(mBinding.roomSetTv, o -> {
            RoomData.RoomDataBean bean = mRoomData.getRoomData();
            int modelId = -1;
            int tagId = -1;
            if (bean != null) {
                modelId = bean.getRoomType();
                if (bean.getRoomTags() != null) {
                    tagId = bean.getRoomTags();
                }
            }
            mContext.startActivity(RoomSetActivity.newIntent(mContext, mRoomId, mRoomData.getUserIdentity(), modelId, tagId,mRoomData.getModeName()));
            dismiss();
        });

    }

    public void setOnOperateDlgListener(OnOwnerClickListener listener) {
        this.mListener = listener;
    }
}
