package com.deepsea.mua.voice.fragment;

import android.os.Bundle;
import android.view.View;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RankVpAdapter;
import com.deepsea.mua.voice.databinding.FragmentRoomRankBinding;

/**
 * Created by JUN on 2019/4/10
 */
public class RoomRankFragment extends BaseFragment<FragmentRoomRankBinding> {

    private RankVpAdapter mAdapter;

    //1 财富榜 2 魅力榜
    private String mType;
    //房间id
    private String mRoomId;

    public static RoomRankFragment newInstance(String type, String roomId) {
        RoomRankFragment instance = new RoomRankFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("type", type);
            bundle.putString("roomId", roomId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("type", type);
            bundle.putString("roomId", roomId);
        }
        return instance;
    }

    @Override
    protected boolean isLazyView() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_room_rank;
    }

    @Override
    protected void initView(View view) {
        mType = mBundle.getString("type");
        mRoomId = mBundle.getString("roomId");
        initViewPager();
    }

    private void initViewPager() {
        mAdapter = new RankVpAdapter(getChildFragmentManager(), mType, mRoomId);
        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
    }
}
