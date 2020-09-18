package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.entity.PageBean;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.MicroSort;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.UpdateApplyMicEvent;
import com.deepsea.mua.stub.utils.eventbus.UpdateInRoomMemberEvent;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FullServiceUsersAdapter;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.databinding.FragmentFullServiceManBinding;
import com.deepsea.mua.voice.databinding.FragmentSortApplyBinding;
import com.deepsea.mua.voice.dialog.FullServiceUserDetailDialog;
import com.deepsea.mua.voice.dialog.FullServiceUserDialog;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.utils.inter.OnManageListener;
import com.deepsea.mua.voice.viewmodel.FullServiceSortModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SortApplyMicFragment extends BaseFragment<FragmentSortApplyBinding> {
    private static OnManageListener mListener;

    public SortApplyMicFragment() {

    }

    private List<MicroOrder> mData = new ArrayList<>();
    private String sex;


    public static SortApplyMicFragment newInstance(List<MicroOrder> micMan,String sex, OnManageListener listener) {
        mListener = listener;
        SortApplyMicFragment instance = new SortApplyMicFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putSerializable("micMan", (Serializable) micMan);
            bundle.putString("sex", sex);

            instance.setArguments(bundle);
        } else {
            bundle.putSerializable("micMan", (Serializable) micMan);
            bundle.putString("sex", sex);
        }
        return instance;
    }

    public void setData(List<MicroOrder> data) {
        if (data != null && data.size() > 0) {
            this.mData = data;
            adapter.setNewData(mData);
        } else {
                mData.clear();
                adapter.notifyDataSetChanged();

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateApplyMicEvent event) {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sort_apply;
    }

    private FullServiceSortModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    protected void initView(View view) {
        sex = mBundle.getString("sex");
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(FullServiceSortModel.class);
        mData = (List<MicroOrder>) mBundle.getSerializable("micMan");
        initRecyclerView();
    }

    MicroSortAdapter adapter;

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (mBinding.recyclerView.getItemAnimator() != null) {
            mBinding.recyclerView.getItemAnimator().setChangeDuration(0);
        }

        adapter = new MicroSortAdapter(mContext);
        adapter.setSex(sex);
        adapter.setManager(true);
        adapter.setOnMicroListener(new MicroSortAdapter.OnMicroListener() {
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


    }


}
