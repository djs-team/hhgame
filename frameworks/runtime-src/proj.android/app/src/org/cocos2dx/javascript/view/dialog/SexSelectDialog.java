package org.cocos2dx.javascript.view.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.hh.game.R;
import com.hh.game.databinding.DialogSexSelectBinding;

/**
 * Created by JUN on 2019/4/4
 */
public class SexSelectDialog extends BaseDialog<DialogSexSelectBinding> {

    public interface OnSexSelectListener {
        void onSexSelect(String sex);
    }

    private OnSexSelectListener mListener;

    public SexSelectDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
        mBinding.setDlg(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_sex_select;
    }

    @Override
    protected float getWidthPercent() {
        return 0.895F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public void setOnSexSelectListener(OnSexSelectListener listener) {
        this.mListener = listener;
    }

    public void onClick(View view) {
        //上一步
        if (view == mBinding.previousBtn) {
            dismiss();
        }
        //xgg
        else if (view == mBinding.xggIv) {
            mBinding.xggIv.setSelected(true);
            mBinding.xjjIv.setSelected(false);
            mBinding.nextBtn.setEnabled(true);
            ViewBindUtils.setTextColor(mBinding.nextBtn, R.color.black);
        }
        //xjj
        else if (view == mBinding.xjjIv) {
            mBinding.xjjIv.setSelected(true);
            mBinding.xggIv.setSelected(false);
            mBinding.nextBtn.setEnabled(true);
            ViewBindUtils.setTextColor(mBinding.nextBtn, R.color.black);
        }
        //下一步
        else if (view == mBinding.nextBtn) {
            String sex = mBinding.xggIv.isSelected() ? "1" : "2";
            if (mListener != null) {
                mListener.onSexSelect(sex);
            }
            dismiss();
        }
    }
}
