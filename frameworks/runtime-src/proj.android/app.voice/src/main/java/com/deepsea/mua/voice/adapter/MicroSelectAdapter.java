package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RoomModeHelpVo;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemDialogSelectBinding;
import com.deepsea.mua.voice.databinding.ItemRoomModeHelpBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class MicroSelectAdapter extends BaseBindingAdapter<RoomData.MicroInfosBean, ItemDialogSelectBinding> {

    private OnSelectMicUserListener onSelectMicUserListener;

    public interface OnSelectMicUserListener {
        void onSelectClick(int Level, int Number);
    }

    public void setOnSelectMicUserListener(OnSelectMicUserListener onSelectMicUserListener) {
        this.onSelectMicUserListener = onSelectMicUserListener;
    }

    public MicroSelectAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_dialog_select;
    }


    @Override
    protected void bind(BindingViewHolder<ItemDialogSelectBinding> holder, RoomData.MicroInfosBean bean) {
        if (bean.getUser() != null) {
            WsUser user = bean.getUser();
            //红娘
            String hongAvatar = user.getHeadImageUrl();
            if (!TextUtils.isEmpty(hongAvatar)) {
                GlideUtils.loadImage(holder.binding.ivGuestBigAvatar, hongAvatar, R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg);
                GlideUtils.circleImage(holder.binding.ivGuestSmallAvatar, hongAvatar, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            }
            ViewBindUtils.setText(holder.binding.tvGuestName, user.getName());
            StringBuilder info = new StringBuilder();
            info.append(user.getAge());
            info.append("岁");
            if (user.getStature() != 0) {
                info.append("|");
                info.append(user.getStature() + "cm");
            }
            info.append("|");
            info.append("男");
            ViewBindUtils.setText(holder.binding.tvGuestIntroduction, info.toString());
            ViewBindUtils.RxClicks(holder.binding.rlUserGuest, o -> {
                ViewBindUtils.setVisible(holder.binding.rlGuestSelect, holder.binding.rlGuestSelect.getVisibility() == View.GONE);
            });
            ViewBindUtils.RxClicks(holder.binding.ivGuestSelectConfirm, o -> {
                if (onSelectMicUserListener != null) {
                    onSelectMicUserListener.onSelectClick(bean.getType(), bean.getNumber());
                }
            });
        }
    }
}
