package com.deepsea.mua.app.im.mua;

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
import com.deepsea.mua.app.im.adapter.GiftChatAdapter;
import com.deepsea.mua.app.im.databinding.FragmentGiftChatBinding;
import com.deepsea.mua.app.im.viewmodel.GiftChatViewModel;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.GiftInfoBean;
import com.deepsea.mua.stub.entity.GiftListBean;
import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SignatureUtils;
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
public class GiftChatFragment extends BaseFragment<FragmentGiftChatBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private GiftChatViewModel mViewModel;
    private String touid;

    public interface OnItemGiftClickListener {
        void CallBack(boolean isSuccess, String giftName, String giftImg, String animation);
    }

    private OnItemGiftClickListener itemGiftClickListener;

    public void setItemGiftClickListener(OnItemGiftClickListener itemGiftClickListener) {
        this.itemGiftClickListener = itemGiftClickListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setInfo(String touid) {
        this.touid = touid;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gift_chat;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(GiftChatViewModel.class);

        mViewModel.refresh().observe(this, new BaseObserver<GiftInfoBean>() {
            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }

            @Override
            public void onSuccess(GiftInfoBean result) {
                List<GiftBean> data = result.getGift_info();
                if (data != null && data.size() > 0) {
                    initViewPager(data, 2, 4);
                }
            }
        });
        requestBalance();

    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.tvSend, o -> {
            if (selectGiftBean == null) {
                ToastUtils.showToast("请选择礼物");
                return;
            }
            addFriendWithGift(selectGiftBean.getGift_id(), selectGiftBean.getGift_name(), selectGiftBean.getGift_image(), selectGiftBean.getAnimation());
        });
        subscribeClick(mBinding.rlRecharge, o -> {
            checkParentLock();
        });
    }

    /**
     * @param rowNum  行数
     * @param spanNum 列数
     */
    private GiftChatAdapter lastAdapter;
    private int lastSelectPosition = -1;
    private GiftBean selectGiftBean = null;

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
            GiftChatAdapter adapter = new GiftChatAdapter(getActivity(), menuItems, itemWidth);
            int finalI = i;
            adapter.setMyClickListener(new GiftChatAdapter.OnMyItemClickListener() {
                @Override
                public void onItemClick(View view, int pos) {
                    int defaultPosition = adapter.getDefaultPosition();
                    if (lastSelectPosition != -1 && lastSelectPosition != finalI) {
                        if (lastAdapter != null) {
                            selectGiftBean = null;
                            lastAdapter.setDefaultPosition(-1);
                            lastAdapter.notifyDataSetChanged();
                        }
                    }
                    if (defaultPosition != pos)
                        selectGiftBean = menuItems.get(pos);
                    adapter.setDefaultPosition(pos);
                    adapter.notifyDataSetChanged();
                    lastSelectPosition = finalI;
                    lastAdapter = adapter;
                }
            });
            recyclerView.setAdapter(adapter);
            mList.add(recyclerView);
        }
        //2.ViewPager的适配器
        MenuViewPagerAdapter menuViewPagerAdapter = new MenuViewPagerAdapter(mList);
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

    private void addFriendWithGift(String giftId, String giftName, String giftImg, String animation) {
        Map<String, String> map = new HashMap<>();
        map.put("touid", touid);
        map.put("gift_id", giftId);
        map.put("signature", SignatureUtils.signByToken());
        onAddFriendEvent(map, giftName, giftImg, animation);
    }

    private void onAddFriendEvent(Map<String, String> map, String giftName, String giftImg, String animation) {
        mViewModel.sendGift(map).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (itemGiftClickListener != null) {
                    itemGiftClickListener.CallBack(true, giftName, giftImg, animation);
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    private void requestBalance() {
        mViewModel.wallet().observe(this, new BaseObserver<WalletBean>() {
            @Override
            public void onSuccess(WalletBean result) {
                if (result != null) {
                    balance = String.valueOf(result.getCoin());
                    mBinding.tvUserCount.setText(result.getCoin() + "");

                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private String balance;

    private void checkParentLock() {
        showProgress();
        mViewModel.checkSatus().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                PageJumpUtils.jumpToRecharge(balance + "");
            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                if (code == 204 || code == 205) {
                    toastShort(msg);
                } else {
                    super.onError(msg, code);
                }
            }
        });
    }
}


