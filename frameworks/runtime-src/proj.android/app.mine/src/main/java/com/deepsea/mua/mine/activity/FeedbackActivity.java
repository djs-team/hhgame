package com.deepsea.mua.mine.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.text.TextUtils;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityFeedbackBinding;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/7
 */
public class FeedbackActivity extends BaseActivity<ActivityFeedbackBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private ProfileViewModel mViewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(ProfileViewModel.class);
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.commitTv, o -> {
            String s = mBinding.feedEdit.getText().toString();
            if (TextUtils.isEmpty(s)) {
                toastShort("请输入反馈内容");
                return;
            }

            mViewModel.setFeedback(s).observe(this, new ProgressObserver<BaseApiResult>(mContext) {
                @Override
                public void onSuccess(BaseApiResult result) {
                    Intent intent = new Intent(mContext, FeedResultActivity.class);
                    intent.putExtra("title", "意见反馈");
                    intent.putExtra("content", "我们的工作人员会及时处理，多谢您的宝贵意见！");
                    startActivity(intent);
                    finish();
                }
            });
        });
    }
}
