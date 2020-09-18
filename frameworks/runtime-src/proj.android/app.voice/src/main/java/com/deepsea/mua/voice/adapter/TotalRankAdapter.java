package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.TotalRank;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.span.LevelResUtils;
import com.deepsea.mua.voice.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by JUN on 2019/4/10
 */
public class TotalRankAdapter extends BaseQuickAdapter<TotalRank, BaseViewHolder> {

    private String type = "心动值";
    private boolean isWealth;

    public TotalRankAdapter(Context context, int layoutResId, List<TotalRank> data) {
        super(layoutResId, data);
    }

    public String getType() {
        return type;
    }

    public void setWealth(boolean wealth) {
        isWealth = wealth;
        type = isWealth ? "财富值" : "心动值";
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TotalRank item) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.rank_tv, String.valueOf(position + 3));
        GlideUtils.circleImage(helper.getView(R.id.rank_iv), item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        helper.setText(R.id.rank_nick_tv, item.getNickname());
        helper.setText(R.id.level_tv, item.getUser_lv() + "");
        helper.setVisible(R.id.level_tv, item.getUser_lv() > 0);
        helper.setBackgroundRes(R.id.level_tv, LevelResUtils.getLevelRes(item.getUser_lv()));
        helper.getView(R.id.rank_tv).setSelected(isWealth);
        helper.setText(R.id.number_tv, String.format(Locale.CHINA, "%s\n%s", type, FormatUtils.subZeroAndDot(item.getCoin())));
        helper.setText(R.id.uid_tv, "ID: " + item.getUserid());
    }
}
