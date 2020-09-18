package com.deepsea.mua.voice.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.deepsea.mua.core.view.NoScrollViewPager;
import com.deepsea.mua.core.view.xtablayout.XTabLayout;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.stub.view.FixedTabLayout;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.MicManageAdapter;
import com.deepsea.mua.voice.adapter.SongManageAdapter;
import com.deepsea.mua.voice.lrc.impl.LrcRow;
import com.uuzuche.lib_zxing.DisplayUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 麦序管理
 * Created by JUN on 2019/4/23
 */
public class SongManagerDialog extends DialogFragment {
    Context mActivity;
    private OnManageListener mListener;
    private String mRoomId;

    public SongManagerDialog() {

    }


    public static SongManagerDialog newInstance(String mRoomId) {
        SongManagerDialog instance = new SongManagerDialog();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putString("mRoomId", mRoomId);
            instance.setArguments(bundle);
        } else {
            bundle.putString("mRoomId", mRoomId);
        }
        return instance;
    }

    public interface OnManageListener {
        void onLyricSetting(List<LrcRow> rows);

        void onResetLrc();

        void setLyricTips(String tips);

        void onLyricSycn(int currentPos);

        void onDownLoadSong(SongInfo songInfo);
    }

    public interface onViewPagerHeightListener {
        void onHeightChanged(int height);
    }

    public void setmListener(OnManageListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewPager!=null&&position>0) {

            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(position);
                }
            }, 100);
        }
    }

    private int position=0;

    public void setCuttentPos(int pos) {
        position = pos;
//            viewPager.setCurrentItem(pos);
//            tabLayout.getTabAt(2).select();
//        }
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
        return (int) (mActivity.getResources().getDisplayMetrics().widthPixels * 1);
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
            window.setGravity(Gravity.BOTTOM);
        }
    }

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_songs, container);
        return view;
    }

    NoScrollViewPager viewPager;
    XTabLayout tabLayout;
    SongManageAdapter mAdapter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String mRoomId = getArguments().getString("mRoomId");
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tab_layout);
        mAdapter = new SongManageAdapter(getChildFragmentManager(), mListener, mRoomId, new onViewPagerHeightListener() {
            @Override
            public void onHeightChanged(int height) {
                ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                params.height = DisplayUtil.dip2px(mActivity, height);
                viewPager.setLayoutParams(params);
            }
        });
        viewPager.setAdapter(mAdapter);
        viewPager.setNoScroll(true);
        viewPager.setOffscreenPageLimit(5);
        tabLayout.setupWithViewPager(viewPager);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition(),false);
            }

            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(XTabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            //在每个add事务前增加一个remove事务，防止连续的add
            manager.beginTransaction().remove(this).commit();
            super.show(manager, tag);
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }
}
