package com.deepsea.mua.app.im.mua;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendAddAdapter;
import com.deepsea.mua.app.im.adapter.GiftKnapsackAdapter;
import com.deepsea.mua.app.im.databinding.FragmentGiftPanelBinding;
import com.deepsea.mua.app.im.viewmodel.GiftKnapsackModel;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AddFriendWithGiftDialog;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.luck.picture.lib.tools.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

//import com.deepsea.mua.app.im.utils.GridPagerSnapHelper;

/**
 * Created by JUN on 2019/5/5
 */
public class GiftKnapsackFragment extends BaseFragment<FragmentGiftPanelBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private GiftKnapsackModel mViewModel;
    private String touid;
    private String toUserName;
    private  static FriendAddAdapter.OnFriendAddListener mListener;
    public static BaseFragment newInstance(String touid, String toUserName, FriendAddAdapter.OnFriendAddListener onFriendAddListener) {
        GiftKnapsackFragment instance = new GiftKnapsackFragment();
        mListener=onFriendAddListener;
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("touid", touid);
            bundle.putString("toUserName", toUserName);
            instance.setArguments(bundle);
        } else {
            bundle.putString("touid", touid);
            bundle.putString("toUserName", toUserName);
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
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(GiftKnapsackModel.class);
        mViewModel.getMePacks().observe(this, new BaseObserver<List<PackBean>>() {
            @Override
            public void onSuccess(List<PackBean> list) {
                if (list != null && list.size() > 0) {
                    initViewPager(list, 3, 4);
                }
            }
            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });

    }

    /**
     * @param rowNum  行数
     * @param spanNum 列数
     */
    private void initViewPager(List<PackBean> datas, int rowNum, int spanNum) {
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
            ArrayList<PackBean> menuItems = new ArrayList(datas.subList(fromIndex, toIndex));
            //b.设置每个页面的适配器数据
            int screenWidth = ScreenUtils.getScreenWidth(getActivity());
            int space = ScreenUtils.dip2px(getActivity(), 22) / spanNum;
            int itemWidth = screenWidth / spanNum - space;
            GiftKnapsackAdapter adapter = new GiftKnapsackAdapter(getActivity(), menuItems, itemWidth);
            adapter.setMyClickListener(new GiftKnapsackAdapter.OnMyItemClickListener() {
                @Override
                public void onItemClick(View view, int pos) {
                    PackBean giftBean = menuItems.get(pos);
                    showGiftDialog(adapter,pos,giftBean.getGift_id(), giftBean.getGift_image());
                }
            });
            recyclerView.setAdapter(adapter);
            mList.add(recyclerView);
        }
        //2.ViewPager的适配器
        MenuViewPagerAdapter menuViewPagerAdapter = new MenuViewPagerAdapter(mList);
        mBinding.vPager.setAdapter(menuViewPagerAdapter);
        //3.动态设置ViewPager的高度，并加载所有页面

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

    private void showGiftDialog(GiftKnapsackAdapter adapter,int position,String giftId, String giftPic) {
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
                addFriendWithGift( adapter, position,giftId, inputMsg);
            }
        });
        dialog.show();
    }
    private void addFriendWithGift(GiftKnapsackAdapter adapter,int position,String giftId, String friendmsg) {
        Map<String, String> map = new HashMap<>();
        map.put("touid", touid);
        map.put("gift_id", giftId);
        map.put("friendmsg", friendmsg);
        map.put("type", "2");
        onAddFriendEvent(adapter,position,map);
    }

    private void onAddFriendEvent(GiftKnapsackAdapter adapter,int position,Map<String, String> map) {
        mViewModel.addFriendly(map).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                dialog.dismiss();
                toastShort( result.getDesc());
                if (result.getCode()==200){
                    if (mListener!=null){
                        mListener.onSendRequest();
                    }
                    PackBean bean= adapter.getDataList().get(position);
                    bean.setPack_num(String.valueOf(Integer.valueOf(bean.getPack_num())-1));
                    adapter.notifyItemChanged(position);
                }

            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }
}


