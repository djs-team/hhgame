package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.receive.SongParam;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSongOriginalBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SongOriginalAdapter extends BaseBindingAdapter<SongParam, ItemSongOriginalBinding> {

    private OnSongOperateListener mListener;

    public SongOriginalAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<SongParam> data) {
        super.setNewData(data);

    }

    @Override
    public void addData(List<SongParam> data) {
        super.addData(data);
    }

    public interface OnSongOperateListener {
        void AppointmentClick(int position, SongParam bean);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_song_original;
    }

    public void setOnSongOperateListener(OnSongOperateListener listener) {
        this.mListener = listener;
    }


    @Override
    protected void bind(BindingViewHolder<ItemSongOriginalBinding> holder, SongParam item) {
        int pos = holder.getAdapterPosition();

        ViewBindUtils.RxClicks(holder.binding.tvConfirm, o -> {
            if (mListener != null) {
                mListener.AppointmentClick(pos, item);
            }
        });
        ViewBindUtils.setText(holder.binding.tvSongSinger, item.getSingerName());
        holder.binding.tvSongName.setText(item.getSongName());
        holder.binding.tvSongName.setSelected(true);
    }

}