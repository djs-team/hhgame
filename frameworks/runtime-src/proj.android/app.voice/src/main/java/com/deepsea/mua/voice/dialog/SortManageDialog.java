package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.databinding.DialogSortManageBinding;

import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class SortManageDialog extends BaseDialog<DialogSortManageBinding> {

    private MicroSortAdapter.OnMicroListener mListener;

    private SortPageAdapter mAdapter;
    private int defaultTab = 1;

    public SortManageDialog(@NonNull Context context, int tab) {
        super(context);
        initViewPager();
        this.defaultTab = tab;
        mBinding.viewPager.setCurrentItem(defaultTab - 1);
        setOnDismissListener(dialog -> mBinding.viewPager.setCurrentItem(0));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_sort_manage;
    }

    public void setOnMicroListener(MicroSortAdapter.OnMicroListener listener) {
        this.mListener = listener;
    }

    private void initViewPager() {
        mAdapter = new SortPageAdapter();
        mAdapter.setOnMicroListener(new MicroSortAdapter.OnMicroListener() {
            @Override
            public void onTopMicro(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onTopMicro(uid);
                }
            }

            @Override
            public void onOnWheat(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onOnWheat(uid);
                }
            }

            @Override
            public void onRemove(String uid) {
                dismiss();
                if (mListener != null) {
                    mListener.onRemove(uid);
                }
            }
        });
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
    }


    public void setNewData(List<List<MicroOrder>> lists) {
        mAdapter.setNewData(lists);
    }

    private static class SortPageAdapter extends PagerAdapter {

        private final String[] ONLINE_TITLES = {"男嘉宾", "女嘉宾"};
        private SparseArray<RecyclerView> views = new SparseArray<>();

        private MicroSortAdapter.OnMicroListener mListener;

        private List<List<MicroOrder>> mDatas;

        @Override
        public int getCount() {
            return ONLINE_TITLES.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public void setNewData(List<List<MicroOrder>> lists) {
            this.mDatas = lists;
            notifyDataSetChanged();
        }

        public List<MicroOrder> getItem(int position) {
            if (!CollectionUtils.isEmpty(mDatas) && position < mDatas.size()) {
                return mDatas.get(position);
            }
            return null;
        }

        public void setOnMicroListener(MicroSortAdapter.OnMicroListener listener) {
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
                itemView.setLayoutManager(new LinearLayoutManager(container.getContext()));
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                itemView.setOverScrollMode(View.OVER_SCROLL_NEVER);
                if (itemView.getItemAnimator() != null) {
                    itemView.getItemAnimator().setChangeDuration(0);
                }
                views.put(position, itemView);

                MicroSortAdapter adapter = new MicroSortAdapter(container.getContext());
                adapter.setSex(position == 0 ? "1" : "2");
                adapter.setManager(true);
                adapter.setOnMicroListener(mListener);
                itemView.setAdapter(adapter);
            }

            if (itemView.getAdapter() instanceof MicroSortAdapter) {
                MicroSortAdapter adapter = (MicroSortAdapter) itemView.getAdapter();
                adapter.setNewData(getItem(position));
            }

            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return ONLINE_TITLES[position];
        }
    }
}
