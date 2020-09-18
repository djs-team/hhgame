package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.SongGuidePageAdapter;
import com.deepsea.mua.voice.databinding.DialogSongChooseAlertBinding;
import com.deepsea.mua.voice.databinding.DialogSongGuideBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 点歌提示
 * Created by JUN on 2018/9/27
 */
public class SongGuideDialog extends BaseDialog<DialogSongGuideBinding> {

    public SongGuideDialog(@NonNull Context context) {
        super(context);
        init();

    }

    public interface OnClickListener {
        /**
         * 点击回调
         */
        void onClickKnow();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    private void init() {
        List<View> views = new ArrayList<>();
        View pageOne = LayoutInflater.from(mContext).inflate(R.layout.item_guide_one, null);
        View pageTwo = LayoutInflater.from(mContext).inflate(R.layout.item_guide_two, null);
        View pageThree = LayoutInflater.from(mContext).inflate(R.layout.item_guide_three, null);
        View pageFour = LayoutInflater.from(mContext).inflate(R.layout.item_guide_four, null);
        View pageFive = LayoutInflater.from(mContext).inflate(R.layout.item_guide_five, null);
        views.add(pageOne);
        views.add(pageTwo);
        views.add(pageThree);
        views.add(pageFour);
        views.add(pageFive);
        SongGuidePageAdapter pageAdapter = new SongGuidePageAdapter(views);
        mBinding.viewPager.setAdapter(pageAdapter);
        mBinding.viewPager.setCurrentItem(0);
        mBinding.viewPager.setOffscreenPageLimit(5);
        mBinding.viewPager.setNoScroll(true);
        pageOne.findViewById(R.id.rl_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(1);
            }
        });
        pageTwo.findViewById(R.id.rl_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(2);
            }
        });
        pageThree.findViewById(R.id.rl_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(3);
            }
        });
        pageFour.findViewById(R.id.rl_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(4);
            }
        });
        pageFive.findViewById(R.id.rl_group).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickKnow();
                }
                dismiss();
            }
        });

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_song_guide;
    }

    @Override
    protected float getWidthPercent() {
        return 0.84F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

}
