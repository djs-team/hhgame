package com.deepsea.mua.app.im.mua;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.deepsea.mua.app.im.Constant;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.databinding.FragmentConversationBinding;
import com.deepsea.mua.app.im.ui.ChatActivity;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.im.model.EaseAtMessageHelper;
import com.deepsea.mua.im.model.EaseDingMessageHelper;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.client.hyphenate.IEMMessageListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * conversation list fragment
 */
public class ConversationFragment extends BaseFragment<FragmentConversationBinding> {

    private final static int MSG_REFRESH = 2;

    private List<EMConversation> conversationList = new ArrayList<>();

    private ConversationAdapter mAdapter;


    protected boolean hidden;
    protected boolean isConflict;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void initView(View view) {
        conversationList.addAll(loadConversationList());
        initRecyclerView();
        EMClient.getInstance().addConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        HxHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(mDataSyncListener);
    }

    private void initRecyclerView() {
        if (mAdapter == null) {
            mAdapter = new ConversationAdapter(mContext);
        }
        mAdapter.setOnItemClickListener(mItemClickListener);
        mAdapter.setOnItemLongClickListener((view, position) -> {
            showConversationPop(view, position);
            return false;
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
        mAdapter.setNewData(conversationList);
    }

    private BaseBindingAdapter.OnItemClickListener mItemClickListener = (view, position) -> {
        EMConversation conversation = mAdapter.getItem(position);
        String username = conversation.conversationId();

        if (isSystemConversation(conversation)) {
            Intent intent = new Intent(getActivity(), SystemMsgActivity.class);
            intent.putExtra(Constant.EXTRA_USER_ID, username);
            startActivity(intent);
        } else if (username.equals(EMClient.getInstance().getCurrentUser()))
            Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
        else {
            // start chat activity
            Intent intent = new Intent(getActivity(), ChatActivity.class);
            if (conversation.isGroup()) {
                if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                    // it's group chat
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
                } else {
                    intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
                }

            }
            // it's single chat
            intent.putExtra(Constant.EXTRA_USER_ID, username);
            startActivity(intent);
        }
    };

    private void showConversationPop(View anchor, int position) {


//
    }

    private HxHelper.DataSyncListener mDataSyncListener = success -> refresh();

    private EMMessageListener messageListener = new IEMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                if (message.getChatType() == EMMessage.ChatType.ChatRoom)
                    continue;

                HxHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }
    };

    private void refreshUIWithMessage() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::refresh);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = mAdapter.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        if (tobeDeleteCons.getType() == EMConversation.EMConversationType.GroupChat) {
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }

        refresh();

        // update unread count
//        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
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
            isConflict = false;
            handler.sendEmptyMessage(1);
        }
    };

    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_REFRESH: {
                    conversationList.clear();
                    conversationList.addAll(loadConversationList());
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public void refresh() {
        if (!handler.hasMessages(MSG_REFRESH)) {
            handler.sendEmptyMessage(MSG_REFRESH);
        }
    }

    private boolean isSystemConversation(EMConversation conversation) {
        if (mAdapter == null) {
            mAdapter = new ConversationAdapter(mContext);
        }
        return mAdapter.isSystemConversation(conversation);
    }

    /**
     * load conversation list
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // get all conversations
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<>();

        //the conversation id 100 - 150
        List<Pair<Long, EMConversation>> systemConversations = new ArrayList<>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    if (isSystemConversation(conversation)) {
                        systemConversations.add(new Pair<>(Long.parseLong(conversation.conversationId()), conversation));
                    } else {
                        sortList.add(new Pair<>(conversation.getLastMessage().getMsgTime(), conversation));
                    }
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
        for (Pair<Long, EMConversation> sortItem : systemConversations) {
            EMMessage lastMessage = sortItem.second.getLastMessage();
            if (lastMessage != null && lastMessage.getChatType() == EMMessage.ChatType.ChatRoom) {
                continue;
            }

            list.add(sortItem.second);
        }
        return list;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        HxHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(mDataSyncListener);
    }
}
