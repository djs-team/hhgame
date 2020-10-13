package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.receive.SongRankData;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSongAppointmentBinding;
import com.deepsea.mua.voice.databinding.ItemSongRankBinding;

import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SongRankAdapter extends BaseBindingAdapter<SongRankData, ItemSongRankBinding> {


    public SongRankAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<SongRankData> data) {
        super.setNewData(data);

    }

    @Override
    public void addData(List<SongRankData> data) {
        super.addData(data);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_song_rank;
    }


    @Override
    protected void bind(BindingViewHolder<ItemSongRankBinding> holder, SongRankData item) {
        int pos = holder.getAdapterPosition();
        ViewBindUtils.setText(holder.binding.tvSongIndex, String.valueOf(pos + 1));
        if (pos == 0) {
            ViewBindUtils.setImageRes(holder.binding.ivAchievement, R.drawable.icon_ranking_champion);
        } else if (pos == 1) {
            ViewBindUtils.setImageRes(holder.binding.ivAchievement, R.drawable.icon_ranking_runnerup);
        } else if (pos == 2) {
            ViewBindUtils.setImageRes(holder.binding.ivAchievement, R.drawable.icon_ranking_secondrunnerup);
        }
        ViewBindUtils.setVisible(holder.binding.ivAchievement, pos <= 2);
        ViewBindUtils.setVisible(holder.binding.viewLine, pos >= 2);
        ViewBindUtils.setText(holder.binding.tvUserName, item.getUserName());
        ViewBindUtils.setText(holder.binding.tvSongInfo, item.getSongName() + "-" + item.getSingerName());
        GlideUtils.circleImage(holder.binding.ivUserhead, item.getUserImage(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.setText(holder.binding.tvAccount, String.valueOf(item.getScore()));
        ViewBindUtils.RxClicks(holder.binding.ivUserhead, o -> {
            PageJumpUtils.jumpToProfile(String.valueOf(item.getUserId()));
        });

    }

}