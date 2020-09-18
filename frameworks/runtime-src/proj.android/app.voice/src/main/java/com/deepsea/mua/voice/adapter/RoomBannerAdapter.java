package com.deepsea.mua.voice.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;

/**
 * Created by JUN on 2019/7/31
 */
public class RoomBannerAdapter implements CBViewHolderCreator {

    @Override
    public Holder createHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_room_banner;
    }

    private static class ViewHolder extends Holder<VoiceBanner.BannerListBean> {
        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            imageView = (ImageView) itemView;
        }

        @Override
        public void updateUI(VoiceBanner.BannerListBean data) {
            if (!data.isLocalImage()) {
                GlideUtils.loadImage(imageView, data.getImage(), R.drawable.ic_place, R.drawable.ic_place);
            } else {
                ViewBindUtils.setImageRes(imageView, data.getImageId());
            }
        }
    }
}
