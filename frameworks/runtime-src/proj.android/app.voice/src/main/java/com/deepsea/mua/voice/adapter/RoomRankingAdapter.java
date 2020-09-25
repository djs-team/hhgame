package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRankingBinding;

/**
 * Created by JUN on 2019/4/2
 */
public class RoomRankingAdapter extends BaseBindingAdapter<String, ItemRankingBinding> {

    public RoomRankingAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_ranking;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRankingBinding> holder, String item) {
        int errorRes = R.drawable.ic_place;
        if (TextUtils.isEmpty(item)) {
            holder.binding.rankIv.setImageResource(errorRes);
        } else {
            GlideUtils.circleImage(holder.binding.rankIv, item, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        }
    }
}
