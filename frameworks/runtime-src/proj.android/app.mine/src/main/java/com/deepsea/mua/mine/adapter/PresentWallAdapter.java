package com.deepsea.mua.mine.adapter;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.stub.entity.PresentWallBean;
import com.deepsea.mua.stub.utils.NumberUtils;

/**
 * Created by JUN on 2019/9/12
 */
public class PresentWallAdapter extends BaseQuickAdapter<PresentWallBean, BaseViewHolder> {

    public PresentWallAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, PresentWallBean aData) {
        ImageView present_iv = helper.getView(R.id.present_iv);
        GlideUtils.loadImage(present_iv, aData.gift_image, R.drawable.ic_place, R.drawable.ic_place);
        helper.setText(R.id.present_name_tv, aData.gift_name);
        helper.setText(R.id.present_count_tv, "x" + NumberUtils.formatNum(aData.num, false));
    }
}
