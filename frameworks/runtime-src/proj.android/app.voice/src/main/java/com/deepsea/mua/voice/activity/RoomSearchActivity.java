package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;

import com.deepsea.mua.core.utils.UiUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.entity.RoomSearchs;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MemberSearchAdapter;
import com.deepsea.mua.voice.adapter.RoomSearchAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomSearchBinding;
import com.deepsea.mua.voice.viewmodel.RoomSearchViewModel;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/18
 */
public class RoomSearchActivity extends BaseActivity<ActivityRoomSearchBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private RoomSearchViewModel mViewModel;
    @Inject
    RoomJoinController mRoomJump;

    //最近访问
    private RoomSearchAdapter mAdapter;
    //模糊搜索
    private RoomSearchAdapter mRoomAdapter;
    private MemberSearchAdapter mMemberAdapter;

    //1模糊搜索 2个人搜索 3房间搜索
    private int mSearchType = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_search;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(RoomSearchViewModel.class);
        mViewModel.visited().observe(this, new BaseObserver<RoomSearchs>() {
            @Override
            public void onSuccess(RoomSearchs result) {
                if (result != null) {
                    mAdapter.setNewData(result.getRoom_msg());
                }
            }
        });
        initRecyclerView();
        initSearchRv();
        initSearchEdit();
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.cancelTv, o -> {
            finish();
        });
        subscribeClick(mBinding.roomTv, o -> {
            mSearchType = 3;
            ViewBindUtils.setVisible(mBinding.latestRl, false);
            ViewBindUtils.setVisible(mBinding.scrollView, true);
        });
        subscribeClick(mBinding.peopleTv, o -> {
            mSearchType = 2;
            ViewBindUtils.setVisible(mBinding.latestRl, false);
            ViewBindUtils.setVisible(mBinding.scrollView, true);
        });
        subscribeClick(mBinding.peopleMore, o -> {
            startActivity(SearchMoreActivity.newIntent(mContext, "1"));
        });
        subscribeClick(mBinding.roomMore, o -> {
            startActivity(SearchMoreActivity.newIntent(mContext, "2"));
        });
    }

    private void initRecyclerView() {
        mAdapter = new RoomSearchAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {
            String roomId = mAdapter.getItem(position).getRoom_id();
            mRoomJump.startJump(roomId, mContext);
        });
        mBinding.latestRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.latestRv.setAdapter(mAdapter);
    }

    private void initSearchRv() {
        mRoomAdapter = new RoomSearchAdapter(mContext);
        mRoomAdapter.setOnItemClickListener((view, position) -> {
            String roomId = mRoomAdapter.getItem(position).getRoom_id();
            mRoomJump.startJump(roomId, mContext);
        });
        mBinding.roomRv.setNestedScrollingEnabled(false);
        mBinding.roomRv.setHasFixedSize(true);
        mBinding.roomRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.roomRv.setAdapter(mRoomAdapter);

        mMemberAdapter = new MemberSearchAdapter(mContext);
        mMemberAdapter.setOnItemClickListener((view, position) -> {
            String state = mMemberAdapter.getItem(position).getState();
            String roomId = "";
            if (mMemberAdapter.getItem(position).getRoom_id() != null) {
                roomId = mMemberAdapter.getItem(position).getRoom_id();
            }
            if (!TextUtils.isEmpty(state) && (state.equals("4") || state.equals("5"))) {
                if (!TextUtils.isEmpty(roomId)) {
                    mRoomJump.startJump(roomId, mContext);
                }
            } else {
                String uid = mMemberAdapter.getItem(position).getUser_id();
                PageJumpUtils.jumpToProfile(uid);
            }
        });
        mBinding.peopleRv.setNestedScrollingEnabled(false);
        mBinding.peopleRv.setHasFixedSize(true);
        mBinding.peopleRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.peopleRv.setAdapter(mMemberAdapter);
    }


    private void initSearchEdit() {
        mBinding.searchEdit.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                String text = mBinding.searchEdit.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    search(text);
                }
                UiUtils.hideKeyboard(v);
                return true;
            }
            return false;
        });
        mBinding.searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                boolean isEmpty = TextUtils.isEmpty(s.toString());
                ViewBindUtils.setVisible(mBinding.latestRl, isEmpty);
                ViewBindUtils.setVisible(mBinding.scrollView, !isEmpty || mSearchType != 1);
            }
        });
    }

    private void search(String text) {
        mViewModel.roomSearch(text, mSearchType + "")
                .observe(this, new BaseObserver<RoomSearchs>() {
                    @Override
                    public void onSuccess(RoomSearchs result) {
                        if (result != null) {
                            boolean hasMembers = result.getMember_list() != null && !result.getMember_list().isEmpty();
                            ViewBindUtils.setVisible(mBinding.peopleLayout, hasMembers && mSearchType == 1);
                            mMemberAdapter.setNewData(result.getMember_list());
                            boolean hasRooms = result.getRoom_list() != null && !result.getRoom_list().isEmpty();
                            ViewBindUtils.setVisible(mBinding.roomLayout, hasRooms && mSearchType == 1);
                            mRoomAdapter.setNewData(result.getRoom_list());
                            ViewBindUtils.setVisible(mBinding.emptyView, !hasMembers && !hasRooms);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        mRoomJump.destroy();
        super.onDestroy();
    }
}
