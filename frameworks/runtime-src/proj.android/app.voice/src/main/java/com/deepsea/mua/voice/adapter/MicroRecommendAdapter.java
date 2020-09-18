package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RecommendRoomResult;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemDialogSelectBinding;
import com.deepsea.mua.voice.databinding.ItemRecommendBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class MicroRecommendAdapter extends BaseBindingAdapter<RecommendRoomResult.ListBean, ItemRecommendBinding> {
    private int roomType;

    public MicroRecommendAdapter(Context context, int type) {
        super(context);
        this.roomType = type;

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_recommend;
    }


    @Override
    protected void bind(BindingViewHolder<ItemRecommendBinding> holder, RecommendRoomResult.ListBean bean) {
        ViewBindUtils.setText(holder.binding.tvGuestName, bean.getNickname());
        StringBuilder info = new StringBuilder();
        info.append(bean.getAge());
        info.append("岁");
        if (!TextUtils.isEmpty(bean.getStature())) {
            info.append(" |");
            info.append(" " + bean.getStature() + "cm");
        }
        String sex = SexResUtils.getSex(bean.getSex());
        info.append(sex);
        ViewBindUtils.setText(holder.binding.tvUserInfo, info.toString());
        if (bean.getState() == 1) {
            ViewBindUtils.setBackgroundRes(holder.binding.tvState, R.drawable.bg_microstate_blindate);
            ViewBindUtils.setText(holder.binding.tvState, roomType == 9 ? "热聊中" : "相亲中");
        } else if (bean.getState() == 2) {
            ViewBindUtils.setBackgroundRes(holder.binding.tvState, R.drawable.bg_microstate_wait);
            ViewBindUtils.setText(holder.binding.tvState, "等待中");
        }
        ViewBindUtils.setVisible(holder.binding.tvState, bean.getState() == 1 || bean.getState() == 2);
        ViewBindUtils.setImageUrl(mContext, holder.binding.ivHead, bean.getAvatar());

    }
}
