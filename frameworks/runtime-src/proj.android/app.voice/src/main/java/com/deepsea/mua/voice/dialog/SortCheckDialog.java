package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.databinding.DialogMicroSortBinding;

import java.util.List;
import java.util.Locale;

/**
 * 麦序查看
 * Created by JUN on 2019/4/23
 */
public class SortCheckDialog extends BaseDialog<DialogMicroSortBinding> {

    private MicroSortAdapter mAdapter;
    private OnCancelSortListener mListener;

    public interface OnCancelSortListener {
        void onCancel();
    }

    public SortCheckDialog(@NonNull Context context) {
        super(context);
        initRecyclerView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_micro_sort;
    }

    @Override
    protected void initListener() {
        ViewBindUtils.RxClicks(mBinding.cancelSort, o -> {
            if (mListener != null) {
                mListener.onCancel();
            }
            dismiss();
        });
    }

    public void setOnCancelSortListener(OnCancelSortListener listener) {
        this.mListener = listener;
    }

    private void initRecyclerView() {
        mAdapter = new MicroSortAdapter(mContext);
        mBinding.microSortRv.setNestedScrollingEnabled(false);
        mBinding.microSortRv.setHasFixedSize(true);
        mBinding.microSortRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.microSortRv.setAdapter(mAdapter);
    }

    public void setData(List<MicroOrder> list) {
        if (CollectionUtils.isEmpty(list)) {
            dismiss();
            return;
        }

        boolean contains = false;
        for (int i = 0; i < list.size(); i++) {
            MicroOrder sort = list.get(i);
            String userId = sort.getUser().getUserId();
            if (userId.equals(UserUtils.getUser().getUid())) {
                WsUser user = sort.getUser();
                mBinding.include.setIsSelf(true);
                mBinding.include.sortTv.setText(mAdapter.sortPos(i));
                ViewBindUtils.setTextColor(mBinding.include.sortTv, R.color.white);
                GlideUtils.circleImage(mBinding.include.avatarIv, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
                mBinding.include.nickTv.getPaint().setFakeBoldText(true);
                mBinding.include.nickTv.setText(user.getName());
                ViewBindUtils.setTextColor(mBinding.include.nickTv, R.color.white);
                mBinding.include.microSortTv.setText(String.format(Locale.CHINA, "前方还有%d名用户", i));
                contains = true;
                break;
            }
        }

        if (!contains) {
            dismiss();
            return;
        }

        mAdapter.setNewData(list);
    }
}
