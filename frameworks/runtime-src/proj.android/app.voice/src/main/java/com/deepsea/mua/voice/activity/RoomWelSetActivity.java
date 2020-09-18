package com.deepsea.mua.voice.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ActivityRoomWelSetBinding;
import com.deepsea.mua.voice.viewmodel.RoomSetViewModel;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/1
 * 房间设置/玩法介绍、欢迎语
 */
public class RoomWelSetActivity extends BaseActivity<ActivityRoomWelSetBinding> {

    public static final String TYPE_PLAY = "type_play";
    public static final String TYPE_WEL = "type_wel";

    @Inject
    ViewModelFactory viewModelFactory;
    private RoomSetViewModel mViewModel;

    private String type;
    private String hint;

    public static Intent newIntent(Context context, String type, String hint) {
        Intent intent = new Intent(context, RoomWelSetActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("hint", hint);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_room_wel_set;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        type = intent.getStringExtra("type");
        hint = intent.getStringExtra("hint");
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        type = savedInstanceState.getString("type");
        hint = savedInstanceState.getString("hint");
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoomSetViewModel.class);
        //玩法介绍
        if (TYPE_PLAY.equals(type)) {
            mBinding.titleBar.setTitle("玩法介绍");
            mBinding.roomSetEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1000)});
        }
        //欢迎语
        else {
            mBinding.titleBar.setTitle("欢迎语");
            mBinding.roomSetEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(120)});
        }
        mBinding.roomSetEdit.setText(hint);
        mBinding.roomSetEdit.setSelection(mBinding.roomSetEdit.getText().length());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", type);
        outState.putString("hint", hint);
    }

    @Override
    public void finish() {
        roomUpdate();
        super.finish();
    }

    private void roomUpdate() {
        String value = mBinding.roomSetEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(value) && !TextUtils.equals(value, hint)) {
            if (TYPE_PLAY.equals(type)) {
                mViewModel.setRoomPlay(value);
            } else {
                mViewModel.setRoomWel(value);
            }
        }
    }
}
