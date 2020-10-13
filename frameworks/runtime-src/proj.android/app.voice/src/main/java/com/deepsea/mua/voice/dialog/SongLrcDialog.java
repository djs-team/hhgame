package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.SongLrcAdapter;
import com.deepsea.mua.voice.databinding.DialogSongLrcBinding;
import com.deepsea.mua.voice.lrc.impl.LrcRow;

import java.util.List;

/**
 * Created by JUN on 2018/9/27
 */
public class SongLrcDialog extends BaseDialog<DialogSongLrcBinding> {

    public SongLrcDialog(@NonNull Context context) {
        super(context);

    }

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClick(int num);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_song_lrc;
    }

    @Override
    protected float getWidthPercent() {
        return 1F;
    }

    public void setSongInfo(SongInfo songInfo) {
        ViewBindUtils.setText(mBinding.tvSongName, songInfo.getSongName());
        ViewBindUtils.setText(mBinding.tvSongDesc, songInfo.getSingerName());
    }

    public void setLrcProcess(int pos, int max) {
        mBinding.seekbarSongProgress.setMax(max);
        String endTimeStr = TimeUtils.formatSongTime(max);
        ViewBindUtils.setText(mBinding.tvSongTimeEnd, endTimeStr);
        mBinding.seekbarSongProgress.setProgress(pos);
        String startTimeStr = TimeUtils.formatSongTime(pos);
        ViewBindUtils.setText(mBinding.tvSongTimeStart, startTimeStr);
    }

    private SongLrcAdapter adapter;

    public void setLrcSetting(List<LrcRow> rows) {
        adapter = new SongLrcAdapter(mContext);
        mBinding.rvLrc.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvLrc.setAdapter(adapter);
        adapter.setNewData(rows);

        /**
         * 既然是动画，就会有时间，我们把动画执行时间变大一点来看一看效果
         */
//        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
//        defaultItemAnimator.s(1000);
//        defaultItemAnimator.setRemoveDuration(1000);
//        mRecyclerView.setItemAnimator(defaultItemAnimator);
    }

    public void setLrcScroll(int currentPosition) {
        Log.d("songLrc", ";" + currentPosition);
//        RecyclerView.LayoutManager layoutManager = mBinding.rvLrc.getLayoutManager();
//        if (layoutManager instanceof LinearLayoutManager) {
//            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
//            int firstItemPosition = linearManager.findFirstVisibleItemPosition();
//
//        }
        adapter.setSelectPos(currentPosition);
        adapter.notifyDataSetChanged();
        mBinding.rvLrc.smoothScrollToPosition(currentPosition + 3);
    }

    public void clearData() {
        ViewBindUtils.setText(mBinding.tvSongTimeStart, "00.00");
        ViewBindUtils.setText(mBinding.tvSongTimeEnd, "");
        mBinding.seekbarSongProgress.setProgress(0);
        ViewBindUtils.setText(mBinding.tvSongName, "");
        ViewBindUtils.setText(mBinding.tvSongDesc, "");
        adapter.setNewData(null);
    }


}
