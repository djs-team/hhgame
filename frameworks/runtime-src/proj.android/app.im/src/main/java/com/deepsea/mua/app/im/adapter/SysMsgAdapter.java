package com.deepsea.mua.app.im.adapter;

import android.content.Context;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ItemSysMsgBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.SystemMsgBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

/**
 * Created by JUN on 2019/5/5
 */
public class SysMsgAdapter extends BaseBindingAdapter<SystemMsgBean, ItemSysMsgBinding> {

    public SysMsgAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_sys_msg;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSysMsgBinding> holder, SystemMsgBean item) {
        ViewBindUtils.setText(holder.binding.tvContent, item.getContent());
        ViewBindUtils.setText(holder.binding.tvTime, item.getTime());
//        String strMsg = "今天<font color=\"#00ff00\">天气不错</font>";
//        tv_msg.setText(Html.fromHtml(strMsg));
    }
}
