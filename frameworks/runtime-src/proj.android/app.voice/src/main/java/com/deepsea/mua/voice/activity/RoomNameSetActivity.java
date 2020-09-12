package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ActivityRoomNameSetBinding;
import com.deepsea.mua.voice.viewmodel.RoomSetViewModel;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/1
 * 房间设置/房间名称
 */
public class RoomNameSetActivity extends BaseActivity<ActivityRoomNameSetBinding> {

    @Inject
    ViewModelProvider.Factory mFactory;
    private RoomSetViewModel mViewModel;

    private String name;

    public static Intent newIntent(Context context, String name) {
        Intent intent = new Intent(context, RoomNameSetActivity.class);
        intent.putExtra("NAME", name);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        name = intent.getStringExtra("NAME");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("NAME", name);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_name_set;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mFactory).get(RoomSetViewModel.class);
        mBinding.roomNameEdit.setText(name);
        mBinding.roomNameEdit.setSelection(mBinding.roomNameEdit.getText().length());
    }

    @Override
    public void finish() {
        String input = mBinding.roomNameEdit.getText().toString();
        if (!TextUtils.isEmpty(input) && !TextUtils.equals(name, input)) {
            mViewModel.setRoomName(input);
        }
        super.finish();
    }
}
