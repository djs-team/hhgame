package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Build;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.entity.HomeInfo;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomBinding;
import com.deepsea.mua.voice.view.TextureVideoViewOutlineProvider;
import com.uuzuche.lib_zxing.DisplayUtil;

/**
 * Created by JUN on 2019/4/17
 */
public class RoomAdapter extends BaseBindingAdapter<HomeInfo.RoomBean, ItemRoomBinding> {

    private final int SINGLE_WIDTH;
    private int type;

    public RoomAdapter(Context context) {
        super(context);
        SINGLE_WIDTH = (mContext.getResources().getDisplayMetrics().widthPixels
                - ResUtils.dp2px(mContext, 15) * 2
                - ResUtils.dp2px(mContext, 16)) / 2;
    }

    public RoomAdapter(Context context, int type) {
        super(context);
        SINGLE_WIDTH = (mContext.getResources().getDisplayMetrics().widthPixels
                - ResUtils.dp2px(mContext, 15) * 2
                - ResUtils.dp2px(mContext, 16)) / 2;
        this.type = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_room;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRoomBinding> holder, HomeInfo.RoomBean item) {
        ViewGroup.LayoutParams lp = holder.binding.roomIv.getLayoutParams();
        lp.width = SINGLE_WIDTH;
        lp.height = SINGLE_WIDTH;
        holder.binding.roomIv.setLayoutParams(lp);
        if (Integer.valueOf(item.getRoom_type()) > 6) {
            ViewBindUtils.setBackgroundRes(holder.binding.tagTv, R.drawable.room_tag_blinddate_bg);
        } else {
            ViewBindUtils.setBackgroundRes(holder.binding.tagTv, R.drawable.room_tag_mfriend_bg);
        }
        GlideUtils.circleImage(holder.binding.avatarIv, item.getMaker_avatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nameTv.setText(item.getMaker_nickname());
        GlideUtils.roundImage(holder.binding.roomIv, item.getFm_room_image(), R.drawable.ic_place_room_bg, R.drawable.ic_place_room_bg, 10);
        holder.setVisible(holder.binding.roomLockRl, TextUtils.equals("1", item.getRoom_lock()));
//        holder.setVisible(holder.binding.tagTv, !TextUtils.isEmpty(item.getRoom_tags()));
        holder.binding.tagTv.setText(item.getRoom_tags());
        holder.binding.nickTv.setText(item.getRoom_name());
        holder.setVisible(holder.binding.ageTv, item.getFm_age() > 0);
        holder.binding.ageTv.setText(item.getFm_age() + "岁");
        holder.setVisible(holder.binding.cityTv, !TextUtils.isEmpty(item.getFm_city()));
        holder.binding.cityTv.setText(item.getFm_city());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.binding.llDesc.setOutlineProvider(new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 10)));
            holder.binding.llDesc.setClipToOutline(true);
        }
        GlideUtils.loadGif(holder.binding.ivPlay, R.drawable.icon_room_play);
        if (item.getXq_type() == 1) {
            ViewBindUtils.setBackgroundRes(holder.binding.tvState, R.drawable.bg_microstate_blindate);
            ViewBindUtils.setText(holder.binding.tvState, "相亲中");
        } else if (item.getXq_type() == 2) {
            ViewBindUtils.setBackgroundRes(holder.binding.tvState, R.drawable.bg_microstate_wait);
            ViewBindUtils.setText(holder.binding.tvState, "等待中");
        }
        ViewBindUtils.setText(holder.binding.tvWelcome, "欢迎来到" + item.getRoom_name() + "的直播间");

//        Shader shader = new LinearGradient(0, 0, 0, 20, Color.parseColor("#FD1E1D"), Color.parseColor("#FE711C"), Shader.TileMode.CLAMP);
//        holder.binding.tvHuigou.getPaint().setShader(shader);
    }
}
