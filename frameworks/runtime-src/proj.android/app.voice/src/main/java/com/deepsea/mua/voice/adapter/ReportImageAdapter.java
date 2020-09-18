package com.deepsea.mua.voice.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ResUtils;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.stub.entity.ReportPicVo;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.ProfileUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemMicroSortForManyBinding;
import com.deepsea.mua.voice.databinding.ItemReportImgBinding;

import java.util.Locale;

/**
 * Created by JUN on 2019/4/23
 */
public class ReportImageAdapter extends BaseBindingAdapter<ReportPicVo, ItemReportImgBinding> {

    private OnReportListener onReportListener;

    public interface OnReportListener {
        void imgDel(int pos);
    }

    public void setMyListener(OnReportListener onReportListener) {
        this.onReportListener = onReportListener;
    }

    public ReportImageAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.item_report_img;
    }

    @Override
    protected void bind(BindingViewHolder<ItemReportImgBinding> holder, ReportPicVo item) {
        int pos = holder.getAdapterPosition();
        GlideUtils.loadImageByFile(holder.binding.ivReportImg, item.getLocalPath(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        ViewBindUtils.RxClicks(holder.binding.ivCancel, o -> {
            if (onReportListener != null) {
                onReportListener.imgDel(pos);
            }
        });

    }

    public String getImageOne() {
        String imgOne = "";
        if (getData() != null && getData().size() > 0) {
            for (int i = 0; i < getData().size(); i++) {
                if (i == 0) {
                    imgOne = getData().get(i).getOnlinePath();
                }
            }
        }
        return imgOne;
    }

    public String getImageTwo() {
        String imgtwo = "";
        if (getData() != null && getData().size() > 0) {
            for (int i = 0; i < getData().size(); i++) {
                if (i == 1) {
                    imgtwo = getData().get(i).getOnlinePath();
                }
            }
        }
        return imgtwo;
    }

    public String getImageThree() {
        String imgThree = "";
        if (getData() != null && getData().size() > 0) {
            for (int i = 0; i < getData().size(); i++) {
                if (i == 2) {
                    imgThree = getData().get(i).getOnlinePath();
                }
            }
        }
        return imgThree;
    }


}