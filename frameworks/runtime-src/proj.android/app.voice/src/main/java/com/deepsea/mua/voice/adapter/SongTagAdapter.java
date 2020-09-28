package com.deepsea.mua.voice.adapter;

import android.content.Context;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSongTagBinding;

/**
 * Created by JUN on 2019/5/5
 */
public class SongTagAdapter extends BaseBindingAdapter<String, ItemSongTagBinding> {

    private OnSongTagSelectListener mListener;

    public interface OnSongTagSelectListener {
        void onSelectTag(int position);
    }

    public void setmListener(OnSongTagSelectListener mListener) {
        this.mListener = mListener;
    }

    public SongTagAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_song_tag;
    }

    private int defaultPos = 0;


    @Override
    protected void bind(BindingViewHolder<ItemSongTagBinding> holder, String item) {
        holder.binding.tvSongTag.setSelected(holder.getLayoutPosition() == defaultPos);
        ViewBindUtils.RxClicks(holder.binding.tvSongTag, o -> {
            defaultPos = holder.getLayoutPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.onSelectTag(defaultPos);
            }
        });

    }

    public int getForbiddenDay() {
        return defaultPos;
    }

}
