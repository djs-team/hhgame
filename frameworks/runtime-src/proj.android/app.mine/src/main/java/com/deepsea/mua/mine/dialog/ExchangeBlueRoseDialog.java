package com.deepsea.mua.mine.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.DialogExchangeBlueRoseBinding;
import com.deepsea.mua.stub.entity.BlueRoseExchange;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;


/**
 * Created by JUN on 2018/9/27
 */
public class ExchangeBlueRoseDialog extends BaseDialog<DialogExchangeBlueRoseBinding> {

    public ExchangeBlueRoseDialog(@NonNull Context context) {
        super(context);
    }

    public interface OnClickListener {
        /**
         * 点击回调
         *
         * 数量
         * 礼物id
         */
        void onClick(String num, String gift_id);
    }
    private  OnClickListener mListener;

    public void setmListener(OnClickListener mListener) {
        this.mListener = mListener;
    }

    private int mInterval = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_exchange_blue_rose;
    }

    @Override
    protected float getWidthPercent() {
        return 0.8F;
    }

    @Override
    protected float getDimAmount() {
        return 0.2F;
    }

    public void setMsg(BlueRoseExchange vo) {
        if (!TextUtils.isEmpty(vo.getGift_image())) {
            GlideUtils.loadImage(mBinding.ivLogo, vo.getGift_image());
        }
        ViewBindUtils.setText(mBinding.tvBlueRoseDesc, "蓝:" + vo.getGift_blue_coin() + "\n" + "数量:" + vo.getPack_num());
        ViewBindUtils.RxClicks(mBinding.ivClose, o -> {
            dismiss();
        });
        ViewBindUtils.RxClicks(mBinding.increaseIv, o -> {
            int count = 0;
            String number = mBinding.numberEdit.getText().toString();
            if (FormatUtils.isNumber(number)) {
                count = Integer.parseInt(number);
            }
            count += mInterval;
            setHammerNumber(count);
        });
        ViewBindUtils.RxClicks(mBinding.reduceIv, o -> {
            int count = 0;
            String number = mBinding.numberEdit.getText().toString();
            if (FormatUtils.isNumber(number)) {
                count = Integer.parseInt(number);
            }
            if (count >= mInterval) {
                count -= mInterval;
                setHammerNumber(count);
            }
        });
        ViewBindUtils.RxClicks(mBinding.exchangeIv,o->{
            String count = mBinding.numberEdit.getText().toString();
            if (TextUtils.isEmpty(count) || FormatUtils.equalsZero(count)) {
                ToastUtils.showToast("请输入购买数量");
                return;
            }
            if (mListener!=null){
                mListener.onClick(count,vo.getGift_id());
            }
        });
    }

    private void setHammerNumber(int count) {
        mBinding.numberEdit.setText(String.valueOf(count));
        mBinding.numberEdit.setSelection(mBinding.numberEdit.getText().length());
    }
}
