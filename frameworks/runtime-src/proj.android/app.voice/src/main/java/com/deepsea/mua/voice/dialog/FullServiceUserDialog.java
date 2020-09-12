package com.deepsea.mua.voice.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.deepsea.mua.core.dialog.BaseDialog;
import com.deepsea.mua.core.dialog.BaseDialogFragment;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.view.NoScrollViewPager;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.view.FixedTabLayout;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.activity.RoomActivity;
import com.deepsea.mua.voice.adapter.FullServiceSortAdapter;
import com.deepsea.mua.voice.adapter.FullServiceUsersAdapter;
import com.deepsea.mua.voice.databinding.DialogFullServiceUserBinding;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class FullServiceUserDialog extends DialogFragment {
    Context mActivity;
    private OnMicroListener mListener;
    private int hongId;
    private String roomId;

    public FullServiceUserDialog() {

    }

    public static FullServiceUserDialog newInstance(int hongId, String roomId) {
        FullServiceUserDialog instance = new FullServiceUserDialog();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("roomId", roomId);
            bundle.putInt("hongId", hongId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("roomId", roomId);
            bundle.putInt("hongId", hongId);
        }
        return instance;
    }

    public interface OnMicroListener {
        //邀请上麦
        void onInviteClick(String uid);

        //送礼/打赏
        void sendGift(String uid);

        //加好友
        void addFriend(String uid);
    }

    public void setmListener(OnMicroListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        mActivity = context;
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.FullscreenDialog);
    }

    private int getWidth() {
        return (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.8);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = getWidth();
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
            window.setDimAmount(0.5f);
            window.setGravity(Gravity.CENTER);
        }
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_full_service_user, container);
        return view;
    }

    NoScrollViewPager viewPager;
    FixedTabLayout tabLayout;
    TextView boyTab;
    TextView girlTab;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        roomId = getArguments().getString("roomId");
        hongId = getArguments().getInt("hongId");
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        boyTab = view.findViewById(R.id.boy_tab);
        girlTab = view.findViewById(R.id.girl_tab);
        boyTab.setOnClickListener(v -> {
            viewPager.setCurrentItem(0);
            boyTab.setSelected(true);
            girlTab.setSelected(false);
        });
        girlTab.setOnClickListener(v -> {
            viewPager.setCurrentItem(1);
            boyTab.setSelected(false);
            girlTab.setSelected(true);
        });
        FullServiceSortAdapter mAdapter = new FullServiceSortAdapter(getChildFragmentManager(), mListener, hongId, roomId);
        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(true);
//        tabLayout.setViewPager(viewPager);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        boyTab.setSelected(true);
    }

}
