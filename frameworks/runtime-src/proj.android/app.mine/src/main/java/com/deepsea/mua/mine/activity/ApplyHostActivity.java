package com.deepsea.mua.mine.activity;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityApplyHostBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.ApplyHostVo;
import com.deepsea.mua.stub.utils.ArouterConst;

import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;


/**
 * Created by JUN on 2019/5/6
 * 申请主持
 */
@Route(path = ArouterConst.PAGE_ME_MINE_APPLYHOST)

public class ApplyHostActivity extends BaseActivity<ActivityApplyHostBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_apply_host;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);

        requestData();

    }

    private String state = "";//任务状态

    @Override
    protected void initListener() {
        subscribeClick(mBinding.rlClose, o -> {
            finish();
        });
        subscribeClick(mBinding.tvApply, o -> {
            if (state.equals("10")) {
                AAlertDialog mDialog = new AAlertDialog(mContext);
                mDialog.setMessage("是否申请成为红娘？");
                mDialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        mDialog.dismiss();
                    }
                });
                mDialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        mDialog.dismiss();
                        fetchApplyHost();
                    }
                });
                mDialog.show();
            }
        });

    }


    private void requestData() {
        mViewModel.init_apply().observe(this, new BaseObserver<ApplyHostVo>() {
            @Override
            public void onSuccess(ApplyHostVo result) {
                initData(result);
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private void fetchApplyHost() {
        mViewModel.fetchApplyHost().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                ToastUtils.showToast(result.getDesc());
                requestData();
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private void initData(ApplyHostVo data) {

        if (data.getResult() != null) {
            ViewBindUtils.setText(mBinding.tvContentOne, data.getResult().getOne());
            ViewBindUtils.setText(mBinding.tvContentTwo, data.getResult().getTwo());
            ViewBindUtils.setText(mBinding.tvContentThree, data.getResult().getThree());
        }
        if (!TextUtils.isEmpty(data.getStatus())) {
            state = data.getStatus();
            if (state.equals("10")) {
                ViewBindUtils.setText(mBinding.tvApply, "申请成为红娘");
            } else if (state.equals("0")) {
                ViewBindUtils.setText(mBinding.tvApply, "申请中");
            } else if (state.equals("1")) {
                ViewBindUtils.setText(mBinding.tvApply, "已通过");

            }

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
