package com.deepsea.mua.voice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.RoomTags;
import com.deepsea.mua.stub.entity.VoiceRoomBean;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.SignatureUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomModeAdapter;
import com.deepsea.mua.voice.adapter.RoomTagsAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomCreateBinding;
import com.deepsea.mua.voice.utils.ViewAnimUtils;
import com.deepsea.mua.voice.viewmodel.RoomCreateViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/2
 */
public class RoomCreateActivity extends BaseActivity<ActivityRoomCreateBinding> {

    @Inject
    ViewModelFactory mViewModelFactory;
    private RoomCreateViewModel mViewModel;

    private RoomModeAdapter mModeAdapter;
    private RoomTagsAdapter mTagsAdapter;

    private boolean isExpand = true;
    private int mModeRvHeight;
    private boolean isAnimationRunning;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_create;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RoomCreateViewModel.class);
        initTagsRv();
        initModeRv();
        getTagList();
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.modeLayout, o -> {
            expandModeRv();
        });
        subscribeClick(mBinding.createTv, o -> {
            String name = mBinding.nameEdit.getText().toString();
            if (TextUtils.isEmpty(name)) {
                toastShort("请输入房间名称");
                return;
            }
            String mTagId = mTagsAdapter.getSelectTag();
            if (TextUtils.isEmpty(mTagId)) {
                toastShort("请选择房间标签");
                return;
            }
            String mModeId = mModeAdapter.getSelectModeId();
            if (TextUtils.isEmpty(mModeId)) {
                toastShort("请选择房间模式");
                return;
            }
            showProgress();
            Map<String, String> map = new HashMap<>();
            map.put("room_name", mBinding.nameEdit.getText().toString().trim());
            map.put("room_type", mModeId);
            map.put("room_tags", mTagsAdapter.getSelectTag());
            map.put("signature", SignatureUtils.signWith(mModeId));
            mViewModel.create().observe(this,
                    new BaseObserver<VoiceRoomBean.RoomInfoBean>() {
                        @Override
                        public void onSuccess(VoiceRoomBean.RoomInfoBean result) {
                            hideProgress();
                            finish();
                        }

                        @Override
                        public void onError(String msg, int code) {
                            hideProgress();

                                toastShort(msg);

                        }
                    });
        });
    }

    private void initTagsRv() {
        mTagsAdapter = new RoomTagsAdapter(mContext);
        mTagsAdapter.setOnItemClickListener((view, position) -> {
            RoomTags.TagListBean item = mTagsAdapter.getItem(position);
            mTagsAdapter.setSelectPos(position);
        });
        mBinding.labelRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        mBinding.labelRv.addItemDecoration(new GridItemDecoration(4, ResUtils.dp2px(mContext, 8)));
        mBinding.labelRv.setNestedScrollingEnabled(false);
        mBinding.labelRv.setHasFixedSize(true);
        mBinding.labelRv.setAdapter(mTagsAdapter);
    }

    private void initModeRv() {
        mModeAdapter = new RoomModeAdapter(mContext);
        mModeAdapter.setOnItemClickListener((view, position) -> {
            RoomTags.ModeListBean item = mModeAdapter.getItem(position);
            mBinding.modeTv.setText(item.getRoom_mode());
            mModeAdapter.setSelectPos(position);

            mTagsAdapter.setNewData(item.getMode_tags());
        });
        mBinding.modeRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.modeRv.setNestedScrollingEnabled(false);
        mBinding.modeRv.setHasFixedSize(true);
        mBinding.modeRv.setAdapter(mModeAdapter);
    }

    private void getTagList() {
        mViewModel.getTagsList().observe(this, new BaseObserver<RoomTags>() {
            @Override
            public void onSuccess(RoomTags result) {
                if (result != null) {
                    List<RoomTags.ModeListBean> list = result.getMode_list();
//                    Iterator<RoomTags.ModeListBean> it = list.iterator();
//                    while (it.hasNext()) {
//                        RoomTags.ModeListBean next = it.next();
//                        if (!TextUtils.equals("1", next.getRoom_type())) {
//                            it.remove();
//                        }
//                    }
                    mModeAdapter.setNewData(list);

                    if (!CollectionUtils.isEmpty(list)) {
                        mTagsAdapter.setNewData(list.get(0).getMode_tags());
                    }
                }
            }
        });
    }

    private void expandModeRv() {
        if (mModeRvHeight == 0) {
            mBinding.modeRv.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mModeRvHeight = mBinding.modeRv.getMeasuredHeight();
        }
        if (!isAnimationRunning) {
            int start = isExpand ? mModeRvHeight : 0;
            int end = isExpand ? 0 : mModeRvHeight;
            ViewAnimUtils.ofInt(mBinding.modeRv, ViewAnimUtils.Property.HEIGHT, 300, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    isAnimationRunning = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isExpand = !isExpand;
                    isAnimationRunning = false;
                    mBinding.modeArrowIv.setImageResource(isExpand ? R.drawable.ic_arrow_b_gray : R.drawable.ic_arrow_r_gray);
                }
            }, start, end);
        }
    }
}
