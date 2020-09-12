package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemGuardHeadBinding;
import com.deepsea.mua.voice.databinding.ItemGuardListHeadBinding;
import com.uuzuche.lib_zxing.DisplayUtil;

/**
 * Created by JUN on 2019/10/14
 */
public class GuardListHeaderAdapter extends BaseBindingAdapter<String, ItemGuardListHeadBinding> {

    public GuardListHeaderAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_list_head;
    }


    @Override
    protected void bind(BindingViewHolder<ItemGuardListHeadBinding> holder, String bean) {
        GlideUtils.circleImage(holder.binding.ivHead, bean, R.drawable.ic_place, R.drawable.ic_place);
    }
}
