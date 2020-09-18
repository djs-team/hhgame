package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.RankVo;
import com.deepsea.mua.stub.entity.socket.receive.MicroRankData;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemUserFansBinding;

/**
 * Created by JUN on 2019/10/14
 */
public class UserFansAdapter extends BaseBindingAdapter<MicroRankData, ItemUserFansBinding> {


    public UserFansAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_user_fans;
    }


    @Override
    protected void bind(BindingViewHolder<ItemUserFansBinding> holder, MicroRankData item) {
        int pos = holder.getLayoutPosition();
        if (pos == 0) {
            ViewBindUtils.setBackgroundRes(holder.binding.rankIndexIv, R.drawable.ic_fans_gold);
        } else if (pos == 1) {
            ViewBindUtils.setBackgroundRes(holder.binding.rankIndexIv, R.drawable.ic_fans_silver);
        } else if (pos == 2) {
            ViewBindUtils.setBackgroundRes(holder.binding.rankIndexIv, R.drawable.ic_fans_copper);
        }
        ViewBindUtils.setVisible(holder.binding.rankIndexTv, pos > 2);
        ViewBindUtils.setVisible(holder.binding.rankIndexIv, pos <= 2);
        ViewBindUtils.setText(holder.binding.rankIndexTv, String.valueOf(pos + 1));
        GlideUtils.circleImage(holder.binding.rankIv, item.getUserImage(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.RxClicks(holder.binding.rankIv, o -> {
            PageJumpUtils.jumpToProfile(String.valueOf(item.getUserId()));
        });
        holder.binding.rankNickTv.setText(item.getUserName());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, String.valueOf(item.getSex()));
        StringBuilder info = new StringBuilder();
        info.append(item.getAge());
        info.append("岁");
        if (!TextUtils.isEmpty(item.getStature())) {
            if (Integer.valueOf(item.getStature()) > 0) {
                info.append(" |");
                info.append(" " + item.getStature() + "cm");
            }
        }
        if (!TextUtils.isEmpty(item.getCity())) {
            info.append(" |");
            info.append(" " + item.getCity());
        }
        ViewBindUtils.setText(holder.binding.numberTv, String.valueOf(item.getScore()));

    }
}
