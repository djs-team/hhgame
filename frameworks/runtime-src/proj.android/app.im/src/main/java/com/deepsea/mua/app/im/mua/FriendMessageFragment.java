package com.deepsea.mua.app.im.mua;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import com.deepsea.mua.app.im.Constant;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.FriendMsgAdapter;
import com.deepsea.mua.app.im.databinding.FragmentFriendMessageBinding;
import com.deepsea.mua.app.im.ui.ChatActivity;
import com.deepsea.mua.app.im.viewmodel.FriendListViewModel;
import com.deepsea.mua.im.utils.ChatUserInfoController;
import com.deepsea.mua.im.utils.EaseUserUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.deepsea.mua.stub.controller.OnlineController;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.FriendHXInfo;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.StateUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.HeartBeatEvent;
import com.deepsea.mua.stub.utils.eventbus.UpdateUnreadMsgEvent;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/3/22
 */
public class FriendMessageFragment extends BaseFragment<FragmentFriendMessageBinding> {
    @Inject
    ViewModelFactory mModelFactory;
    private FriendListViewModel mViewModel;
    private FriendMsgAdapter mAdapter;
    protected boolean isConflict;

    public static FriendMessageFragment newInstance() {
        FriendMessageFragment instance = new FriendMessageFragment();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_message;
    }

