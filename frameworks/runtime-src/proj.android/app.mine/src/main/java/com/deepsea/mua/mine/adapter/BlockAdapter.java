package com.deepsea.mua.mine.adapter;

import android.content.Context;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemBlockBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.BlockVo;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class BlockAdapter extends BaseBindingAdapter<BlockVo, ItemBlockBinding> {

    private OnMyClickListener mListener;

    public void setmListener(OnMyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyClickListener {

        void operate(int pos);
    }

    public BlockAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_block;
    }

    @Override
    protected void bind(BindingViewHolder<ItemBlockBinding> holder, BlockVo item) {
        ViewBindUtils.setText(holder.binding.tvName, item.getNickname());
        GlideUtils.circleImage(holder.binding.photoIv, item.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);
        ViewBindUtils.setText(holder.binding.tvBlockTime, item.getCreatetime());
        ViewBindUtils.RxClicks(holder.binding.photoIv, o -> {
            PageJumpUtils.jumpToProfile(item.getUserid());
        });
        ViewBindUtils.RxClicks(holder.binding.consGroup, o -> {
            if (mListener != null) {
                mListener.operate(holder.getLayoutPosition());
            }
        });

    }


}
