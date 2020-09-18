package com.deepsea.mua.app.im.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.GiftBean;

import java.util.List;

/**
 * Created by hanhailong on 2017/8/20.
 */

public class GiftChatAdapter extends RecyclerView.Adapter<GiftChatAdapter.ViewHolder> {
    private OnMyItemClickListener myClickListener;

    public void setMyClickListener(OnMyItemClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface OnMyItemClickListener {

        void onItemClick(View view, int pos);
    }

    private final List<GiftBean> mDataList;
    private final LayoutInflater mLayoutInflater;
    private final int mItemWidth;

    public GiftChatAdapter(Context context, List<GiftBean> dataList, int itemWidth) {
        mDataList = dataList;
        mLayoutInflater = LayoutInflater.from(context);
        this.mItemWidth = itemWidth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = mLayoutInflater
                .inflate(R.layout.item_gift_chat, parent, false);

        return new ViewHolder(inflate, mItemWidth);
    }

    private int defaultPosition = -1;

    public int getDefaultPosition() {
        return defaultPosition;
    }

    public void setDefaultPosition(int defaultPosition) {
        this.defaultPosition = defaultPosition;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GiftBean giftBean = mDataList.get(position);
        holder.bindData(giftBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myClickListener != null) {
                    myClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
        holder.tv_gift_name.setText(giftBean.getGift_name());
        holder.tv_gift_price.setText(giftBean.getGift_coin()+"玫瑰");
        holder.tv_gift_number.setText(giftBean.getGift_number()+"");
        GlideUtils.loadImage(holder.iv_gift_photo, giftBean.getGift_image());
        if (defaultPosition == position) {
            holder.rl_group.setBackgroundResource(R.drawable.bg_gift_selected);
        } else {
            holder.rl_group.setBackgroundResource(R.drawable.bg_gift_unselected);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_gift_photo;
        private TextView tv_gift_name;
        private TextView tv_gift_price;
        private TextView tv_gift_number;
        private RelativeLayout rl_group;

        public ViewHolder(View itemView, int itemWidth) {
            super(itemView);

            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            layoutParams.width = itemWidth;
            iv_gift_photo = itemView.findViewById(R.id.iv_gift_photo);
            tv_gift_name = itemView.findViewById(R.id.tv_gift_name);
            tv_gift_number = itemView.findViewById(R.id.tv_gift_number);
            tv_gift_price = itemView.findViewById(R.id.tv_gift_price);
            rl_group = itemView.findViewById(R.id.rl_group);
        }

        public void bindData(GiftBean itemData) {
            if (itemData != null) {
                itemView.setVisibility(View.VISIBLE);
            } else {
                itemView.setVisibility(View.GONE);
            }
        }
    }

}
