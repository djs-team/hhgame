package com.deepsea.mua.mine.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityFeedResultBinding;
import com.deepsea.mua.stub.base.BaseActivity;

/**
 * Created by JUN on 2019/5/7
 */
public class FeedResultActivity extends BaseActivity<ActivityFeedResultBinding> {

    private String title;
    private String content;

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_result;
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(title)) {
            mBinding.titleBar.setTitle(title);
        }
        if (!TextUtils.isEmpty(content)) {
            mBinding.contentTv.setText(content);
        }
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.closeTv, o -> {
            finish();
        });
    }
}
