package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.FansRankBean;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.span.LevelResUtils;
import com.deepsea.mua.voice.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by JUN on 2019/6/28
 */
public class FansRankAdapter extends BaseQuickAdapter<FansRankBean, BaseViewHolder> {

    public FansRankAdapter(Context context, int layoutResId, List<FansRankBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, FansRankBean item) {
        int position = helper.getAdapterPosition();
        helper.setText(R.id.rank_tv, String.valueOf(position + 3));
        GlideUtils.circleImage(helper.getView(R.id.rank_iv), item.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        helper.setText(R.id.rank_nick_tv, item.getNickname());
        helper.setText(R.id.level_tv, item.getUser_lv() + "");
        helper.setVisible(R.id.level_tv, item.getUser_lv() > 0);
        helper.setBackgroundRes(R.id.level_tv, LevelResUtils.getLevelRes(item.getUser_lv()));
        helper.getView(R.id.rank_tv).setSelected(true);
        helper.setText(R.id.number_tv, String.format(Locale.CHINA, "贡献值\n%s", FormatUtils.subZeroAndDot(item.getCoin())));
        helper.setText(R.id.uid_tv, "ID: " + item.getUser_id());
    }
}
