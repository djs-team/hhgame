package com.deepsea.mua.app.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.EmActivityChatBinding;
import com.deepsea.mua.app.im.runtimepermissions.PermissionsManager;
import com.deepsea.mua.im.EaseConstant;
import com.deepsea.mua.im.EaseUI;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.utils.AndroidWorkaround;
import com.deepsea.mua.stub.utils.ArouterConst;

/**
 */
@Route(path = ArouterConst.PAGE_CHAT)
public class ChatActivity extends BaseActivity<EmActivityChatBinding> {

    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;

    @Autowired(name = "userId")
    String toChatUsername;
    @Autowired(name = "userNickName")
    String userNickName;
    @Autowired(name = "userInfo")
    String onLineState;
    @Autowired(name = EaseConstant.EXTRA_USER_ONLINE)

    String userInfo;

    @Override
    protected void handleIntent(Intent intent, boolean isFromNewIntent) {
        if (intent.hasExtra("userId")) {
            toChatUsername = intent.getStringExtra("userId");
            userNickName = intent.getStringExtra("userNickName");
            userInfo = intent.getStringExtra("userInfo");
            onLineState=intent.getStringExtra(EaseConstant.EXTRA_USER_ONLINE);
        }
    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {
        toChatUsername = savedInstanceState.getString("userId");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.em_activity_chat;
    }

    @Override
    protected void initView() {
        activityInstance = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString(EaseConstant.EXTRA_USER_ID, toChatUsername);
        bundle.putString(EaseConstant.EXTRA_USER_NICKNAME, userNickName);
        bundle.putString(EaseConstant.EXTRA_USER_INFO, userInfo);
        bundle.putString(EaseConstant.EXTRA_USER_ONLINE, onLineState);

        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        AndroidWorkaround.assistActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EaseUI.getInstance().getNotifier().reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("userId", toChatUsername);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
