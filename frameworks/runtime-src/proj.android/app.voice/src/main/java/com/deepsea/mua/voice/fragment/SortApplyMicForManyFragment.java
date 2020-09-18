package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.MicroInRoomData;
import com.deepsea.mua.stub.utils.eventbus.UpdateApplyMicEvent;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.adapter.MicroSortForManyAdapter;
import com.deepsea.mua.voice.databinding.FragmentSortApplyBinding;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.utils.inter.OnManageListener;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SortApplyMicForManyFragment extends BaseFragment<FragmentSortApplyBinding> {
    private static OnManageListener mListener;

    public SortApplyMicForManyFragment() {

    }

    private List<MicroOrder> mData = new ArrayList<>();

    public static SortApplyMicForManyFragment newInstance(List<MicroOrder> micMan, OnManageListener listener) {
        mListener = listener;
        SortApplyMicForManyFragment instance = new SortApplyMicForManyFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putSerializable("micMan", (Serializable) micMan);

            instance.setArguments(bundle);
        } else {
            bundle.putSerializable("micMan", (Serializable) micMan);
        }
        return instance;
    }

    public void setData(List<MicroOrder> data) {
//        if (data != null && data.size() > 0) {
//            this.mData = data;
//            adapter.setNewData(mData);
//        } else {
//            mData.clear();
//            adapter.notifyDataSetChanged();
//        }
    }

    private int tabType = 0;

    public void setTabType(int tabType) {
        this.tabType = tabType;
        List<MicroOrder> dataSofa = new ArrayList<>();
        List<MicroOrder> dataCommon = new ArrayList<>();
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).getLevel() == 4) {
                dataSofa.add(mData.get(i));
            } else {
                dataCommon.add(mData.get(i));
            }
        }
        if (tabType == 1) {
            adapter.setNewData(dataSofa);
        } else if (tabType == 2) {
            adapter.setNewData(dataCommon);
        }

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEvent(UpdateApplyMicEvent event) {
//        adapter.notifyDataSetChanged();
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MicroInRoomData event) {
        adapter.setNewData(event.microOrders);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sort_apply;
    }



    @Override
        protected void initView(View view) {

        EventBus.getDefault().register(this);
        mData = (List<MicroOrder>) mBundle.getSerializable("micMan");
        initRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    MicroSortForManyAdapter adapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }

        adapter = new MicroSortForManyAdapter(mContext);
        adapter.setManager(true);
        adapter.setOnMicroListener(new MicroSortForManyAdapter.OnMicroListener() {
            @Override
            public void onTopMicro(String uid) {
                if (mListener != null) {
                    mListener.onTopMicro(uid);
                }
            }

            @Override
            public void onOnWheat(String uid) {
                if (mListener != null) {
                    mListener.onOnWheat(uid);
                }
            }

            @Override
            public void onRemove(String uid) {
                if (mListener != null) {
                    mListener.onRemove(uid);
                }
            }
        });
        adapter.setNewData(mData);
        mBinding.recyclerView.setAdapter(adapter);
        JoinRoom joinRoom=AppConstant.getInstance().getJoinRoom();
        if (joinRoom.getRoomMode()==8){
            setTabType(1);
        }


    }


}
