package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemDialogSelectBinding;
import com.deepsea.mua.voice.databinding.ItemGuardHeadBinding;
import com.uuzuche.lib_zxing.DisplayUtil;

/**
 * Created by JUN on 2019/10/14
 */
public class GuardHeaderAdapter extends BaseBindingAdapter<String, ItemGuardHeadBinding> {

//    private OnSelectMicUserListener onSelectMicUserListener;
//
//    public interface OnSelectMicUserListener {
//        void onSelectClick(int Level, int Number);
//    }
//
//    public void setOnSelectMicUserListener(OnSelectMicUserListener onSelectMicUserListener) {
//        this.onSelectMicUserListener = onSelectMicUserListener;
//    }

    public GuardHeaderAdapter(Context context) {
        super(context);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_guard_head;
    }


    @Override
    protected void bind(BindingViewHolder<ItemGuardHeadBinding> holder, String bean) {
        int pos = holder.getLayoutPosition();
        if (pos < getData().size() - 1) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(mContext, 25), DisplayUtil.dip2px(mContext, 33));
            layoutParams.setMargins(0, 0, DisplayUtil.dip2px(mContext, -8), 0);
            holder.binding.rlGroup.setLayoutParams(layoutParams);
        }
//        if (getData() != null) {
//            if ((getData().size() >= 2 && pos == 1) || getData().size() == 1) {
//                holder.binding.ivHeadBg.setVisibility(View.VISIBLE);
//            } else {
//                holder.binding.ivHeadBg.setVisibility(View.GONE);
//            }
//        }
        GlideUtils.roundImageWithBorder(holder.binding.ivHead, bean,1, Color.parseColor("#EF51B2"), R.drawable.ic_place, R.drawable.ic_place);
    }
}
