package com.deepsea.mua.mine.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.adapter.GuardRenewDaysAdapter;
import com.deepsea.mua.mine.databinding.DialogGuardRenewOpenBinding;

/**
 * Created by JUN on 2019/10/18
 */
public class GuardRenewOpenDialog extends BaseDialog<DialogGuardRenewOpenBinding> {

    public interface OnClickListener {
        public void onClickOk();
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public GuardRenewOpenDialog(@NonNull Context context) {
        super(context);

    }

    public void setData() {
        GuardRenewDaysAdapter adapter = new GuardRenewDaysAdapter(mContext);
        mBinding.rvRenewDay.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rvRenewDay.setAdapter(adapter);
        adapter.setOnMyListener(new GuardRenewDaysAdapter.OnMyClickListener() {
            @Override
            public void onCheckedDay() {

            }
        });
//        adapter.setNewData();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_renew_open;
    }

    @Override
    protected float getWidthPercent() {
        return 0.75F;
    }

    @Override
    protected void initListener() {
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClickOk();
                }
                dismiss();
            }
        });
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
