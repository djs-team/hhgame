package com.deepsea.mua.voice.view.present;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemRoomGiftBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by JUN on 2019/7/25
 */
public class MePackView extends ViewPager {

    private PackAdapter mVpAdapter;

    public MePackView(@NonNull Context context) {
        this(context, null);
    }

    public MePackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setOverScrollMode(OVER_SCROLL_NEVER);
        mVpAdapter = new PackAdapter();
        setAdapter(mVpAdapter);
    }

    public void setData(List<GiftBean> list) {
        List<List<GiftBean>> datas = new ArrayList<>();
        if (list != null) {
            int index = 0;
            int singleCount = 8;
            while (index < list.size()) {
                if (index + singleCount <= list.size()) {
                    datas.add(list.subList(index, index + singleCount));
                    index += singleCount;
                } else {
                    List<GiftBean> subList = list.subList(index, list.size());
                    datas.add(subList);
                    index += subList.size();
                }
            }
        }

        mVpAdapter.setNewData(datas);
    }

    public int getCount() {
        return mVpAdapter.getCount();
    }

    public void setOnSelectedListener(OnSelectedListener<GiftBean> listener) {
        mVpAdapter.setOnSelectedListener(listener);
    }

    public void releaseSelected() {
        mVpAdapter.releaseSelected();
    }

    public GiftBean getSelected() {
        return mVpAdapter.getSelectBean();
    }

    private static class PackAdapter extends PagerAdapter {

        private SparseArray<RecyclerView> views;
        private List<List<GiftBean>> mDatas;

        private GiftBean mSelectBean;
        private OnSelectedListener<GiftBean> mListener;

        public PackAdapter() {
            views = new SparseArray<>();
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setNewData(List<List<GiftBean>> list) {
            mDatas = list;
            notifyDataSetChanged();
        }

        public void setOnSelectedListener(OnSelectedListener<GiftBean> listener) {
            this.mListener = listener;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            RecyclerView itemView = null;
            if (position < views.size()) {
                itemView = views.get(position);
            }

            if (itemView == null) {
                itemView = new RecyclerView(container.getContext());
                itemView.setLayoutManager(new GridLayoutManager(container.getContext(), 4));
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemView.setNestedScrollingEnabled(false);
                itemView.setHasFixedSize(true);
                itemView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                if (itemView.getItemAnimator() != null) {
                    itemView.getItemAnimator().setChangeDuration(0);
                }
                views.put(position, itemView);

                PackRvAdapter adapter = new PackRvAdapter(container.getContext());
                adapter.setOnItemClickListener((view, index) -> onRvItemClick(position, index));
                itemView.setAdapter(adapter);

//                int defPos = 0;
//                if (position == defPos) {
//                    List<PackBean> list = mDatas.get(defPos);
//                    if (!list.isEmpty()) {
//                        mSelectBean = list.get(defPos);
//                    }
//                    adapter.setSelectPos(defPos);
//                }
            }

            PackRvAdapter adapter = (PackRvAdapter) itemView.getAdapter();
            adapter.setNewData(mDatas.get(position));

            container.addView(itemView);
            return itemView;
        }

        /**
         * RecyclerView点击事件
         *
         * @param position ViewPager item position
         * @param index    RecyclerView position
         */
        private void onRvItemClick(int position, int index) {
            for (int i = 0; i < views.size(); i++) {
                RecyclerView recyclerView = views.get(i);
                PackRvAdapter adapter = (PackRvAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    if (position == i) {
                        adapter.setSelectPos(index);
                        mSelectBean = adapter.getItem(index);
                    } else {
                        adapter.setSelectPos(-1);
                    }
                }
            }

            if (mListener != null) {
                mListener.onSelected(mSelectBean);
            }
        }

        public void releaseSelected() {
            mSelectBean = null;
            onRvItemClick(-1, -1);
        }

        public GiftBean getSelectBean() {
            for (int i = 0; i < views.size(); i++) {
                RecyclerView recyclerView = views.get(i);
                PackRvAdapter adapter = (PackRvAdapter) recyclerView.getAdapter();
                if (adapter != null && adapter.getSelectPos() != -1) {
                    return adapter.getItem(adapter.getSelectPos());
                }
            }
            return null;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    private static class PackRvAdapter extends BaseBindingAdapter<GiftBean, ItemRoomGiftBinding> {

        private int mSelectPos = -1;

        public PackRvAdapter(Context context) {
            super(context);
        }

        public void setSelectPos(int pos) {
            if (mSelectPos != pos) {
                if (mSelectPos >= 0 && mSelectPos < getItemCount()) {
                    int index = mSelectPos;
                    mSelectPos = -1;
                    notifyItemChanged(index);
                }
                mSelectPos = pos;
                if (mSelectPos >= 0 && mSelectPos < getItemCount()) {
                    notifyItemChanged(mSelectPos);
                }
            }
        }

        public int getSelectPos() {
            if (mSelectPos >= 0 && mSelectPos < getItemCount()) {
                return mSelectPos;
            }
            return -1;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_room_gift;
        }

        @Override
        protected void bind(BindingViewHolder<ItemRoomGiftBinding> holder, GiftBean item) {
            GlideUtils.loadImage(holder.binding.giftIv, item.getGift_image());
            holder.binding.giftNameTv.setText(item.getGift_name());
            holder.binding.giftPriceTv.setText(String.format(Locale.CHINA, "%s玫瑰", item.getGift_coin()));
            ViewBindUtils.setVisible(holder.binding.heartTv, true);
            int position = holder.getAdapterPosition();
            holder.binding.getRoot().setSelected(mSelectPos == position);
        }
    }
}
