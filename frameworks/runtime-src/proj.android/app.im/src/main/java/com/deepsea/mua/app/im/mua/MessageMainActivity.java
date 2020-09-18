package com.deepsea.mua.app.im.mua;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ActivityMessageBinding;
import com.deepsea.mua.app.im.databinding.ActivityMessageMainBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ArouterConst;

/**
 * Created by JUN on 2019/7/11
 */
@Route(path = ArouterConst.PAGE_MSG_MESSAGEMAIN)
public class MessageMainActivity extends BaseActivity<ActivityMessageMainBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message_main;
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.rlFinish, o -> {
            finish();
        });
    }
}
