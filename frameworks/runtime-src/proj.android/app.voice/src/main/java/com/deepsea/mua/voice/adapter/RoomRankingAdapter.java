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
        int position = holder.getAdapterPosition();
//        int bgRes = R.drawable.ic_gold_bg;
        int errorRes = R.drawable.ic_place;
//        switch (position) {
//            case 0:
//                bgRes = R.drawable.ic_gold_bg;
//                errorRes = R.drawable.ic_place_gold;
//                break;
//            case 1:
//                bgRes = R.drawable.ic_silver_bg;
//                errorRes = R.drawable.ic_place_silver;
//                break;
//            case 2:
//                bgRes = R.drawable.ic_copper_bg;
//                errorRes = R.drawable.ic_place_copper;
//                break;
//        }
//        ViewBindUtils.setImageRes(holder.binding.rankBgIv,bgRes);
        if (TextUtils.isEmpty(item)) {
            holder.binding.rankIv.setImageResource(errorRes);
        } else {
            GlideUtils.circleImage(holder.binding.rankIv, item, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        }
    }
}
