package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemMicroSortBinding;
import com.deepsea.mua.voice.databinding.ItemMicroSortForManyBinding;

import java.util.Locale;

/**
 * Created by JUN on 2019/4/23
 */
public class MicroSortAdapter extends BaseBindingAdapter<MicroOrder, ItemMicroSortForManyBinding> {

    private OnMicroListener mListener;
    private boolean isManager;
    private String sex;

    public void setSex(String sex) {
        this.sex = sex;
    }

    public interface OnMicroListener {
        void onTopMicro(String uid);

        void onOnWheat(String uid);

        void onRemove(String uid);
    }

    public MicroSortAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_micro_sort_for_many;
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
    protected void bind(BindingViewHolder<ItemMicroSortForManyBinding> holder, MicroOrder item) {
        int pos = holder.getAdapterPosition();
        if (!isManager) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.binding.avatarIv.getLayoutParams();
            lp.width = lp.height = ResUtils.dp2px(mContext, 36);
            holder.binding.avatarIv.setLayoutParams(lp);
        }

        holder.binding.setIsManager(isManager);
        holder.binding.sortTv.setText(sortPos(pos));
        WsUser user = item.getUser();
        GlideUtils.circleImage(holder.binding.avatarIv, user.getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        holder.binding.nickTv.setText(user.getName());
        holder.binding.infoTv.setText(ProfileUtils.getProfile(user.getAge(), user.getCity(), user.getStature()));

        holder.setOnClickListener(holder.binding.topMicroRl, o -> {
            if (mListener != null) {
                mListener.onTopMicro(user.getUserId());
            }
        });
        holder.setOnClickListener(holder.binding.onWheatMicroRl, o -> {
            if (mListener != null) {
                mListener.onOnWheat(user.getUserId());
            }
        });
        holder.setOnClickListener(holder.binding.removeMicroRl, o -> {
            if (mListener != null) {
                mListener.onRemove(user.getUserId());
                remove(pos);
                notifyItemRemoved(pos);
            }
        });
        holder.setOnClickListener(holder.binding.avatarIv, o -> {
            PageJumpUtils.jumpToProfile(item.getUser().getUserId());
        });
        boolean hasMember = InMicroMemberUtils.getInstance().judgeMicroHasMember(sex);
        if (hasMember) {
            holder.binding.topMicroRl.setEnabled(false);
//            holder.binding.onWheatMicroTv.setBackgroundColor(mContext.getResources().getColor(R.color.DEDEDE));
        } else {
            holder.binding.topMicroRl.setEnabled(true);
//            holder.binding.onWheatMicroTv.setBackgroundColor(mContext.getResources().getColor(R.color.primary_yellow));
        }
    }

    public String sortPos(int index) {
        if (index < 9) {
            return String.format(Locale.CHINA, "0%d", index + 1);
        }
        return String.valueOf(index + 1);
    }
}