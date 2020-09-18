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
import android.widget.TextView;

import com.deepsea.mua.core.view.NoScrollViewPager;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.MicroSort;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.view.FixedTabLayout;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.FullServiceSortAdapter;
import com.deepsea.mua.voice.adapter.MicManageAdapter;
import com.deepsea.mua.voice.utils.inter.OnManageListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class MicManagerDialog extends DialogFragment {
    Context mActivity;
    private String sex;
    private OnManageListener mListener;

    public MicManagerDialog() {

    }


    public static MicManagerDialog newInstance(List<MicroOrder> data, String sex, int onlineVisitorPageNum, int canSelectMicroNum,int onMicroCost) {
        MicManagerDialog instance = new MicManagerDialog();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putSerializable("micMain", (Serializable) data);
            bundle.putString("sex", sex);
            bundle.putInt("pageNum", onlineVisitorPageNum);
            bundle.putInt("canSelectMicroNum", canSelectMicroNum);
            bundle.putInt("onMicroCost", onMicroCost);

            instance.setArguments(bundle);
        } else {
            bundle.putSerializable("micMain", (Serializable) data);
            bundle.putString("sex", sex);
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
        view = inflater.inflate(R.layout.dialog_mic_manage, container);
        return view;
    }

    NoScrollViewPager viewPager;
//    FixedTabLayout tabLayout;
    //    TextView boyTab;
//    TextView girlTab;
    MicManageAdapter mAdapter;
    TextView tv_title;
    TextView applyTab;
    TextView intRoomTab;
    TextView currentActiveTab;
    TextView friendTab;

    public void setApplyData(List<List<MicroOrder>> data, int selectMicManagerSex) {
        if (data != null && !CollectionUtils.isEmpty(data)) {
            mAdapter.setApplyData(data.get(selectMicManagerSex - 1));
            InMicroMemberUtils.getInstance().saveMicroNum(data.get(selectMicManagerSex - 1));
        } else {
            mAdapter.setApplyData(new ArrayList<>());
            InMicroMemberUtils.getInstance().saveMicroNum(new ArrayList<>());
        }



    }

    public void setVisitorInroomData(List<OnlineUser.UserBasis> data) {
        if (data!=null&&!CollectionUtils.isEmpty(data)) {
            mAdapter.setVisitorInroomData(data);
        } else {
            mAdapter.setVisitorInroomData(null);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<MicroOrder> micMan = (List<MicroOrder>) getArguments().getSerializable("micMain");
        String sex = getArguments().getString("sex");
        int pageNum = getArguments().getInt("pageNum");
        int onMicroCost = getArguments().getInt("onMicroCost");
        int canSelectMicroNum = getArguments().getInt("canSelectMicroNum");
        viewPager = view.findViewById(R.id.view_pager);
//        tabLayout = view.findViewById(R.id.tab_layout);
        tv_title = view.findViewById(R.id.tv_title);
        applyTab = view.findViewById(R.id.apply_tab);
        intRoomTab = view.findViewById(R.id.in_room_tab);
        currentActiveTab = view.findViewById(R.id.current_active_tab);
        friendTab = view.findViewById(R.id.friend_tab);
        if (sex.equals("1")) {
            tv_title.setText("男嘉宾麦位管理");
        } else {
            tv_title.setText("女嘉宾麦位管理");
        }
//        boyTab = view.findViewById(R.id.boy_tab);
//        girlTab = view.findViewById(R.id.girl_tab);
//        boyTab.setOnClickListener(v -> {
//            viewPager.setCurrentItem(0);
//            boyTab.setSelected(true);
//            girlTab.setSelected(false);
//        });
//        girlTab.setOnClickListener(v -> {
//            viewPager.setCurrentItem(1);
//            boyTab.setSelected(false);
//            girlTab.setSelected(true);
//        });

        mAdapter = new MicManageAdapter(getChildFragmentManager(), mListener, micMan, sex, pageNum, canSelectMicroNum, onMicroCost);
        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(true);
        viewPager.setOffscreenPageLimit(3);
//        tabLayout.setViewPager(viewPager);
//        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dismiss();
//            }
//        });
//        boyTab.setSelected(true);
        applyTab.setSelected(true);
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
    }

}
