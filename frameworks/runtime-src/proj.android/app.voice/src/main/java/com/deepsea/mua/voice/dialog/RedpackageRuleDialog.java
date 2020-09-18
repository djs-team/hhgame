package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.DialogRedpakageRuleBinding;

/**
 * Created by JUN on 2018/9/27
 * 粉丝榜
 */
public class RedpackageRuleDialog extends BaseDialog<DialogRedpakageRuleBinding> {

    public RedpackageRuleDialog(@NonNull Context context) {
        super(context);

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_redpakage_rule;
    }

    @Override
    protected float getWidthPercent() {
        return 0.85F;
    }


    public void setMsg(String content) {
        ViewBindUtils.setHtml(mBinding.tvRule, content);

    }


}
