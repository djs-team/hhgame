package com.deepsea.mua.app.im.mua;

import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendAddAdapter;
import com.deepsea.mua.app.im.databinding.ActivityFriendAddBinding;
import com.deepsea.mua.app.im.viewmodel.AddFriendViewModel;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AddFriendWithGiftDialog;
import com.deepsea.mua.stub.dialog.AddFriendWithNoGiftDialog;

import com.deepsea.mua.stub.entity.WalletBean;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


/**
 * Created by JUN on 2019/5/7
 */
@Route(path = ArouterConst.PAGE_MSG_ADDFRIEND)
public class FriendAddActivity extends BaseActivity<ActivityFriendAddBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private AddFriendViewModel mViewModel;
    @Autowired
    String touid;
    @Autowired
    String photo;
    @Autowired
    String nickName;
    @Autowired
    boolean isAddNoGift;

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        touid = savedInstanceState.getString("touid");
        photo = savedInstanceState.getString("photo");
        nickName = savedInstanceState.getString("nickName");
        isAddNoGift = savedInstanceState.getBoolean("isAddNoGift");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("touid", touid);
        outState.putString("photo", photo);
        outState.putString("nickName", nickName);
        outState.putBoolean("isAddNoGift", isAddNoGift);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra("uid")) {
            touid = intent.getStringExtra("uid");
        }
        if (intent.hasExtra("photo")) {
            photo = intent.getStringExtra("photo");
        }
        if (intent.hasExtra("nickName")) {
            nickName = intent.getStringExtra("nickName");
        }
        isAddNoGift = intent.getBooleanExtra("isAddNoGift", false);
    }

    public void setTextViewStyles(TextView text) {

        LinearGradient mLinearGradient = new LinearGradient(0, 0, text.getMeasuredWidth() / 2, 0, Color.parseColor("#CE54C8"), Color.parseColor("#9344E5"), Shader.TileMode.CLAMP);
        text.getPaint().setShader(mLinearGradient);
        text.invalidate();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_friend_add;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(AddFriendViewModel.class);
        initUser();
        initTabLayout();
        initViewPager();
        requestBalance();
        setTextViewStyles(mBinding.btnBuyMore);
    }

    @Override
    protected void initListener() {
        subscribeClick(mBinding.btnBuyMore, o -> {
            checkParentLock();
        });
        subscribeClick(mBinding.ivUserhead, o -> {
            PageJumpUtils.jumpToProfile(touid);
        });
    }

    private void initUser() {
        GlideUtils.circleImage(mBinding.ivUserhead, photo, R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        String msg = String.format(getResources().getString(R.string.msg_leaving_words), nickName);

        mBinding.tvUsername.setText(msg);
    }

    private FriendAddAdapter mAdapter;

    private void initTabLayout() {
    }

    boolean isSendAddMessage = false;

    private void initViewPager() {
        mAdapter = new FriendAddAdapter
                (getSupportFragmentManager(), touid, nickName, new FriendAddAdapter.OnFriendAddListener() {
                    @Override
                    public void onSendRequest() {
                        isSendAddMessage = true;
                    }
                });

        mBinding.viewPager.setAdapter(mAdapter);
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.tabLayout.setViewPager(mBinding.viewPager);
        String isMatchmaker = UserUtils.getUser().getIs_matchmaker();
        if (!TextUtils.isEmpty(isMatchmaker) && isMatchmaker.equals("1")) {
            //是红娘
            mBinding.btnAddFriendNogift.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog();
                }
            });
        }
        ViewBindUtils.setVisible(mBinding.btnAddFriendNogift, isAddNoGift);
    }

    AddFriendWithNoGiftDialog dialog = null;

    private void showDialog() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new AddFriendWithNoGiftDialog(mContext);
        dialog.setRightButton("不送礼加好友", new AddFriendWithNoGiftDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog d) {
                String inputMsg = dialog.getInputMsg();
                dialog.dismiss();
                showProgress("请稍后");
                Map<String, String> map = new HashMap<>();
                map.put("touid", touid);
                map.put("friendmsg", inputMsg);
                map.put("type", "");
                onAddFriendEvent(map);
            }
        });
        dialog.show();
    }

    boolean isAddFriendSuccess = false;

    private void onAddFriendEvent(Map<String, String> map) {
        mViewModel.addFriendly(map).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                isSendAddMessage = true;
                if (result.getCode() == 200) {
                    toastShort("已发送");
                    isAddFriendSuccess = true;
                } else {
                    toastShort(result.getDesc());
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                hideProgress();
                Intent intent = new Intent();
                intent.putExtra("uid", touid);
                setResult(Activity.RESULT_OK, intent);
            }
        });
    }


    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    private void requestBalance() {
        mViewModel.wallet().observe(this, new BaseObserver<WalletBean>() {
            @Override
            public void onSuccess(WalletBean result) {
                if (result != null) {
                    balance = String.valueOf(result.getCoin());
                    mBinding.tvAccountNum.setText("X" + result.getCoin() + "朵");

                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
            }
        });
    }

    private String balance;

    private void checkParentLock() {
        showProgress();
        mViewModel.checkSatus().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                hideProgress();
                PageJumpUtils.jumpToRecharge(balance + "");
            }

            @Override
            public void onError(String msg, int code) {
                hideProgress();
                if (code == 204 || code == 205) {
                    toastShort(msg);
                } else {
                    super.onError(msg, code);
                }
            }
        });
    }


    @Override
    public void finish() {
        if (isSendAddMessage) {
            Intent intent = new Intent();
            intent.putExtra("uid", touid);
            setResult(Activity.RESULT_OK, intent);
        }

        super.finish();
    }
}
