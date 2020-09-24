package com.deepsea.mua.voice.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.view.xtablayout.XTabLayout;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.dialog.SexEditDialog;
import com.deepsea.mua.stub.dialog.UnauthorizedDialog;
import com.deepsea.mua.stub.entity.IsCreateRoomVo;
import com.deepsea.mua.stub.entity.RoomModes;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.CitySortEvent;
import com.deepsea.mua.stub.utils.eventbus.GotoShowEvent;
import com.deepsea.mua.stub.utils.eventbus.MarqueeEvent;
import com.deepsea.mua.stub.utils.eventbus.ShowRankStepOne;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.activity.RoomCreateNewActivity;
import com.deepsea.mua.voice.activity.RoomSearchActivity;
import com.deepsea.mua.voice.adapter.VoiceVpAdapter;
import com.deepsea.mua.voice.databinding.FragmentVoiceBinding;
import com.deepsea.mua.voice.viewmodel.HomeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/22
 */
public class VoiceFragment extends BaseFragment<FragmentVoiceBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private HomeViewModel mViewModel;

    private VoiceVpAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_voice;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventBus(this);
    }


    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(HomeViewModel.class);
        initViewPager();
        String marqueeData = AppConstant.getInstance().getMarquee();
        if (!TextUtils.isEmpty(marqueeData)) {
            mBinding.rlMarquee.setVisibility(View.VISIBLE);
            mBinding.tvMarquee.setMarqueeNum(-1);
            mBinding.tvMarquee.setText(marqueeData);

        } else {
            mBinding.rlMarquee.setVisibility(View.GONE);
            mBinding.tvMarquee.setMarqueeNum(0);
            mBinding.tvMarquee.setText("暂无公告");
        }
        getBanners();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MarqueeEvent event) {
        if (!TextUtils.isEmpty(event.content)) {
            mBinding.rlMarquee.setVisibility(View.VISIBLE);
            mBinding.tvMarquee.setMarqueeNum(-1);
            mBinding.tvMarquee.setText(event.content);
        } else {
            mBinding.rlMarquee.setVisibility(View.GONE);
            mBinding.tvMarquee.setMarqueeNum(0);
            mBinding.tvMarquee.setText("暂无公告");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GotoShowEvent event) {
        jumpToMineRooms();
    }

    @Override
    protected void initListener() {

        subscribeClick(mBinding.llCreateRoom, o -> {
            String sex = UserUtils.getUser().getSex();
            if (sex.equals("0")) {
                SexEditDialog sexEditDialog = new SexEditDialog(mContext);
                sexEditDialog.show();
            } else {
                jumpToMineRooms();
            }
        });
        subscribeClick(mBinding.ivSearch, o -> {
            startActivity(new Intent(mContext, RoomSearchActivity.class));
        });
        subscribeClick(mBinding.llFilter, o -> {
            PageJumpUtils.jumpScreenAreaDialog(getActivity());
        });
    }

    XTabLayout.Tab lastTab = null;

    private void initViewPager() {
        mAdapter = new VoiceVpAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setOffscreenPageLimit(5);
//        mBinding.tabLayout.addOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(XTabLayout.Tab tab) {
//                if (tab.getPosition() == 0) {
//                    tab.setTextColor(getResources().getColor(R.color.color_FFFF3D42));
//                    mBinding.tabLayout.setSelectedTabIndicatorColor(mContext.getResources().getColor(R.color.color_FFFF3D42));
//                } else if (tab.getPosition()==1){
//                    tab.setTextColor(getResources().getColor(R.color.color_FF9351FE));
//                    mBinding.tabLayout.setSelectedTabIndicatorColor(mContext.getResources().getColor(R.color.color_FF9351FE));
//                    XTabLayout.Tab tab0= mBinding.tabLayout.getTabAt(0);
//                    tab0.getmView().getmTextView().setTextColor(getResources().getColor(R.color.color_FF656565));
//                    setUnderLineDefault(1);
//                }else {
//                    tab.setTextColor(getResources().getColor(R.color.color_FFCE58FA));
//                    mBinding.tabLayout.setSelectedTabIndicatorColor(mContext.getResources().getColor(R.color.color_FFCE58FA));
//                }
//                setUnderLineDefault(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(XTabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(XTabLayout.Tab tab) {
//            }
//        });
    }
//    private void  setUnderLineDefault(int position){
//        for (int i=0;i<mBinding.tabLayout.getTabCount();i++){
//            if (i!=position){
//                XTabLayout.Tab tab= mBinding.tabLayout.getTabAt(i);
//                tab.getmView().getmTextView().setTextColor(getResources().getColor(R.color.color_FF656565));
//            }
//        }
//    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.RequestCode.AREA_CODE) {
                String city = data.getStringExtra(Constant.Province);
                String city_two = data.getStringExtra(Constant.City);
                String city_three = data.getStringExtra(Constant.AREA);
                String age = data.getStringExtra(Constant.Age);
                CitySortEvent citySortEvent = new CitySortEvent();
                citySortEvent.setCity(city);
                citySortEvent.setCity_two(city_two);
                citySortEvent.setCity_three(city_three);
                citySortEvent.setAge(age);
                EventBus.getDefault().post(citySortEvent);
            }
        }
    }

    private void jumpToMineRooms() {
        showProgress();
        mViewModel.create().observe(this,
                new BaseObserver<VoiceRoomBean.RoomInfoBean>() {
                    @Override
                    public void onSuccess(VoiceRoomBean.RoomInfoBean result) {
                        hideProgress();
                        Intent intent = new Intent(mContext, RoomCreateNewActivity.class);
                        intent.putExtra("roomInfo", result);
                        startActivity(intent);
                    }

                    @Override
                    public void onError(String msg, int code) {
                        hideProgress();
                        if (code == 900) {
                            new UnauthorizedDialog(mContext).show();
                        } else {
                            toastShort(msg);
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        getRoomModes();
        isCreateRoom();
    }

    private void isCreateRoom() {
        mViewModel.iscreateroom().observe(this, new BaseObserver<IsCreateRoomVo>() {
            @Override
            public void onSuccess(IsCreateRoomVo result) {
                if (result != null) {
                    setRoomParam(result);
                }
            }
        });
    }

    private void setRoomParam(IsCreateRoomVo vo) {
        int isShow = vo.getIs_show();
        int type = vo.getType();
        if (isShow == 1) {
            ViewBindUtils.setVisible(mBinding.llCreateRoom, true);
            ViewBindUtils.setVisible(mBinding.llFilter, false);

        } else {
            ViewBindUtils.setVisible(mBinding.llCreateRoom, false);
            ViewBindUtils.setVisible(mBinding.llFilter, true);

        }

    }


    private void getRoomModes() {
        if (mAdapter.getCount() != 0)
            return;

        mViewModel.getRoomModes().observe(this, new BaseObserver<RoomModes>() {
            @Override
            public void onSuccess(RoomModes result) {
                if (result != null && result.getRoom_mode() != null) {
                    List<RoomModes.RoomModeBean> list = result.getRoom_mode();
                    mAdapter.setNewData(list);
                    mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
            }
        });
    }

    public void getBanners() {
        mViewModel.getBanners().observe(this, new BaseObserver<List<VoiceBanner.BannerListBean>>() {
            @Override
            public void onSuccess(List<VoiceBanner.BannerListBean> result) {
                if (result != null) {
                    setBanner(result);
                }
            }
        });
    }

    private void setBanner(List<VoiceBanner.BannerListBean> list) {
        List<View> views = new ArrayList<>();
        mBinding.banner.initCoefficient(list.size());
        for (int i = 0; i < list.size() * mBinding.banner.getCoefficient(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            int index = i % list.size();
            GlideUtils.roundImage(imageView, list.get(index).getImage(), R.drawable.ic_place, R.drawable.ic_place, 10);
            imageView.setOnClickListener(v -> {
                VoiceBanner.BannerListBean bean = list.get(index);
                if (bean.getLink_type().equals("3")) {
                    switch (bean.getUi_type()) {
                        case "2":
                            EventBus.getDefault().post(new ShowRankStepOne(3));
                            break;
                        default:
                            break;
                    }

                } else {
                    PageJumpUtils.jumpToWeb(bean.getTitle(), bean.getLinkurl());
                }

            });
            views.add(imageView);
        }
        mBinding.banner.addView(views);
        mBinding.banner.setNavLayoutVisible(list.size() > 1);
        if (list.size() > 1) {
            mBinding.banner.startPlay(true);
        }
    }

}
