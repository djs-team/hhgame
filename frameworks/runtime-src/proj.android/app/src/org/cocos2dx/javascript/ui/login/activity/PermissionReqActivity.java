package org.cocos2dx.javascript.ui.login.activity;

import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hh.game.R;
import com.hh.game.databinding.ActivityPermissionBinding;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class PermissionReqActivity extends BaseActivity<ActivityPermissionBinding> {
    @Inject
    ViewModelFactory mViewModelFactory;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_permission;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.rlPermissionNext, o -> {
            toastShort("下一步");
        });
        subscribeClick(mBinding.ivPermissionCameraSwich, o -> {
            if (!mBinding.ivPermissionCameraSwich.isSelected()){
                mBinding.ivPermissionCameraSwich.setSelected(true);
                toastShort("开");
            }else {
                mBinding.ivPermissionCameraSwich.setSelected(false);
                toastShort("关");
            }
        });
        subscribeClick(mBinding.ivPermissionNotifySwich, o -> {
            if (!mBinding.ivPermissionNotifySwich.isSelected()){
                mBinding.ivPermissionNotifySwich.setSelected(true);
                toastShort("开");
            }else {
                mBinding.ivPermissionNotifySwich.setSelected(false);
                toastShort("关");
            }
        });
        subscribeClick(mBinding.ivPermissionLocationSwich, o -> {
            if (!mBinding.ivPermissionLocationSwich.isSelected()){
                mBinding.ivPermissionLocationSwich.setSelected(true);
                toastShort("开");
            }else {
                mBinding.ivPermissionLocationSwich.setSelected(false);
                toastShort("关");
            }
        });
    }

}
