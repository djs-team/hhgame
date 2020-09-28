package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.OnlinesBean;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemFullServiceBinding;
import com.deepsea.mua.voice.databinding.ItemFullServiceForManyBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JUN on 2019/4/23
 */
public class SortFriendsAdapter extends BaseBindingAdapter<FriendInfoBean, ItemFullServiceForManyBinding> {

    private OnMicroListener mListener;
    private boolean isManager;
    private List<Boolean> booleanlist = new ArrayList<>();
    private OnSelectMicUserListener onSelectMicUserListener;

    public interface OnSelectMicUserListener {
        void onDisableSelectListener(int maxNum);
    }

    public void setOnSelectMicUserListener(OnSelectMicUserListener onSelectMicUserListener) {
        this.onSelectMicUserListener = onSelectMicUserListener;
    }

    @Override
    public void setNewData(List<FriendInfoBean> data) {
        super.setNewData(data);
        booleanlist.clear();
        if (data!=null) {
            for (int i = 0; i < data.size(); i++) {
                //设置默认的显示
                booleanlist.add(false);
            }
        }
    }

    @Override
    public void addData(List<FriendInfoBean> data) {
        super.addData(data);
        List<Boolean> list = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            //设置默认的显示
            list.add(false);
        }
        if (list.size() > 0) {
            booleanlist.addAll(list);
        }
    }

    public List<FriendInfoBean> getSelectData() {
        List<FriendInfoBean> data = new ArrayList<>();
        if (getData() != null) {
            for (int i = 0; i < getData().size(); i++) {
                //更改指定位置的数据
                if (booleanlist.get(i)) {
                    data.add(getData().get(i));
                }
            }
        }
        return data;
    }

    public interface OnMicroListener {
        void ItemClick(int position, FriendInfoBean bean);
    }

    public SortFriendsAdapter(Context context) {
        super(context);
    }

    private int canSelectMicroNum;

    public SortFriendsAdapter(Context context, int canSelectMicroNum) {
        super(context);
        this.canSelectMicroNum = canSelectMicroNum;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_full_service_for_many;
    }

    public void setOnMicroListener(OnMicroListener listener) {
        this.mListener = listener;
    }

    public void setManager(boolean isManager) {
        if (this.isManager != isManager) {
            this.isManager = isManager;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void bind(BindingViewHolder<ItemFullServiceForManyBinding> holder, FriendInfoBean item) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            int pos = holder.getAdapterPosition();

            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.ItemClick(pos, item);
                }
            }
        });
        ViewBindUtils.RxClicks(holder.binding.avatarIv, o -> {
            PageJumpUtils.jumpToProfile(item.getUser_id());
        });

        GlideUtils.circleImage(holder.binding.avatarIv, item.getAvatar(), R.drawable.ic_place, R.drawable.ic_place);
        ViewBindUtils.setText(holder.binding.nickTv, item.getNickname());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, String.valueOf(item.getSex()));
        ViewBindUtils.setText(holder.binding.tvAge, item.getAge() == 0 ? "" : String.valueOf(item.getAge()));
        ViewBindUtils.setText(holder.binding.tvLocation, item.getCity());
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(item.getCity()));
        holder.binding.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用集合保存当前的状态
                if (getSelectData().size() - 1 >= canSelectMicroNum) {
                    buttonView.setChecked(false);
                    if (onSelectMicUserListener != null) {
                        onSelectMicUserListener.onDisableSelectListener(canSelectMicroNum);
                    }
                } else {
                    booleanlist.set(holder.getLayoutPosition(), isChecked);
                }
            }
        });
        if (booleanlist.size() > 0) {
            holder.binding.cbSelect.setChecked(booleanlist.get(holder.getLayoutPosition()));
        }
    }

}