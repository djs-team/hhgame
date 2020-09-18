package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepsea.mua.core.view.NoScrollViewPager;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.view.FixedTabLayout;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicManageAdapter;
import com.deepsea.mua.voice.adapter.MicManageForServenAdapter;
import com.deepsea.mua.voice.utils.inter.OnManageListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class MicManagerForServenDialog extends DialogFragment {
    Context mActivity;
    private String sex;
    private OnManageListener mListener;

    public MicManagerForServenDialog() {
    }

    public static MicManagerForServenDialog newInstance(List<MicroOrder> data, int onlineVisitorPageNum, int canSelectMicroNum, int onMicroCost) {
        MicManagerForServenDialog instance = new MicManagerForServenDialog();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putSerializable("micMain", (Serializable) data);
            bundle.putInt("pageNum", onlineVisitorPageNum);
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putInt("onMicroCost", onMicroCost);

            instance.setArguments(bundle);
        } else {
            bundle.putSerializable("micMain", (Serializable) data);
            bundle.putInt("pageNum", onlineVisitorPageNum);
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putInt("onMicroCost", onMicroCost);
        }
        return instance;
    }


    public void setmListener(OnManageListener mListener) {
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
        return (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 0.9);
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
        view = inflater.inflate(R.layout.dialog_mic_manage_for_many, container);
        return view;
    }

    NoScrollViewPager viewPager;
    //    FixedTabLayout tabLayout;
    TextView applyTab;
    TextView intRoomTab;
    TextView currentActiveTab;
    TextView friendTab;
    TextView sofaMicroTab;
    TextView commonMicroTab;
    TextView lineSofaTab;
    TextView lineCommonTab;
    MicManageForServenAdapter mAdapter;
    TextView tv_title;
    private int tabType = 0;
    private LinearLayout llTabType;

    public void setApplyData(List<MicroOrder> data) {
        if (data != null && !CollectionUtils.isEmpty(data)) {
            mAdapter.setApplyData(data);
            InMicroMemberUtils.getInstance().saveMicroNum(data);
        } else {
            mAdapter.setApplyData(new ArrayList<>());
            InMicroMemberUtils.getInstance().saveMicroNum(new ArrayList<>());
        }
    }

    public void setVisitorInroomData(List<OnlineUser.UserBasis> data) {
        if (data != null && !CollectionUtils.isEmpty(data)) {
            mAdapter.setVisitorInroomData(data);
        } else {
            mAdapter.setVisitorInroomData(null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<MicroOrder> micMan = (List<MicroOrder>) getArguments().getSerializable("micMain");
        int pageNum = getArguments().getInt("pageNum");
        int canSelectMicroNum = getArguments().getInt("canSelectMicroNum");
        int onMicroCost = getArguments().getInt("onMicroCost");
        viewPager = view.findViewById(R.id.view_pager);
//        tabLayout = view.findViewById(R.id.tab_layout);
        applyTab = view.findViewById(R.id.apply_tab);
        intRoomTab = view.findViewById(R.id.in_room_tab);
        currentActiveTab = view.findViewById(R.id.current_active_tab);
        friendTab = view.findViewById(R.id.friend_tab);
        sofaMicroTab = view.findViewById(R.id.sofa_micro_tab);
        commonMicroTab = view.findViewById(R.id.common_micro_tab);
        lineSofaTab = view.findViewById(R.id.line_sofa_tab);
        lineCommonTab = view.findViewById(R.id.line_common_tab);
        llTabType = view.findViewById(R.id.ll_tab_type);
        tv_title = view.findViewById(R.id.tv_title);
        tv_title.setText("嘉宾麦位管理");
        mAdapter = new MicManageForServenAdapter(getChildFragmentManager(), mListener, micMan, pageNum, canSelectMicroNum, onMicroCost);
        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(true);
        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setViewPager(viewPager);
        applyTab.setSelected(true);
        JoinRoom joinRoom = AppConstant.getInstance().getJoinRoom();
        if (joinRoom != null && joinRoom.getRoomMode() == 8) {
            ViewBindUtils.setVisible(llTabType, true);
        } else {
            ViewBindUtils.setVisible(llTabType, false);
        }
        applyTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
                applyTab.setSelected(true);
                intRoomTab.setSelected(false);
                currentActiveTab.setSelected(false);
                friendTab.setSelected(false);
            }
        });
        intRoomTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
                applyTab.setSelected(false);
                intRoomTab.setSelected(true);
                currentActiveTab.setSelected(false);
                friendTab.setSelected(false);
            }
        });
        currentActiveTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
                applyTab.setSelected(false);
                intRoomTab.setSelected(false);
                currentActiveTab.setSelected(true);
                friendTab.setSelected(false);
            }
        });
        friendTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
                applyTab.setSelected(false);
                intRoomTab.setSelected(false);
                currentActiveTab.setSelected(false);
                friendTab.setSelected(true);
            }
        });
        sofaMicroTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabType = 1;
                com.deepsea.mua.voice.utils.AppConstant.getInstance().setTabType(tabType);
                mAdapter.setApplyTabType(tabType);
                ViewBindUtils.setVisible(lineSofaTab, true);
                lineCommonTab.setVisibility(View.INVISIBLE);
                ViewBindUtils.setTextColor(sofaMicroTab, R.color.color_FFEB1FC7);
                ViewBindUtils.setTextColor(commonMicroTab, R.color.color_FF272727);

            }
        });
        commonMicroTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tabType = 2;
                com.deepsea.mua.voice.utils.AppConstant.getInstance().setTabType(tabType);
                mAdapter.setApplyTabType(tabType);
                ViewBindUtils.setVisible(lineCommonTab, true);
                lineSofaTab.setVisibility(View.INVISIBLE);
                ViewBindUtils.setTextColor(commonMicroTab, R.color.color_FFEB1FC7);
                ViewBindUtils.setTextColor(sofaMicroTab, R.color.color_FF272727);

            }
        });

    }

}
