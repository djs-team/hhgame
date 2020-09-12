package com.deepsea.mua.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityIdentityAuthBinding;
import com.deepsea.mua.mine.dialog.UnauthErrorDialog;
import com.deepsea.mua.mine.mvp.PersonalContracts;
import com.deepsea.mua.mine.mvp.impl.PersonalPresenter;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.UserUtils;

/**
 * 作者：liyaxing  2019/9/2 17:29
 * 类别 ：身份认证
 */
@Route(path = ArouterConst.PAGE_AUTH)
public class PresentIdentityAuthActivity extends BaseActivity<ActivityIdentityAuthBinding> implements PersonalContracts.PresentIdentityAuthView {

    @Autowired(name = "type")
    int aType;//0 未提交 1认证 2未通过

    public static Intent newIntent(Context context, int type) {
        Intent intent = new Intent(context, PresentIdentityAuthActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity_auth;
    }

    @Override
    protected void initView() {
        aType = getIntent().getIntExtra("type", 0);
        mPresenter = new PersonalPresenter(this, 0);

        //mBinding.authBgRl   type 1 身份认证蓝色背景   1 已认证   白色背景
        //mBinding.identityAuth 未认证时显示
        //   mBinding.hasCertification     已认证
        if (aType == 1) {
            mBinding.titleNickTv.setText("实名认证");
            mBinding.titleNickTv.setTextColor(getResources().getColor(R.color.F313131));
            mBinding.authBgRl.setBackgroundResource(R.color.white);
            mBinding.identityAuth.setVisibility(View.GONE);
            mBinding.hasCertification.setVisibility(View.VISIBLE);
            mBinding.backIv.setBackgroundResource(R.drawable.ic_back_black);

        } else {
            mBinding.titleNickTv.setText("身份认证");
            mBinding.backIv.setBackgroundResource(R.drawable.ic_back);
            mBinding.titleNickTv.setTextColor(getResources().getColor(R.color.FFFFFF));
            mBinding.authBgRl.setBackgroundResource(R.drawable.auth_bg);
            mBinding.identityAuth.setVisibility(View.VISIBLE);
            mBinding.hasCertification.setVisibility(View.GONE);

        }


        Spanned comment = Html.fromHtml("<font color='#FF0202'>" + "* " + "</font>" + "提交成功后无法修改，请谨慎填写。");
        mBinding.instructions.setText(comment);
        subscribeClick(mBinding.submit, o -> {
            if (!TextUtils.isEmpty(mBinding.nickTv.getText().toString().trim()) && !TextUtils.isEmpty(mBinding.identityNumber.getText().toString().trim())) {
                String regex = "\\d{15}(\\d{2}[0-9xX])?";
                if (mBinding.identityNumber.getText().toString().trim().matches(regex)) {
                    showProgress();
                    mPresenter.realuser(mBinding.nickTv.getText().toString().trim(), mBinding.identityNumber.getText().toString().trim());
                } else {
                    UnauthErrorDialog aUnauthErrorDialog = new UnauthErrorDialog(mContext);
                    aUnauthErrorDialog.setCreatListener(() -> {
                        mBinding.identityNumber.setText("");
                    });
                    aUnauthErrorDialog.show();
                }
            } else {
                ToastUtils.showToast("请填写完整信息");
            }

        });

        subscribeClick(mBinding.backIvLl, o -> {
            finish();
        });


    }


    @Override
    public void showErrorMsg(int aType, String msg) {
        hideProgress();
        ToastUtils.showToast(msg);
        mBinding.identityNumber.setText("");
        mBinding.nickTv.setText("");


        UnauthErrorDialog aUnauthErrorDialog = new UnauthErrorDialog(mContext);
        aUnauthErrorDialog.setCreatListener(() -> {
            mBinding.identityNumber.setText("");
        });
        aUnauthErrorDialog.show();

    }

    @Override
    public void realuserOk(String response) {
        hideProgress();
        mBinding.titleNickTv.setText("实名认证");
        mBinding.titleNickTv.setTextColor(getResources().getColor(R.color.F313131));
        mBinding.authBgRl.setBackgroundResource(R.color.white);
        mBinding.identityAuth.setVisibility(View.GONE);
        mBinding.hasCertification.setVisibility(View.VISIBLE);
        mBinding.backIv.setBackgroundResource(R.drawable.ic_back_black);

        User user = UserUtils.getUser();
        user.setAttestation("1");
        UserUtils.saveUser(user);

    }

    PersonalContracts.Presenter mPresenter;

    @Override
    public void setPresenter(PersonalContracts.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onFailure() {
        hideProgress();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }
}
