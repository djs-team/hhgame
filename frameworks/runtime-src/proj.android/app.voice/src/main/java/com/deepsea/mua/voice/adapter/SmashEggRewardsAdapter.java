package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.receive.BreakEggRecord;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSmashEggRvBinding;
import com.deepsea.mua.voice.databinding.ItemSongAppointmentBinding;

import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SmashEggRewardsAdapter extends BaseBindingAdapter<BreakEggRecord, ItemSmashEggRvBinding> {


    public SmashEggRewardsAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<BreakEggRecord> data) {
        super.setNewData(data);

    }

    @Override
    public void addData(List<BreakEggRecord> data) {
        super.addData(data);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_smash_egg_rv;
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            if (mData.size() > 11) {
                return 11;
            } else {
                return  mData.size();
            }
        } else {
            return 0;
        }
    }

    @Override
    protected void bind(BindingViewHolder<ItemSmashEggRvBinding> holder, BreakEggRecord item) {
        int pos = holder.getAdapterPosition();
        ViewBindUtils.setVisible(holder.binding.tvNumIndex, pos == 0);
        ViewBindUtils.setVisible(holder.binding.tvEggSilverDesc, pos == 0);
        ViewBindUtils.setVisible(holder.binding.tvEggGoldDesc, pos == 0);
        ViewBindUtils.setVisible(holder.binding.ivEggGold, pos != 0 && item.getEggType() == 0);
        ViewBindUtils.setVisible(holder.binding.ivEggSilver, pos != 0 && item.getEggType() == 1);
        // 0为金蛋，1为银蛋
        if (item.getEggType() == 0) {
            GlideUtils.loadImage(holder.binding.ivEggGold, item.getGiftImage());
        } else {
            GlideUtils.loadImage(holder.binding.ivEggSilver, item.getGiftImage());
        }


//        ViewBindUtils.setText(holder.binding.tvSongIndex, String.valueOf(pos + 1)+".");
//
//        ViewBindUtils.setText(holder.binding.tvOnDemandPerson, TextUtils.isEmpty(item.getDemandUserName()) ? "" : item.getDemandUserName());
//
//        ViewBindUtils.setText(holder.binding.tvGuestsPerson, TextUtils.isEmpty(item.getConsertUserName()) ? "" : item.getConsertUserName());
//        ViewBindUtils.setVisible(holder.binding.tvGuestsMsg,!TextUtils.isEmpty(item.getConsertUserName()));
//        holder.binding.tvSongName.setMarqueeNum(-1);
//        holder.binding.tvSongName.setText(item.getSongName()+"     —"+item.getSingerName());
//        holder.binding.tvSongName.setSelected(true);


    }

}