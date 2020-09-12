package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MemberSearchAdapter;
import com.deepsea.mua.voice.adapter.RoomSearchAdapter;
import com.deepsea.mua.voice.databinding.ActivitySearchMoreBinding;
import com.deepsea.mua.voice.viewmodel.RoomSearchViewModel;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/19
 */
public class SearchMoreActivity extends BaseActivity<ActivitySearchMoreBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private RoomSearchViewModel mViewModel;
    @Inject
    RoomJoinController mRoomJump;

    private RoomSearchAdapter mRoomAdapter;
    private MemberSearchAdapter mMemberAdapter;

    //1 查看用户 2 查看房间
    private String mSearchType;

    public static Intent newIntent(Context context, String searchType) {
        Intent intent = new Intent(context, SearchMoreActivity.class);
        intent.putExtra("searchType", searchType);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        mSearchType = intent.getStringExtra("searchType");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("searchType", mSearchType);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search_more;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RoomSearchViewModel.class);
        mBinding.titleBar.setTitle(TextUtils.equals("1", mSearchType) ? "更多用户" : "更多房间");
        initSearchRv();
        getMore();
    }

    private void initSearchRv() {
        mRoomAdapter = new RoomSearchAdapter(mContext);
        mRoomAdapter.setOnItemClickListener((view, position) -> {
            String roomId = mRoomAdapter.getItem(position).getRoom_id();
            mRoomJump.startJump(roomId, mContext);
        });
        mMemberAdapter = new MemberSearchAdapter(mContext);
        mMemberAdapter.setOnItemClickListener((view, position) -> {
            String state=mMemberAdapter.getItem(position).getState();
            String roomId=mMemberAdapter.getItem(position).getRoom_id();
            if (!TextUtils.isEmpty(state) && (state.equals("4") || state.equals("5"))) {
                mRoomJump.startJump(roomId,mContext);
            }else {
                String uid = mMemberAdapter.getItem(position).getUser_id();
                PageJumpUtils.jumpToProfile(uid);
            }
        });
        mBinding.searchRv.setNestedScrollingEnabled(false);
        mBinding.searchRv.setHasFixedSize(true);
        mBinding.searchRv.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void getMore() {
        mViewModel.getmoremsg(mSearchType).observe(this,
                new BaseObserver<RoomSearchs>() {
                    @Override
                    public void onSuccess(RoomSearchs result) {
                        boolean hasMembers = result.getMember_list() != null && !result.getMember_list().isEmpty();
                        if (hasMembers) {
                            mBinding.searchRv.setAdapter(mMemberAdapter);
                            mMemberAdapter.setNewData(result.getMember_list());
                        }
                        boolean hasRooms = result.getRoom_list() != null && !result.getRoom_list().isEmpty();
                        if (hasRooms) {
                            mBinding.searchRv.setAdapter(mRoomAdapter);
                            mRoomAdapter.setNewData(result.getRoom_list());
                        }
                        ViewBindUtils.setVisible(mBinding.emptyView, !hasMembers && !hasRooms);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        mRoomJump.destroy();
        super.onDestroy();
    }
}
