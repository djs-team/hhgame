package com.deepsea.mua.app.im.widget;

import android.content.Context;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.deepsea.mua.app.im.Constant;
import com.deepsea.mua.app.im.HxHelper;
import com.deepsea.mua.im.widget.chatrow.EaseChatRow;
import com.deepsea.mua.im.widget.presenter.EaseChatRowPresenter;

/**
 * Created by zhangsong on 17-10-12.
 */

public class ChatRowLivePresenter extends EaseChatRowPresenter {
    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new ChatRowConferenceInvite(cxt, message, position, adapter);
    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);

        String confId = message.getStringAttribute(Constant.EM_CONFERENCE_ID, "");
        String confPassword = message.getStringAttribute(Constant.EM_CONFERENCE_PASSWORD,"");
        int type = message.getIntAttribute(Constant.EM_CONFERENCE_TYPE, 0);
        HxHelper.getInstance().goLive(confId, confPassword, message.getFrom());
    }

}
