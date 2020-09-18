package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.ViewModelProviders;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.app.im.HxSettingModel;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ActivityMsgSettingBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
@Route(path = ArouterConst.PAGE_MSG_SETTING)
public class MsgSettingActivity extends BaseActivity<ActivityMsgSettingBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private MsgSettingViewModel mViewModel;
    private String mFansmenustatus;

    private HxSettingModel mSettingModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_msg_setting;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(MsgSettingViewModel.class);
        mSettingModel = HxHelper.getInstance().getModel();
        mFansmenustatus = UserUtils.getUser().getFansmenustatus();
        mBinding.anchorNoticeIv.setSelected("0".equals(mFansmenustatus));
        mBinding.msgNoticeIv.setSelected(mSettingModel.getSettingMsgNotification());
        mBinding.shakeIv.setSelected(mSettingModel.getSettingMsgVibrate());
    }

    @Override
    protected void initListener() {
        mBinding.msgNoticeIv.setOnClickListener(v -> {
            mSettingModel.setSettingMsgNotification(!mSettingModel.getSettingMsgNotification());
            mBinding.msgNoticeIv.setSelected(mSettingModel.getSettingMsgNotification());
        });
        mBinding.shakeIv.setOnClickListener(v -> {
            mSettingModel.setSettingMsgVibrate(!mSettingModel.getSettingMsgVibrate());
            mBinding.shakeIv.setSelected(mSettingModel.getSettingMsgVibrate());
        });
        mBinding.anchorNoticeIv.setOnClickListener(v -> fansmenusatatus());
    }

    private void fansmenusatatus() {
        String type = "0".equals(mFansmenustatus) ? "1" : "0";
        mViewModel.fansmenusatatus(type).observe(this, new ProgressObserver<BaseApiResult>(mContext) {
            @Override
            public void onSuccess(BaseApiResult result) {
                mFansmenustatus = type;
                mBinding.anchorNoticeIv.setSelected("0".equals(type));

                User user = UserUtils.getUser();
                user.setFansmenustatus(mFansmenustatus);
                UserUtils.saveUser(user);
            }
        });
    }
}
