package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemShareChoseMicroBinding;
import com.deepsea.mua.voice.databinding.ItemSongLrcBinding;
import com.deepsea.mua.voice.lrc.impl.LrcRow;

import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SongLrcAdapter extends BaseBindingAdapter<LrcRow, ItemSongLrcBinding> {


    public SongLrcAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<LrcRow> data) {
        super.setNewData(data);

    }

//    @Override
//    public int getItemCount() {
//        return mData.size() > 7 ? 7 : mData.size();
//    }

    @Override
    public void addData(List<LrcRow> data) {
        super.addData(data);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_song_lrc;
    }


    @Override
    protected void bind(BindingViewHolder<ItemSongLrcBinding> holder, LrcRow item) {
        ViewBindUtils.setText(holder.binding.tvLrc, item.getContent());
        if (holder.getLayoutPosition() == selectPos) {
            ViewBindUtils.setTextColor(holder.binding.tvLrc, R.color.lrc_select);
            holder.binding.tvLrc.setTextSize(12);
            holder.binding.tvLrc.getPaint().setFakeBoldText(true);
        } else {
            ViewBindUtils.setTextColor(holder.binding.tvLrc, R.color.lrc_normal);
            holder.binding.tvLrc.setTextSize(12);
            holder.binding.tvLrc.getPaint().setFakeBoldText(false);

        }
    }

    private int selectPos = 0;

    public void setSelectPos(int pos) {
        selectPos = pos;
    }

}