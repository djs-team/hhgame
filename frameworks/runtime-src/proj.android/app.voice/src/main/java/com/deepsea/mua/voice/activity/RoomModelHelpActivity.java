package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.RoomModeHelpBean;
import com.deepsea.mua.stub.entity.RoomModeHelpVo;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.RoomTypeHelpAdapter;
import com.deepsea.mua.voice.databinding.ActivityRoomModeHelpBinding;
import com.deepsea.mua.voice.viewmodel.RoomCreateViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/6/28
 * 房间类型说明
 */
public class RoomModelHelpActivity extends BaseActivity<ActivityRoomModeHelpBinding> {

    @Inject
    ViewModelFactory mViewModelFactory;
    private RoomCreateViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_mode_help;
    }

    private RoomTypeHelpAdapter mAdapter;

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(RoomCreateViewModel.class);
        mAdapter = new RoomTypeHelpAdapter(mContext);
        mBinding.rvModeHelp.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvModeHelp.setAdapter(mAdapter);
        fetchData();

    }

    private void fetchData() {
        mViewModel.getRoomModeHelp().observe(this, new BaseObserver<RoomModeHelpBean>() {
            @Override
            public void onSuccess(RoomModeHelpBean result) {
                if (result.getList() != null && result.getList().size() > 0) {
                    mAdapter.setNewData(result.getList());
                }
            }
        });
    }


}
