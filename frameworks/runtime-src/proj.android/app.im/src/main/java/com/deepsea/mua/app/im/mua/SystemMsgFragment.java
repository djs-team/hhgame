package com.deepsea.mua.app.im.mua;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.deepsea.mua.app.im.R;
import com.deepsea.mua.app.im.adapter.SysMsgAdapter;
import com.deepsea.mua.app.im.databinding.FragmentSysMsgBinding;
import com.deepsea.mua.app.im.viewmodel.SysMsgViewModel;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.entity.SystemMsgBean;
import com.deepsea.mua.stub.entity.SystemMsgListBean;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.BrushEvent;
import com.deepsea.mua.stub.utils.eventbus.UpdateUnreadMsgEvent;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by JUN on 2019/5/5
 */
public class SystemMsgFragment extends BaseFragment<FragmentSysMsgBinding> {

    @Inject
    ViewModelFactory mModelFactory;
    private SysMsgViewModel mViewModel;

    private SysMsgAdapter mAdapter;

    public static SystemMsgFragment newInstance() {
        SystemMsgFragment instance = new SystemMsgFragment();
//        Bundle bundle = instance.getArguments();
//        if (bundle == null) {
//            bundle = new Bundle();
//            bundle.putSerializable("micMan", (Serializable) micMan);
//            bundle.putString("sex", sex);
//
//            instance.setArguments(bundle);
//        } else {
//            bundle.putSerializable("micMan", (Serializable) micMan);
//            bundle.putString("sex", sex);
//        }
        return instance;
    }
    private boolean isResumed, isHidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isHidden = hidden;
        if (isResumed && !isHidden) {
            mBinding.refreshLayout.autoRefresh();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
        if (!isHidden) {
            mBinding.refreshLayout.autoRefresh();

        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_sys_msg;
    }

    @Override
    protected void initView(View view) {
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SysMsgViewModel.class);
        initRecyclerView();
        initRefreshLayout();
    }

    private void initRecyclerView() {
        mAdapter = new SysMsgAdapter(mContext);
        mAdapter.setOnItemClickListener((view, position) -> {

        });
        mAdapter.setOnItemLongClickListener(new BaseBindingAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                showDelMsgDialog(position);
                return true;
            }
        });
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    private void initRefreshLayout() {
        mBinding.refreshLayout.setOnRefreshListener(refreshLayout -> {
            mViewModel.refresh().observe(this, new BaseObserver<SystemMsgListBean>() {
                @Override
                public void onSuccess(SystemMsgListBean result) {
                    mBinding.refreshLayout.finishRefresh();
                    if (result != null) {
                        List<SystemMsgBean> data = result.getList();
                        if (data != null && data.size() > 0) {
                            mAdapter.setNewData(data);
                        }
                        SystemMsgListBean.PageInfoBean pageInfo = result.getPageInfo();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishRefresh();
                }
            });
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.refresh().observe(this, new BaseObserver<SystemMsgListBean>() {
                @Override
                public void onSuccess(SystemMsgListBean result) {
                    mBinding.refreshLayout.finishLoadMore();
                    if (result != null) {
                        List<SystemMsgBean> data = result.getList();
                        mAdapter.addData(data);
                        SystemMsgListBean.PageInfoBean pageInfo = result.getPageInfo();
                        mBinding.refreshLayout.setEnableLoadMore(pageInfo.getPage() < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mViewModel.pageNumber--;
                    mBinding.refreshLayout.finishLoadMore();
                }
            });
        });

    }

    private void showDelMsgDialog(int pos) {
        String id = mAdapter.getData().get(pos).getId();
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setTitle("是否要删除该消息");
        dialog.setMessage("删除后不可恢复");
        dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog1) {
                dialog1.dismiss();
            }
        });
        dialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog1) {
                dialog1.dismiss();
                Log.d("system", "删除" + pos);
                fetchDelMsg(pos, id);
            }
        });
        dialog.show();

    }

    private void fetchDelMsg(int pos, String id) {
        mViewModel.systemDel(id).observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result.getCode() == 200) {
                    mAdapter.getData().remove(pos);
                    mAdapter.notifyItemRemoved(pos);
                }
            }
        });

    }

    public void showBrushMsgDialog() {
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setTitle("清除未读系统消息");
        dialog.setMessage("消息气泡会消失，消息不会清除");
        dialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
            }
        });
        dialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                Log.d("system", "删除全部");
                dialog.dismiss();
                EventBus.getDefault().post(new UpdateUnreadMsgEvent());

            }
        });
        dialog.show();
    }
}
