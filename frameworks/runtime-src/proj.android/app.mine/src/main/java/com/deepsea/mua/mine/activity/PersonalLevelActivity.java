package com.deepsea.mua.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityPersonalLevelBinding;
import com.deepsea.mua.mine.mvp.PersonalContracts;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.entity.ProfileBean;
import com.deepsea.mua.stub.utils.span.LevelResUtils;

/**
 * 作者：liyaxing  2019/9/2 15:02
 * 类别 ： 等级
 */
public class PersonalLevelActivity extends BaseActivity<ActivityPersonalLevelBinding> {

    private ProfileBean mProfileBean;
    private String mProfileJson;

    public static Intent newIntent(Context context, String profileJson) {
        Intent intent = new Intent(context, PersonalLevelActivity.class);
        intent.putExtra("profileJson", profileJson);
        return intent;
    }

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        mProfileJson = intent.getStringExtra("profileJson");
        if (!TextUtils.isEmpty(mProfileJson)) {
            mProfileBean = JsonConverter.fromJson(mProfileJson, ProfileBean.class);
        }
    }


    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        mProfileJson = savedInstanceState.getString("profileJson");
        if (!TextUtils.isEmpty(mProfileJson)) {
            mProfileBean = JsonConverter.fromJson(mProfileJson, ProfileBean.class);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mProfileBean != null) {
            outState.putString("profileJson", JsonConverter.toJson(mProfileBean));
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_level;
    }


    PersonalContracts.Presenter mPresenter;

    @Override
    protected void initView() {
        mBinding.sbLevel.setEnabled(false);
        mBinding.sbLevel.setProgress(50);


        GlideUtils.circleImage(mBinding.headIv, mProfileBean.getUser_info().getAvatar(), R.drawable.ic_place_default, R.drawable.ic_place_default);
        //nick
        mBinding.nickTv.setText(mProfileBean.getUser_info().getNickname());


        mBinding.curtValueTv.setText(mProfileBean.getGrade_info().getLv_dengji() + "");
        mBinding.curtValueTv.setBackgroundResource(LevelResUtils.getLevelRes(mProfileBean.getGrade_info().getLv_dengji()));


        //当前等级
        mBinding.startValueTv.setText("Lv." + mProfileBean.getGrade_info().getLv_dengji());
        //下一等级
        mBinding.endValueTv.setText("Lv." + mProfileBean.getGrade_info().getLh_dengji());
        //当前的经验/下一等级的经验
        mBinding.experienceValueTv.setText(mProfileBean.getGrade_info().getLevel_number() + "/" + mProfileBean.getGrade_info().getHight_number());
        //百分比进度
        mBinding.sbLevel.setProgress(mProfileBean.getGrade_info().getPercentage());


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
