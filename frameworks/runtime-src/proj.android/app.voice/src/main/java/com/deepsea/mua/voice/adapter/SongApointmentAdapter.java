package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemSongAppointmentBinding;
import com.deepsea.mua.voice.utils.MatchMakerUtils;

import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SongApointmentAdapter extends BaseBindingAdapter<SongInfo, ItemSongAppointmentBinding> {

    private OnSongOperateListener mListener;

    public SongApointmentAdapter(Context context) {
        super(context);
    }


    @Override
    public void setNewData(List<SongInfo> data) {
        super.setNewData(data);

    }

    @Override
    public void addData(List<SongInfo> data) {
        super.addData(data);
    }


    public interface OnSongOperateListener {
        void DelClick(int position, SongInfo bean);//删除

        void StickyClick(int position, SongInfo bean);//置顶
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_song_appointment;
    }

    public void setOnSongOperateListener(OnSongOperateListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSongAppointmentBinding> holder, SongInfo item) {
        int pos = holder.getAdapterPosition();
        ViewBindUtils.RxClicks(holder.binding.ivSongDel, o -> {
            if (mListener != null) {
                mListener.DelClick(pos, item);
            }
        });
        ViewBindUtils.RxClicks(holder.binding.ivSongSticky, o -> {
            if (mListener != null) {
                mListener.StickyClick(pos, item);
            }
        });
        String posStr = "";
        if (pos < 9) {
            posStr = "0" + pos;
        } else {
            posStr = String.valueOf(pos);
        }
        ViewBindUtils.setText(holder.binding.tvSongIndex, posStr);

        ViewBindUtils.setText(holder.binding.tvOnDemandPerson, TextUtils.isEmpty(item.getDemandUserName()) ? "点播：" : "点播：" + item.getDemandUserName());

        ViewBindUtils.setText(holder.binding.tvGuestsPerson, TextUtils.isEmpty(item.getConsertUserName()) ? "演唱嘉宾：" : "演唱嘉宾：" + item.getConsertUserName());
        holder.binding.tvSongName.setText(item.getSongName());
        boolean isOwner = MatchMakerUtils.isRoomOwner();
        ViewBindUtils.setVisible(holder.binding.ivSongDel, isOwner);
        ViewBindUtils.setVisible(holder.binding.ivSongSticky, isOwner);

    }

}