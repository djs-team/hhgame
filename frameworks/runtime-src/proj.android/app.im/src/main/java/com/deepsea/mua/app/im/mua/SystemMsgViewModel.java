package com.deepsea.mua.app.im.mua;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Pair;

import com.deepsea.mua.core.network.AppExecutors;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/8/27
 */
public class SystemMsgViewModel extends ViewModel {

    private int mPageSize = 20;

    private String mLoadMoreMsgId;

    @Inject
    AppExecutors mExecutors;

    @Inject
    public SystemMsgViewModel() {
    }

    public int getPageSize() {
        return mPageSize;
    }

    public LiveData<List<EMMessage>> refresh(EMConversation conversation) {
        mLoadMoreMsgId = null;
        MediatorLiveData<List<EMMessage>> liveData = new MediatorLiveData<>();
        mExecutors.diskIO().execute(() -> {
            List<EMMessage> list = conversation.loadMoreMsgFromDB(null, mPageSize);

            if (!CollectionUtils.isEmpty(list)) {
                mLoadMoreMsgId = list.get(0).getMsgId();

                List<Pair<Long, EMMessage>> sortList = new ArrayList<>();
                for (EMMessage message : list) {
                    sortList.add(new Pair<>(message.getMsgTime(), message));
                }
                try {
                    // Internal is TimSort algorithm, has bug
                    sortMessageByChatTime(sortList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.clear();
                for (Pair<Long, EMMessage> sortItem : sortList) {
                    list.add(sortItem.second);
                }
            }

            liveData.postValue(list);
        });
        return liveData;
    }

    public LiveData<List<EMMessage>> loadMore(EMConversation conversation) {
        MediatorLiveData<List<EMMessage>> liveData = new MediatorLiveData<>();
        mExecutors.diskIO().execute(() -> {
            List<EMMessage> list = conversation.loadMoreMsgFromDB(mLoadMoreMsgId, mPageSize);

            if (!CollectionUtils.isEmpty(list)) {
                mLoadMoreMsgId = list.get(0).getMsgId();

                List<Pair<Long, EMMessage>> sortList = new ArrayList<>();
                for (EMMessage message : list) {
                    sortList.add(new Pair<>(message.getMsgTime(), message));
                }
                try {
                    // Internal is TimSort algorithm, has bug
                    sortMessageByChatTime(sortList);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                list.clear();
                for (Pair<Long, EMMessage> sortItem : sortList) {
                    list.add(sortItem.second);
                }
            }

            liveData.postValue(list);
        });
        return liveData;
    }

    private void sortMessageByChatTime(List<Pair<Long, EMMessage>> messageList) {
        Collections.sort(messageList, (con1, con2) -> {
            if (con1.first.equals(con2.first)) {
                return 0;
            } else if (con2.first > con1.first) {
                return 1;
            } else {
                return -1;
            }
        });
    }
}
