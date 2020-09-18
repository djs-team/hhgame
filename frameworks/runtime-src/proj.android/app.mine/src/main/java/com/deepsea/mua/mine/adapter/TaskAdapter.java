package com.deepsea.mua.mine.adapter;

import android.content.Context;
import android.graphics.Color;

import com.deepsea.mua.mine.R;
import com.deepsea.mua.mine.databinding.ItemApplyHostBinding;
import com.deepsea.mua.mine.databinding.ItemTaskBinding;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ApplyHost;
import com.deepsea.mua.stub.entity.TaskBean;
import com.deepsea.mua.stub.utils.ViewBindUtils;

import java.util.List;

/**
 * Created by JUN on 2019/5/5
 */
public class TaskAdapter extends BaseBindingAdapter<TaskBean.TaskItem, ItemTaskBinding> {

    private OnMyClickListener mListener;
    private List<Integer> icons;

    public TaskAdapter(Context context, List<Integer> icons) {
        super(context);
        this.icons = icons;

    }

    boolean isShowMore = false;

    public boolean isShowMore() {
        return isShowMore;
    }

    public void setShowMore(boolean showMore) {
        isShowMore = showMore;
    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (mData == null) {
            return 0;
        } else {
            if (isShowMore) {
                return mData.size();
            } else {
                return mData.size() >= 4 ? 4 : mData.size();
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_task;
    }

    @Override
    protected void bind(BindingViewHolder<ItemTaskBinding> holder, TaskBean.TaskItem item) {
        ViewBindUtils.setText(holder.binding.tvTaskName, item.getName());
        ViewBindUtils.setText(holder.binding.tvTaskDesc, item.getDesc());
        if (item.getState().equals("16")) {
            ViewBindUtils.setText(holder.binding.tvTaskFlowerNum, "上麦卡 *" + item.getReward());
        } else if (item.getState().equals("19")) {
            ViewBindUtils.setText(holder.binding.tvTaskFlowerNum, "+" + item.getReward() + "（不限次数）");
        } else {
            ViewBindUtils.setText(holder.binding.tvTaskFlowerNum, "+" + item.getReward());
        }
        ViewBindUtils.setImageRes(holder.binding.ivTaskLogo, icons.get(holder.getLayoutPosition()));
//        1:进行中  2:已完成   3:失效   4:未领取奖励
        if (item.getState().equals("1")) {
            ViewBindUtils.setText(holder.binding.tvTaskState, "去完成");
            holder.binding.tvTaskState.setWithBackgroundColor(Color.parseColor("#FD477B"));

        } else if (item.getState().equals("4")) {
            holder.binding.tvTaskState.setWithBackgroundColor(Color.parseColor("#FEA201"));
            ViewBindUtils.setText(holder.binding.tvTaskState, "领取奖励");
        } else {
            if (item.getState().equals("2")) {
                ViewBindUtils.setText(holder.binding.tvTaskState, "已完成");
            } else if (item.getState().equals("3")) {
                ViewBindUtils.setText(holder.binding.tvTaskState, "失效");
            }
            holder.binding.tvTaskState.setWithBackgroundColor(Color.parseColor("#666667"));
            ViewBindUtils.setEnable(holder.binding.tvTaskState, false);
        }
        ViewBindUtils.RxClicks(holder.binding.tvTaskState, o -> {
            if (mListener != null) {
                mListener.confirm(holder.getLayoutPosition(), item.getType(), item.getState());
            }
        });

    }

    public void setmListener(OnMyClickListener mListener) {
        this.mListener = mListener;
    }

    public interface OnMyClickListener {

        void confirm(int taskPos, String taskId, String state);
    }
}
