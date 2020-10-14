package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.CompoundButton;


import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.SexResUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.voice.databinding.ItemSortvisitorInroomFormanyBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by JUN on 2019/5/7
 */
public class VisitorInRoomAdapter extends BaseBindingAdapter<OnlineUser.UserBasis, ItemSortvisitorInroomFormanyBinding> {

    private OnSelectMicUserListener onSelectMicUserListener;

    public interface OnSelectMicUserListener {
        void onDisableSelectListener(int maxNum);
    }

    public void setOnSelectMicUserListener(OnSelectMicUserListener onSelectMicUserListener) {
        this.onSelectMicUserListener = onSelectMicUserListener;
    }

    private int maxNum;
    private List<String> uids = new ArrayList<>();

    public VisitorInRoomAdapter(Context context, int maxNum) {
        super(context);
        this.maxNum = maxNum;
        setUpMicroData();
    }

    public void setUpMicroData() {
        uids.clear();
        Map<String, String> map = InMicroMemberUtils.getInstance().getMicMap();
        for (String uid : map.values()) {
            uids.add(uid);
        }
        List<MicroOrder> microOrders = InMicroMemberUtils.getInstance().getMicroOrders();
        for (MicroOrder microOrder : microOrders) {
            if (microOrder.getUser() != null)
                uids.add(microOrder.getUser().getUserId());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.item_sortvisitor_inroom_formany;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.item_sortvisitor_inroom_formany;
    }

    private List<Boolean> booleanlist = new ArrayList<>();

    @Override
    public void setNewData(List<OnlineUser.UserBasis> data) {
        super.setNewData(data);
        booleanlist.clear();
        for (int i = 0; i < data.size(); i++) {
            //设置默认的显示
            booleanlist.add(false);
        }
    }

    @Override
    public void addData(List<OnlineUser.UserBasis> data) {
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

    public List<InviteOnmicroData> getSelectData() {
        List<InviteOnmicroData> data = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            //更改指定位置的数据
            if (booleanlist.get(i)) {
                data.add(new InviteOnmicroData(Integer.valueOf(getData().get(i).getUser().getUserId())));
            }
        }
        return data;
    }

    @Override
    protected void bind(BindingViewHolder<ItemSortvisitorInroomFormanyBinding> holder, OnlineUser.UserBasis item) {
        GlideUtils.circleImage(holder.binding.avatarIv, item.getUser().getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nickTv.setText(item.getUser().getName());
        SexResUtils.setSexImgInFindPage(holder.binding.rlSex, holder.binding.sexIv, String.valueOf(item.getUser().getSex()));
        ViewBindUtils.setText(holder.binding.tvAge, TextUtils.isEmpty(item.getUser().getAge()) ? "" : item.getUser().getAge());
        ViewBindUtils.setText(holder.binding.tvLocation, item.getUser().getCity());
        ViewBindUtils.setVisible(holder.binding.ivLocation, !TextUtils.isEmpty(item.getUser().getCity()));
        ViewBindUtils.RxClicks(holder.binding.avatarIv, o -> {
            PageJumpUtils.jumpToProfile(item.getUser().getUserId());
        });
        holder.binding.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //用集合保存当前的状态
                if (booleanlist.size() >= maxNum) {
                    buttonView.setChecked(false);
                    if (onSelectMicUserListener != null) {
                        onSelectMicUserListener.onDisableSelectListener(maxNum);
                    }
                } else
                    booleanlist.set(holder.getLayoutPosition(), isChecked);

            }
        });
        if (booleanlist.size() > 0) {
            holder.binding.cbSelect.setChecked(booleanlist.get(holder.getLayoutPosition()));
        }
        boolean hasUid = uids.contains(item.getUser().getUserId());
        ViewBindUtils.setEnable(holder.binding.cbSelect, !hasUid);
    }

}
