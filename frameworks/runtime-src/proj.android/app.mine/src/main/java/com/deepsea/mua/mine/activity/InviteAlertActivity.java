package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.text.TextUtils;
import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityInviteAlertBinding;
import com.deepsea.mua.mine.viewmodel.InviteAlertModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.InstallInviteCode;
import com.deepsea.mua.stub.entity.InviteAlertMemberBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/8/9
 * 邀请提示
 */
@Route(path = ArouterConst.PAGE_ME_INVITEALERT)

public class InviteAlertActivity extends BaseActivity<ActivityInviteAlertBinding> {
    private InviteAlertModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;
    @Autowired
    String code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invite_alert;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(InviteAlertModel.class);
        fetchInfo();
    }

    private String belongId = "";

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.closeBtn, o -> {
            finish();
        });
        subscribeClick(mBinding.ivBelongHeader, o -> {
            if (!TextUtils.isEmpty(belongId))
                PageJumpUtils.jumpToProfile(belongId);
        });
    }


    private void fetchInfo() {
        try {
            InstallInviteCode inviteCode = JsonConverter.fromJson(code, InstallInviteCode.class);
            if (inviteCode != null) {
                code = inviteCode.getReferrercode();
            }
        }catch (Exception e){

        }

        mViewModel.fetchInfo(code).observe(this, new BaseObserver<InviteAlertMemberBean>() {
            @Override
            public void onSuccess(InviteAlertMemberBean result) {
                if (result != null) {
                    GlideUtils.circleImage(mBinding.ivBelongHeader, result.getAvatar(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                    ViewBindUtils.setText(mBinding.tvBlongName, result.getNickname());
                    SexResUtils.setSexImg(mBinding.ivBelongSex, result.getSex());
                    StringBuilder info = new StringBuilder();
                    info.append(result.getAge());
                    info.append("岁");
                    if (!TextUtils.isEmpty(result.getStature())) {
                        info.append(" |");
                        info.append(" " + result.getStature() + "cm");
                    }
                    if (!TextUtils.isEmpty(result.getAddress())) {
                        info.append(" |");
                        info.append(" " + result.getAddress());
                    }
                    if (!TextUtils.isEmpty(result.getSend_coin())) {
                        ViewBindUtils.setText(mBinding.tvBlongInfo, String.format("您接受%s邀请，加入合合交友，\n可获得%s朵玫瑰", result.getNickname(), result.getSend_coin()));
                    }
                    belongId = result.getId();
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
