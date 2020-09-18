package com.deepsea.mua.app.im.mua;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ActivityMessageBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.ArouterConst;

/**
 * Created by JUN on 2019/7/11
 */
@Route(path = ArouterConst.PAGE_MESSAGE)
public class MessageActivity extends BaseActivity<ActivityMessageBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @Override
    protected void initView() {
    }
}
