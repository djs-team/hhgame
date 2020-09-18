package com.deepsea.mua.voice.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.ViewPager;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomRankVpAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomRankBinding;
import com.deepsea.mua.voice.databinding.LayoutRoomRankBinding;

/**
 * Created by JUN on 2019/4/10
 * 房间内榜单
 */
public class RoomRankActivity extends BaseActivity<ActivityRoomRankBinding> {

    private RoomRankVpAdapter mAdapter;

    private String roomId;

    private LayoutRoomRankBinding mStubBinding;

    public static Intent newIntent(Context context, String roomId) {
        Intent intent = new Intent(context, RoomRankActivity.class);
        intent.putExtra("roomId", roomId);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        roomId = intent.getStringExtra("roomId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_rank;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.viewStub.setOnInflateListener((stub, inflated) -> {
            mStubBinding = DataBindingUtil.bind(inflated);
            if (mStubBinding != null) {
                mStubBinding.backIv.setOnClickListener(v -> finish());
                initViewPager();
            }
        });

        if (!mBinding.viewStub.isInflated() && mBinding.viewStub.getViewStub() != null) {
            mBinding.viewStub.getViewStub().inflate();
        }
    }

    private void initViewPager() {
        mAdapter = new RoomRankVpAdapter(getSupportFragmentManager(), roomId);
        mStubBinding.viewPager.setAdapter(mAdapter);
        mStubBinding.viewPager.setNoScroll(true);
        mStubBinding.tabLayout.setViewPager(mStubBinding.viewPager);

        mStubBinding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int i) {
                int backgroundRes = i == 0 ? R.drawable.rank_wealth_bg : R.drawable.rank_heart_bg;
                mStubBinding.getRoot().setBackgroundResource(backgroundRes);
                mStubBinding.tabLayout.setSelectTextColor(i == 0 ? 0xFFE15EA9 : 0xFFFFA067);
            }
        });
    }
}
