package com.deepsea.mua.app.im.mua;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendAddAdapter;
import com.deepsea.mua.app.im.adapter.GiftPanelAdapter;
import com.deepsea.mua.app.im.databinding.FragmentGiftPanelBinding;
import com.deepsea.mua.app.im.viewmodel.GiftPanelViewModel;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AddFriendWithGiftDialog;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class GiftPanelFragment extends BaseFragment<FragmentGiftPanelBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private GiftPanelViewModel mViewModel;

    private String touid;
    private String toUserName;
    private static FriendAddAdapter.OnFriendAddListener mListener;
    String is_room;

    public static BaseFragment newInstance(String touid, String toUserName, String is_room, FriendAddAdapter.OnFriendAddListener onFriendAddListener) {
        GiftPanelFragment instance = new GiftPanelFragment();
        mListener = onFriendAddListener;
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("touid", touid);
            bundle.putString("toUserName", toUserName);
            bundle.putString("is_room", is_room);
            instance.setArguments(bundle);
        } else {
            bundle.putString("touid", touid);
            bundle.putString("toUserName", toUserName);
            bundle.putString("is_room", is_room);
        }
        return instance;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift_panel;
    }

    @Override
    protected void initView(View view) {
        touid = mBundle.getString("touid");
        toUserName = mBundle.getString("toUserName");
        is_room = mBundle.getString("is_room");
        if (!is_room.equals("1")) {
            is_room = "2";
        }

        mViewModel = ViewModelProviders.of(this, mModelFactory).get(GiftPanelViewModel.class);
        mViewModel.refresh(is_room).observe(this, new BaseObserver<GiftListBean>() {
            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }

            @Override
            public void onSuccess(GiftListBean result) {
                List<GiftBean> data = result.getList();
                if (data != null && data.size() > 0) {
                    initViewPager(data, 3, 4);
                }
            }
        });
    }

    private void initViewPager(List<GiftBean> datas, int rowNum, int spanNum) {
        //1.根据数据的多少来分页，每页的数据为rw
        int singlePageDatasNum = rowNum * spanNum;//每个单页包含的数据量：2*4=8；
        int pageNum = datas.size() / singlePageDatasNum;//算出有几页菜单：20%8 = 3;
        if (datas.size() % singlePageDatasNum > 0) pageNum++;//如果取模大于0，就还要多一页出来，放剩下的不满项
        ArrayList<RecyclerView> mList = new ArrayList<>();
        for (int i = 0; i < pageNum; i++) {
            RecyclerView recyclerView = new RecyclerView(getActivity());
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanNum);
            recyclerView.setLayoutManager(gridLayoutManager);
            int fromIndex = i * singlePageDatasNum;
            int toIndex = (i + 1) * singlePageDatasNum;
            if (toIndex > datas.size()) toIndex = datas.size();
            //a.截取每个页面包含数据
            ArrayList<GiftBean> menuItems = new ArrayList(datas.subList(fromIndex, toIndex));
            //b.设置每个页面的适配器数据
            int screenWidth = ScreenUtils.getScreenWidth(getActivity());
            int space = ScreenUtils.dip2px(getActivity(), 22) / spanNum;
            int itemWidth = screenWidth / spanNum - space;
            //setAdapter
            GiftPanelAdapter adapter = new GiftPanelAdapter(getActivity(), menuItems, itemWidth);
            int finalI = i;
            adapter.setMyClickListener(new GiftPanelAdapter.OnMyItemClickListener() {
                @Override
                public void onItemClick(View view, int pos) {
                    GiftBean giftBean = menuItems.get(pos);
                    showGiftDialog(giftBean.getGift_id(), giftBean.getGift_image());
                }
            });
            recyclerView.setAdapter(adapter);
            mList.add(recyclerView);
        }
        //2.ViewPager的适配器
        GiftPanelFragment.MenuViewPagerAdapter menuViewPagerAdapter = new GiftPanelFragment.MenuViewPagerAdapter(mList);
        mBinding.vPager.setAdapter(menuViewPagerAdapter);
        mBinding.vPager.setOffscreenPageLimit(pageNum - 1);
        mBinding.vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                mBinding.pageIndicator.setCurrentPage(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        mBinding.pageIndicator.setPageCount(pageNum);
    }

    class MenuViewPagerAdapter extends PagerAdapter {
        private ArrayList<RecyclerView> mList;

        public MenuViewPagerAdapter(ArrayList<RecyclerView> mList) {
            this.mList = mList;
        }

        @Override
        public int getCount() {
            return mList.isEmpty() ? 0 : mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }
    }

    AddFriendWithGiftDialog dialog = null;

    private void showGiftDialog(String giftId, String giftPic) {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AddFriendWithGiftDialog(mContext);
        dialog.setContent("送给" + toUserName, giftPic);
        dialog.setRightButton("送礼加好友", new AddFriendWithGiftDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog d) {
                String inputMsg = dialog.getInputMsg();
//                if (TextUtils.isEmpty(inputMsg)) {
//                    ToastUtils.showToast("请输入礼物寄语");
//                    return;
//                }
                dialog.dismiss();
                showProgress("请稍后");
                addFriendWithGift(giftId, inputMsg);
            }
        });
        dialog.show();
    }

    private void addFriendWithGift(String giftId, String friendmsg) {
        Map<String, String> map = new HashMap<>();
        map.put("touid", touid);
        map.put("gift_id", giftId);
        map.put("friendmsg", friendmsg);
        map.put("type", "1");
        onAddFriendEvent(map);
    }

    private void onAddFriendEvent(Map<String, String> map) {
        mViewModel.addFriendly(map).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                if (result.getCode() == 200) {
                    if (mListener != null) {
                        mListener.onSendRequest();
                    }
                    toastShort("发送成功");
                } else {
                    toastShort(result.getDesc());
                }
            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                toastShort(msg);
            }
        });
    }

}


