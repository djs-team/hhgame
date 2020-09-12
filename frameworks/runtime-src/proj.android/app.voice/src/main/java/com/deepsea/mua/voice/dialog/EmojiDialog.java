package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogEmojiBinding;
import com.deepsea.mua.voice.databinding.ItemEmojiBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by JUN on 2019/4/26
 */
public class EmojiDialog extends BaseDialog<DialogEmojiBinding> {

    private EmojiVpAdapter mVpAdapter;

    public EmojiDialog(@NonNull Context context) {
        super(context);
        initViewPager();
    }

    @Override
    protected float getDimAmount() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_emoji;
    }

    private void initViewPager() {
        mVpAdapter = new EmojiVpAdapter();
        mBinding.viewPager.setAdapter(mVpAdapter);
        mBinding.indicator.setViewPager(mBinding.viewPager);
    }

    public void setEmojiData(List<EmojiBean.EmoticonBean> list, boolean isOnMicro, boolean isSending) {
        List<List<EmojiBean.EmoticonBean>> datas = new ArrayList<>();
        if (list != null) {
            if (!isOnMicro) {
                Iterator<EmojiBean.EmoticonBean> iterator = list.iterator();
                while (iterator.hasNext()) {
                    EmojiBean.EmoticonBean next = iterator.next();
                    if (TextUtils.equals(next.getType(), "2"))
                        iterator.remove();
                }
            }

            int index = 0;
            int singleCount = 18;
            while (index < list.size()) {
                if (index + singleCount <= list.size()) {
                    datas.add(list.subList(index, index + singleCount));
                    index += singleCount;
                } else {
                    List<EmojiBean.EmoticonBean> subList = list.subList(index, list.size());
                    datas.add(subList);
                    index += subList.size();
                }
            }
        }
        mVpAdapter.setNewData(datas, isOnMicro, isSending);
    }

    public void setSending(boolean isSending) {
        mVpAdapter.setSending(isSending);
    }

    public void setOnEmojioCheckedListener(EmojiVpAdapter.OnEmojioCheckedListener listener) {
        mVpAdapter.setOnEmojioCheckedListener(listener);
    }

    public static class EmojiVpAdapter extends PagerAdapter {

        private SparseArray<RecyclerView> views;
        private List<List<EmojiBean.EmoticonBean>> mDatas;
        private OnEmojioCheckedListener mEmojiListener;
        private boolean isSending;
        private boolean isOnMicro;

        public interface OnEmojioCheckedListener {

            void onEmojiChecked(EmojiBean.EmoticonBean emoji);
        }

        public EmojiVpAdapter() {
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

        public void setNewData(List<List<EmojiBean.EmoticonBean>> list, boolean isOnMicro, boolean isSending) {
            this.mDatas = list;
            this.isOnMicro = isOnMicro;
            this.isSending = isSending;
            notifyDataSetChanged();
        }

        public void setSending(boolean isSending) {
            this.isSending = isSending;
            notifyDataSetChanged();
        }

        public void setOnEmojioCheckedListener(OnEmojioCheckedListener listener) {
            this.mEmojiListener = listener;
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
                itemView.setLayoutManager(new GridLayoutManager(container.getContext(), 6));
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemView.setNestedScrollingEnabled(false);
                itemView.setHasFixedSize(true);
                itemView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                if (itemView.getItemAnimator() != null) {
                    itemView.getItemAnimator().setChangeDuration(0);
                }
                views.put(position, itemView);

                EmojiRvAdapter adapter = new EmojiRvAdapter(container.getContext());
                adapter.setOnItemClickListener((view, index) -> onRvItemClick(adapter.getItem(index)));
                itemView.setAdapter(adapter);
            }

            EmojiRvAdapter adapter = (EmojiRvAdapter) itemView.getAdapter();
            adapter.setNewData(mDatas.get(position), isOnMicro, isSending);

            container.addView(itemView);
            return itemView;
        }

        /**
         * RecyclerView点击事件
         *
         * @param emoji 选中的表情
         */
        private void onRvItemClick(EmojiBean.EmoticonBean emoji) {
            if (mEmojiListener != null) {
                mEmojiListener.onEmojiChecked(emoji);
            }
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NotNull Object object) {
            return POSITION_NONE;
        }
    }

    public static class EmojiRvAdapter extends BaseBindingAdapter<EmojiBean.EmoticonBean, ItemEmojiBinding> {

        private boolean isSending;
        private boolean isOnMicro;

        public EmojiRvAdapter(Context context) {
            super(context);
        }

        public void setNewData(List<EmojiBean.EmoticonBean> data, boolean isOnMicro, boolean isSending) {
            this.isOnMicro = isOnMicro;
            this.isSending = isSending;
            super.setNewData(data);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_emoji;
        }

        @Override
        protected void bind(BindingViewHolder<ItemEmojiBinding> holder, EmojiBean.EmoticonBean item) {
            GlideUtils.loadImage(holder.binding.emojiIv, item.getFace_image());
            holder.binding.emojiName.setText(item.getFace_name());
            if (isSending || (!isOnMicro && TextUtils.equals(item.getType(), "2"))) {
                holder.binding.emojiIv.setAlpha(0.5F);
                holder.itemView.setEnabled(false);
            } else {
                holder.binding.emojiIv.setAlpha(1F);
                holder.itemView.setEnabled(true);
            }
        }
    }
}
