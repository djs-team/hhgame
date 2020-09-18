package com.deepsea.mua.stub.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.stub.R;
import com.deepsea.mua.stub.databinding.DialogGuardRenewBinding;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.utils.StringUtils;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.List;

/**
 * 续费
 * Created by JUN on 2019/10/18
 */
public class GuardRenewDialog extends BaseDialog<DialogGuardRenewBinding> {

    public interface OnClickListener {
        public void onClickOk(int payType, String coin, String chargeId,String guard_id,String long_day);
    }

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public GuardRenewDialog(@NonNull Context context) {
        super(context);

    }

    private String payCoin;
    private String guard_id;
    private String long_day;
    private String charge_id;

    public void setData(RenewInitVo vo) {
        String endTime = TimeUtils.addTime(720);
        payCoin = vo.getData().get(0).getCoin();
        guard_id = vo.getData().get(0).getId();
        charge_id = vo.getData().get(0).getCharge_id();
        long_day = String.valueOf(Integer.valueOf(vo.getData().get(0).getLong_time()) / 24);
       List payTypes= StringUtils.extractMessageByRegular(vo.getData().get(0).getPaytype());
        showPay(payTypes);
        ViewBindUtils.setText(mBinding.tvEndtime, "守护到期时间：" + endTime);
        ViewBindUtils.setText(mBinding.tvGuardName, vo.getNickname() + "(" + vo.getId() + ")");
        GuardRenewSetmealAdapter adapter = new GuardRenewSetmealAdapter(mContext);
        mBinding.rvRenewDay.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rvRenewDay.setAdapter(adapter);
        adapter.setOnMyListener(new GuardRenewSetmealAdapter.OnMyClickListener() {
            @Override
            public void onChecked(int pos,int hour, String coin,String guardId,String chargeId,String paytypeStr) {
                payCoin = coin;
                guard_id = guardId;
                charge_id =chargeId;
                long_day = String.valueOf(hour / 24);
               List<String> payTypes= StringUtils.extractMessageByRegular(paytypeStr);
                showPay(payTypes);
                String endTime = TimeUtils.addTime(hour);
                ViewBindUtils.setText(mBinding.tvEndtime, "守护到期时间：" + endTime);

            }
        });
        adapter.setNewData(vo.getData());
        mBinding.ivWxCheck.setSelected(true);
    }

    /**
     * 支持方式  微信支付宝的显示隐藏
     */
    private void showPay(List<String> payTypes){
        if (payTypes != null) {
            if (payTypes.size() == 2) {
                ViewBindUtils.setVisible(mBinding.llPayAlipay, true);
                ViewBindUtils.setVisible(mBinding.llPayWx, true);

            } else if (payTypes.size() == 1) {
                if (payTypes.get(0).equals("1")) {
                    ViewBindUtils.setVisible(mBinding.llPayAlipay, true);
                    ViewBindUtils.setVisible(mBinding.llPayWx, false);
                } else if (payTypes.get(0).equals("2")) {
                    ViewBindUtils.setVisible(mBinding.llPayAlipay, false);
                    ViewBindUtils.setVisible(mBinding.llPayWx, true);
                }
            }
        } else {
            ViewBindUtils.setVisible(mBinding.llPayAlipay, false);
            ViewBindUtils.setVisible(mBinding.llPayWx, false);
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.dialog_guard_renew;
    }

    @Override
    protected float getWidthPercent() {
        return 0.86F;
    }

    boolean isOpenRenew = true;

    @Override
    protected void initListener() {
        mBinding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    int payType = 0;
                    if (mBinding.ivWxCheck.isSelected()) {
                        payType = 0;
                    } else {
                        payType = 1;
                    }
                    onClickListener.onClickOk(payType, payCoin, charge_id,guard_id,long_day);
                }
                dismiss();
            }
        });
        mBinding.llPayWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ivWxCheck.setSelected(true);
                mBinding.ivAlipayCheck.setSelected(false);
            }
        });
        mBinding.llPayAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ivWxCheck.setSelected(false);
                mBinding.ivAlipayCheck.setSelected(true);
            }
        });
        mBinding.llRenewOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenRenew) {
                    isOpenRenew = false;
                    ViewBindUtils.setVisible(mBinding.ivRenewSwitch, false);
                } else {
                    isOpenRenew = true;
                    ViewBindUtils.setVisible(mBinding.ivRenewSwitch, true);
                }
            }
        });

    }


}
