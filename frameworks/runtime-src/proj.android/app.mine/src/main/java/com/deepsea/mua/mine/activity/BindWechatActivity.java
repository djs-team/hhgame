package com.deepsea.mua.mine.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ActivityBindWxBinding;
import com.deepsea.mua.mine.dialog.WxBindResultDialog;
import com.deepsea.mua.mine.dialog.WxUnbindConfrimDialog;
import com.deepsea.mua.mine.dialog.WxUnbindResultDialog;
import com.deepsea.mua.mine.viewmodel.ProfileViewModel;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.BindWx;
import com.deepsea.mua.stub.entity.CheckBindWx;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.umeng.socialize.bean.SHARE_MEDIA;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/4/15
 */
public class BindWechatActivity extends BaseActivity<ActivityBindWxBinding> {
    @Inject
    ViewModelFactory mViewModelFactory;
    private ProfileViewModel mViewModel;
    private int isBindWx;
    private String wx_id = "";

    private int bind_type;

    public static Intent newIntent(Context context, int bind_type) {
        Intent intent = new Intent(context, BindWechatActivity.class);
        intent.putExtra("bind_type", bind_type);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_wx;
    }


    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ProfileViewModel.class);
        bind_type = getIntent().getIntExtra("bind_type", 1);
        initBindView();
    }

    private void initBindView() {
        User user = UserUtils.getUser();
        if (bind_type != 2) {
            String phoneNumber = user.getUsername();
            String secrecyPhoneNumber = phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            ViewBindUtils.setText(mBinding.tvPhone, secrecyPhoneNumber);
            ViewBindUtils.setVisible(mBinding.rlBindWx, true);
            ViewBindUtils.setVisible(mBinding.tvBindwxDesc, true);
            ViewBindUtils.setVisible(mBinding.tvLoginType, true);
            ViewBindUtils.setVisible(mBinding.tvPhone, true);
            ViewBindUtils.setVisible(mBinding.tvBindphone, false);
            ViewBindUtils.setVisible(mBinding.ivBindwx, true);
            ViewBindUtils.setVisible(mBinding.ivBindwxLine, true);

            isBindWx();
        } else {
            ViewBindUtils.setVisible(mBinding.rlBindWx, false);
            ViewBindUtils.setVisible(mBinding.tvBindwxDesc, false);
            ViewBindUtils.setVisible(mBinding.tvLoginType, false);
            ViewBindUtils.setVisible(mBinding.tvPhone, false);
            ViewBindUtils.setVisible(mBinding.ivBindwx, false);
            ViewBindUtils.setVisible(mBinding.ivBindwxLine, false);

            String phone = UserUtils.getUser().getUsername();
            ViewBindUtils.setVisible(mBinding.tvBindphone, TextUtils.isEmpty(phone));
        }
    }


    private void isBindWx() {
        mViewModel.isBindWx().observe(this, new ProgressObserver<CheckBindWx>(mContext) {
                    @Override
                    public void onSuccess(CheckBindWx result) {
                        if (result != null) {
                            updateData(result.getIs_band(), result.getWx_id());
                        }
                    }

                    @Override
                    public void onError(String msg, int code) {
                        super.onError(msg, code);
                    }
                }
        );
    }

    private void updateData(int isBind, String wx_id) {
        this.isBindWx = isBind;
        this.wx_id = wx_id;
        ViewBindUtils.setVisible(mBinding.ivBindwx, isBindWx != 1);

    }

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.ivBindwx, o -> {
            if (isBindWx != 1) {
                //解绑
                bindWx();
            }
        });
        subscribeClick(mBinding.tvBindphone, o -> {
            Intent intent = new Intent(mContext, BindPhoneActivity.class);
            startActivityForResult(intent, Constant.RequestCode.RQ_BIND_PHONE);
        });
    }


    private void bindWx() {
        mViewModel.thirdlogin(SHARE_MEDIA.WEIXIN.getName(), BindWechatActivity.this)
                .observe(this, new BaseObserver<BaseApiResult<BindWx>>() {
                    @Override
                    public void onError(String msg, int code) {
                        toastShort(msg);
                    }

                    @Override
                    public void onSuccess(BaseApiResult<BindWx> result) {
                        if (result.getCode() == 200) {
                            if (!TextUtils.isEmpty(wx_id) && !result.getData().getWx_id().equals(wx_id)) {
                                ToastUtils.showToast("此账号已经绑定了微信");

                            } else {
                                wx_id = result.getData().getWx_id();
                                updateWxBindUi(1);
                                isBindWx();
                            }
                        }
                    }
                });
    }


    private void updateWxBindUi(int isBind) {
        isBindWx = isBind;
        WxBindResultDialog dialog = new WxBindResultDialog(mContext);
        dialog.setDialogType(isBindWx);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.RequestCode.RQ_BIND_PHONE) {
                initBindView();
            }
        }
    }
}
