package com.deepsea.mua.core.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.deepsea.mua.core.R;
import com.deepsea.mua.core.databinding.DialogLoadingBinding;

/**
 * Created by JUN on 2018/9/26
 */
public class LoadingDialog extends BaseDialog<DialogLoadingBinding> {

    public int tag = -10000;

    public LoadingDialog(@NonNull Context context) {
        this(context, null);
    }

    public LoadingDialog(@NonNull Context context, String message) {
        super(context);
        TextView loadingTv = findViewById(R.id.loading_tv);
        loadingTv.setText(message);
        loadingTv.setVisibility(TextUtils.isEmpty(message) ? View.GONE : View.VISIBLE);
    }

    @Override
    protected float getDimAmount() {
        return 0F;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_loading;
    }
}