    private void showDelMsgDialog(String userId, int pos) {
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setTitle("是否要删除该会话");
        dialog.setMessage("删除后不可恢复，但可收到对方消息");
        dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog d) {
                dialog.dismiss();

                EMClient.getInstance().chatManager().deleteConversation(userId, true);
                mAdapter.remove(pos);
                mAdapter.notifyItemRemoved(pos);
                EventBus.getDefault().post(new UpdateUnreadMsgEvent());

            }
        });
        dialog.show();
    }

    public void showBrushMsgDialog() {
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setTitle("清除未读好友消息");
        dialog.setMessage("消息气泡会消失，消息不会清除");
        dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog d) {
                dialog.dismiss();
                EMClient.getInstance().chatManager().markAllConversationsAsRead();
                if (mAdapter.getData() != null) {
                    for (FriendInfoBean bean : mAdapter.getData()) {
                        bean.setUnReadCount(0);
                        mAdapter.notifyDataSetChanged();
                    }
                }
                EventBus.getDefault().post(new UpdateUnreadMsgEvent());

            }
        });
        dialog.show();
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FriendListViewModel.class);
        initRecyclerView();
        initRefreshLayout();
        initHX();

    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE || error == EMError.SERVER_SERVICE_RESTRICTED
                    || error == EMError.USER_KICKED_BY_CHANGE_PASSWORD || error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
                isConflict = true;
            } else {
                if (NetUtils.hasNetwork(getActivity())) {
//                    toastShort(getString(R.string.can_not_connect_chat_server_connection));
                } else {
                    toastShort(getString(R.string.the_current_network));
                }
            }
        }

        @Override
        public void onConnected() {
            isConflict = true;
            mHandler.sendEmptyMessage(msg_refesh);
        }
    };

    protected List<FriendHXInfo> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();

        //the conversation id 100 - 150
        List<Pair<Long, EMConversation>> systemConversations = new ArrayList<>();
        /**
         *
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(systemConversations);
            sortConversationByLastChatTime(sortList);
            systemConversations.addAll(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<>();
        List<FriendHXInfo> hxInfoList = new ArrayList<>();
        for (Pair<Long, EMConversation> sortItem : systemConversations) {
            EMMessage lastMessage = sortItem.second.getLastMessage();
            if (lastMessage != null && lastMessage.getChatType() == EMMessage.ChatType.Chat) {
                list.add(sortItem.second);

                String username = sortItem.second.conversationId();
                hxInfoList.add(new FriendHXInfo(username, lastMessage, sortItem.second.getUnreadMsgCount()));
            }
        }
        return hxInfoList;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, (con1, con2) -> {
            if (con1.first.equals(con2.first)) {
                return 0;
            } else if (con2.first > con1.first) {
                return 1;
            } else {
                return -1;
            }
        });
    }

    private EMMessageListener messageListener = new IEMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                if (message.getChatType() == EMMessage.ChatType.ChatRoom)
                    continue;

                HxHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
            }
//            refreshUIWithMessage();
            mHandler.sendEmptyMessage(msg_refesh);
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
//            refreshUIWithMessage();
            mHandler.sendEmptyMessage(msg_refesh);
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
//            refreshUIWithMessage();
            mHandler.sendEmptyMessage(msg_refesh);
        }
    };
    private HxHelper.DataSyncListener mDataSyncListener = new HxHelper.DataSyncListener() {
        @Override
        public void onSyncComplete(boolean success) {
//            mBinding.refreshLayout.autoRefresh();
            mHandler.sendEmptyMessage(msg_refesh);
        }
    };

    private void initHX() {
        EMClient.getInstance().addConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        HxHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(mDataSyncListener);
    }

    private void initRecyclerView() {
        mAdapter = new FriendMsgAdapter(mContext);
        mAdapter.setOnItemLongClickListener(new BaseBindingAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                showDelMsgDialog(mAdapter.getData().get(position).getUser_id(), position);
                return true;
            }
        });
        mAdapter.setOnItemClickListener((view, position) -> {
            FriendInfoBean bean = mAdapter.getItem(position);
            String userId = bean.getUser_id();
            String city = bean.getCity();
            String state = bean.getState();
            StringBuilder info = new StringBuilder();
            info.append(bean.getAge());
            info.append("岁");
            if (city != null && !TextUtils.isEmpty(city)) {
                info.append(" |");
                info.append(" " + city);
            }
            if (state != null && !state.equals("0")) {
                info.append(" |");
                info.append(" " + StateUtils.getState(state));
            }

            Intent intent = new Intent(getActivity(), ChatActivity.class);
            // it's single chat
            intent.putExtra(Constant.EXTRA_USER_ID, userId);
            intent.putExtra(Constant.EXTRA_USER_INFO, info.toString());
            intent.putExtra(Constant.EXTRA_USER_NICKNAME, bean.getNickname());
            intent.putExtra(Constant.EXTRA_USER_ONLINE, bean.getOnline());
            ChatUserInfoController.getInstance().setTochatUserAvatar(bean.getAvatar());
            EaseUserUtils.setCurrentUserAvatar(UserUtils.getUser().getAvatar());
            startActivity(intent);
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }


    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            refresh();
        });
    }


    private void refresh() {
        mViewModel.getFriendList().observe(this, new BaseObserver<FriendInfoListBean>() {
            @Override
            public void onSuccess(FriendInfoListBean result) {

                if (result != null) {
                    mBinding.refreshLayout.finishRefresh();
                    if (result.getList() != null && result.getList().size() > 0) {
                        FriendsUtils.getInstance().saveFriendUtils(result.getList());
                        setFriendData(result.getList());
                    }

                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
                mBinding.refreshLayout.finishRefresh();
            }
        });
    }

    private void setFriendData(List<FriendInfoBean> friendData) {
        List<FriendInfoBean> friendChat = new ArrayList<>();
        List<FriendHXInfo> messageList = loadConversationList();
        if (messageList != null) {
            for (int i = 0; i < friendData.size(); i++) {
                for (int j = 0; j < messageList.size(); j++) {
                    FriendInfoBean friendInfoBean = friendData.get(i);
                    FriendHXInfo friendHXInfo = messageList.get(j);
                    if (friendInfoBean.getUser_id().equals(friendHXInfo.getUserId())) {
                        friendInfoBean.setLastMsg(friendHXInfo.getMessage());
                        friendInfoBean.setTime(friendHXInfo.getMessage().getMsgTime());
                        friendInfoBean.setUnReadCount(friendHXInfo.getUnreadCount());
                        friendChat.add(friendInfoBean);
                    }
                }
            }
            Collections.sort(friendChat, new Comparator<FriendInfoBean>() {

                @Override
                public int compare(FriendInfoBean t1, FriendInfoBean t2) {
                    // TODO Auto-generated method stub
                    if (t1.getTime() < t2.getTime()) {
                        return 1;
                    }
                    if (t1.getTime() > t2.getTime()) {
                        return -1;
                    }
                    return 0;
                }
            });
            mAdapter.setNewData(friendChat);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        HxHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(mDataSyncListener);
        unregisterEventBus(this);
    }

    private final int msg_refesh = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_refesh:
                    mBinding.refreshLayout.autoRefresh();
                    break;

            }

        }
    };
    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
        if (!hidden && !isConflict) {
            refresh();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!hidden) {
            refresh();
        }
    }
}
