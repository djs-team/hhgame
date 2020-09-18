package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.util.Log;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.UserRedPacket;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRedpackageResultBinding;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by JUN on 2019/4/23
 */
public class RedPakageResultAdapter extends BaseBindingAdapter<UserRedPacket, ItemRedpackageResultBinding> {


    public RedPakageResultAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_redpackage_result;
    }

    @Override
    protected void bind(BindingViewHolder<ItemRedpackageResultBinding> holder, UserRedPacket item) {
        GlideUtils.circleImage(holder.binding.ivHead, item.getHeadImageUrl(), R.drawable.ic_place_default, R.drawable.ic_place_default);
        ViewBindUtils.setText(holder.binding.tvName, item.getNickName());
        ViewBindUtils.setText(holder.binding.tvRedpackagePrice, item.getValue() + "元");
        int pos = holder.getLayoutPosition();
        Log.d("AG_EX_AV", pos + ":" + item.getValue());
        if (pos == 0) {
            ViewBindUtils.setImageRes(holder.binding.ivRedpackageRank, R.drawable.ic_redpackage_rank_first);
        } else if (pos == 1) {
            ViewBindUtils.setImageRes(holder.binding.ivRedpackageRank, R.drawable.ic_redpackage_rank_second);
        } else if (pos == 2) {
            ViewBindUtils.setImageRes(holder.binding.ivRedpackageRank, R.drawable.ic_redpackage_rank_third);
        }
        ViewBindUtils.setVisible(holder.binding.ivRedpackageRank, pos <= 2);
    }


}