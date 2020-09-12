package com.deepsea.mua.voice.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.TotalRank;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.LayoutRankViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/8/27
 */
public class RankView extends LinearLayout {

    private LayoutRankViewBinding mBinding;

    public RankView(@NonNull Context context) {
        this(context, null);
    }

    public RankView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RankView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_rank_view, this, true);
    }

    public void setData(List<TotalRank> list) {
        setData(list, false);
    }

    public void setData(List<TotalRank> list, boolean isWealth) {
        if (list == null) {
            list = new ArrayList<>();
        }

        int empty = isWealth ? R.drawable.ic_wealth_place : R.drawable.ic_heart_place;

        int place = R.drawable.ic_place_avatar;

        int index = 0;
        if (index < list.size()) {
            mBinding.rankNo1Iv.setBackgroundResource(R.drawable.white_stroke_3dp);
            GlideUtils.circleImage(mBinding.rankNo1Iv, list.get(index).getAvatar(), place, place);
            mBinding.rankNo1.setVisibility(VISIBLE);
            mBinding.rankNo1Mask.setVisibility(VISIBLE);
        } else {
            mBinding.rankNo1Iv.setBackgroundResource(empty);
            mBinding.rankNo1.setVisibility(GONE);
            mBinding.rankNo1Mask.setVisibility(GONE);
        }
        //top 2
        index = 1;
        if (index < list.size()) {
            mBinding.rankNo2Iv.setBackgroundResource(R.drawable.white_stroke_3dp);
            GlideUtils.circleImage(mBinding.rankNo2Iv, list.get(index).getAvatar(), place, place);
        } else {
            mBinding.rankNo2Iv.setBackgroundResource(empty);
        }
        //top 3
        index = 2;
        if (index < list.size()) {
            mBinding.rankNo3Iv.setBackgroundResource(R.drawable.white_stroke_3dp);
            GlideUtils.circleImage(mBinding.rankNo3Iv, list.get(index).getAvatar(), place, place);
        } else {
            mBinding.rankNo3Iv.setBackgroundResource(empty);
        }
    }
}
