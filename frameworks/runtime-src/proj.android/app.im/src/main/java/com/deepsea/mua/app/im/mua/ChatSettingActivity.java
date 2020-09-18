package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.ViewModelProviders;
import android.view.Gravity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.app.im.HxSettingModel;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.ActivityChatSettingBinding;
import com.deepsea.mua.app.im.databinding.ActivityMsgSettingBinding;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.CheckBlackVo;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.hyphenate.chat.EMClient;

import javax.inject.Inject;

/**
 * 聊天设置---拉黑和举报
 */
@Route(path = ArouterConst.PAGE_MSG_CHAT_SETTING)
public class ChatSettingActivity extends BaseActivity<ActivityChatSettingBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private MsgSettingViewModel mViewModel;
    private String uid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat_setting;
    }

    @Override
    protected void initView() {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(MsgSettingViewModel.class);
        uid = getIntent().getStringExtra("uid");
        checkBlack();
    }

    @Override
    protected void initListener() {
        mBinding.blockIv.setOnClickListener(v -> {
//            mBinding.blockIv.setSelected(!mBinding.blockIv.isSelected());
            if (mBinding.blockIv.isSelected()) {
                //取消拉黑
                showBlackoutDlg();
            } else {
                //拉黑
                showBlackDlg();
            }
        });
        mBinding.rlReport.setOnClickListener(v -> {
            PageJumpUtils.jumpToReport(uid);
        });
    }

    private void checkBlack() {
        mViewModel.isBlack(uid).observe(this,
                new ProgressObserver<CheckBlackVo>(mContext) {
                    @Override
                    public void onSuccess(CheckBlackVo result) {
                        if (result != null) {
                            int isBlack = result.getIs_block();
                            if (isBlack != 0) {
                                mBinding.blockIv.setSelected(isBlack == 2);
                            }
                        }

                    }
                });
    }

    private AAlertDialog mAlertDialog;

    private void showBlackDlg() {
        if (mAlertDialog == null) {
            mAlertDialog = new AAlertDialog(mContext);
        }
        mAlertDialog.setTitle("拉黑好友");
        mAlertDialog.setMessage("拉黑好友之后将无法再接TA的任何消息。");
        mAlertDialog.getMessageTv().setGravity(Gravity.LEFT);
        mAlertDialog.setLeftButton("确定", (v, dialog1) -> {
            dialog1.dismiss();
            defriend();
        });
        mAlertDialog.setRightButton("取消", null);
        mAlertDialog.show();
    }

    private void showBlackoutDlg() {
        if (mAlertDialog == null) {
            mAlertDialog = new AAlertDialog(mContext);
        }
        mAlertDialog.setTitle("取消拉黑");
        mAlertDialog.setMessage("是否将TA从黑名单中移除？");
        mAlertDialog.setLeftButton("确定", (v, dialog1) -> {
            dialog1.dismiss();
            blackout();
        });
        mAlertDialog.setRightButton("取消", null);
        mAlertDialog.show();
    }

    private void defriend() {
        mViewModel.defriend(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        EMClient.getInstance().chatManager().deleteConversation(uid, true);
                        mBinding.blockIv.setSelected(true);
                        toastShort(result);
                    }
                });
    }

    private void blackout() {
        mViewModel.blackout(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        mBinding.blockIv.setSelected(false);

                        toastShort(result);
                    }
                });
    }

}
