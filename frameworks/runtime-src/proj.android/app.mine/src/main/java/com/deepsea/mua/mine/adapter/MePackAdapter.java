package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemMePackBinding;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.Locale;

/**
 * Created by JUN on 2019/7/26
 */
public class MePackAdapter extends BaseBindingAdapter<PackBean, ItemMePackBinding> {

    public MePackAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_me_pack;
    }

    @Override
    protected void bind(BindingViewHolder<ItemMePackBinding> holder, PackBean item) {
        int position = holder.getAdapterPosition();
        ViewBindUtils.setVisible(holder.binding.rightLine, position % 4 != 3);

        GlideUtils.loadImage(holder.binding.giftIv, item.getGift_image(), R.drawable.ic_place, R.drawable.ic_place);
        holder.binding.nameTv.setText(item.getGift_name());
        holder.binding.numberTv.setText(String.format(Locale.CHINA, "x%s", item.getPack_num()));
    }
}
