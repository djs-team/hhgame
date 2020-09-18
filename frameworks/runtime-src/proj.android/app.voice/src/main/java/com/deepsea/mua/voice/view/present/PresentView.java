package com.deepsea.mua.voice.view.present;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.adapter.BindingViewHolder;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.PackBean;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.MultiSend;
import com.deepsea.mua.stub.entity.socket.SingleSend;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.ItemMpUserBinding;
import com.deepsea.mua.voice.databinding.ItemSendNumberBinding;
import com.deepsea.mua.voice.databinding.LayoutPresentViewBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by JUN on 2019/7/24
 * 送礼
 */
public class PresentView extends FrameLayout {

    private Context mContext;

    private LayoutPresentViewBinding mBinding;

    private PresentAdapter mPresentAdapter;
    private MpUserAdapter mUserAdapter;
    private SendNumAdapter mNumAdapter;

    private OnPresentListener mListener;

    //打赏单个用户
    private boolean isSingleSend;

    private int mSendNum = 1;

    public PresentView(@NonNull Context context) {
        this(context, null);
    }

    public PresentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public PresentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_present_view, this, true);

        initReceivers();
        initSendNumRv();
        initViewPager();
        initListener();

    }

    private void initReceivers() {
        mUserAdapter = new MpUserAdapter(mContext);
        mUserAdapter.setOnItemClickListener((view, position) -> {
            mUserAdapter.setOnClickPos(position);
        });
        if (mBinding.mpUserRv.getItemAnimator() != null) {
            mBinding.mpUserRv.getItemAnimator().setChangeDuration(0);
        }
        mBinding.mpUserRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBinding.mpUserRv.setNestedScrollingEnabled(false);
        mBinding.mpUserRv.setAdapter(mUserAdapter);
    }

    private void initSendNumRv() {
        mNumAdapter = new SendNumAdapter(mContext);
        mNumAdapter.setOnItemClickListener((view, position) -> {
            mSendNum = mNumAdapter.getItem(position).getSendNum();
            mBinding.sendNumberTv.setText("x" + mSendNum);
            mBinding.numberLayout.setVisibility(GONE);
        });
        mBinding.numberRv.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.numberRv.setNestedScrollingEnabled(false);
        mBinding.numberRv.setHasFixedSize(true);
        mBinding.numberRv.setAdapter(mNumAdapter);

        String[] array = mContext.getResources().getStringArray(R.array.sendDesc);
        int[] numArray = mContext.getResources().getIntArray(R.array.sendNum);

        List<SendNumModel> list = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            SendNumModel model = new SendNumModel();
            model.setSendNum(numArray[i]);
            model.setDesc(array[i]);
            list.add(model);
        }
        mNumAdapter.setNewData(list);
    }

    private void sendGift() {
        if (mListener != null) {
            if (mUserAdapter.getList().isEmpty()) {
                ToastUtils.showToast("请选择赠送的用户");
                return;
            }

            String id = "";
            int currentItem = mBinding.viewPager.getCurrentItem();
            if (currentItem == 0) {
                GiftView item = (GiftView) mPresentAdapter.getItem(currentItem);
                id = item.getSelected() == null ? "" : item.getSelected().getGift_id();
            } else {
                MePackView item = (MePackView) mPresentAdapter.getItem(currentItem);
                id = item.getSelected() == null ? "" : item.getSelected().getGift_id();
            }

            if (TextUtils.isEmpty(id)) {
                ToastUtils.showToast("请选择礼物");
                return;
            }

            boolean IsUseBag = mBinding.viewPager.getCurrentItem() == 1;

            if (isSingleSend) {
                MicroUser micro = mUserAdapter.getList().get(0);
                SingleSend singleSend = new SingleSend();
                singleSend.setGiftId(id);
                singleSend.setId(micro.getUser().getUserId());
                singleSend.setCount(mSendNum);
                singleSend.setUseBag(IsUseBag);
                mListener.onSingleSend(singleSend);
            } else {
                MultiSend sendModel = new MultiSend();
                sendModel.setCount(mSendNum);
                sendModel.setGiftId(id);
                sendModel.setWholeMicro(mUserAdapter.isWholeMicro());
                sendModel.setMicros(getMicrosList());
                sendModel.setUseBag(IsUseBag);
                mListener.onMultiSend(sendModel);
            }
        }
    }

    private void initViewPager() {
        mPresentAdapter = new PresentAdapter(mContext);
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mBinding.indicator.setViewPager(mPresentAdapter.getItem(position));
            }
        });
        mBinding.viewPager.setNoScroll(true);
        mBinding.viewPager.setAdapter(mPresentAdapter);
        mBinding.indicator.setViewPager(mPresentAdapter.getItem(0));
        mBinding.presentTv.setSelected(true);

        GiftView giftView = (GiftView) mPresentAdapter.getItem(0);
        MePackView packView = (MePackView) mPresentAdapter.getItem(1);
        giftView.setOnSelectedListener(data -> {
            if (data != null) {
                packView.releaseSelected();
                if (data.getGift_type().equals("4")) {
                    List<Integer> userIds = getMicroUserIds();
                    mListener.onBlueRoseSend(userIds);
                } else {
                    sendGift();
                }
            }
        });
        packView.setOnSelectedListener(data -> {
            if (data != null) {
                giftView.releaseSelected();
                sendGift();
            }
        });


        mBinding.viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    MePackView item = (MePackView) mPresentAdapter.getItem(position);
                }
            }
        });
    }

    private List<Integer> getMicroUserIds() {
        List<Integer> userIds = new ArrayList<>();
        List<MicroUser> microUserList = mUserAdapter.getList();
        for (MicroUser microUser : microUserList) {
            if (microUser.getUser() != null) {
                userIds.add(Integer.valueOf(microUser.getUser().getUserId()));
            }
        }
        return userIds;
    }

    private void initListener() {
        ViewBindUtils.RxClicks(mBinding.presentTv, o -> {
            mBinding.viewPager.setCurrentItem(0);
            mBinding.packTv.setTextColor(0xFFFFFFFF);
            mBinding.presentTv.setTextColor(0xFF818181);
            mBinding.viewCommonGiftLine.setVisibility(VISIBLE);
            mBinding.viewGuardGiftLine.setVisibility(INVISIBLE);
        });
        ViewBindUtils.RxClicks(mBinding.packTv, o -> {
            mBinding.viewPager.setCurrentItem(1);
            mBinding.packTv.setTextColor(0xFF818181);
            mBinding.presentTv.setTextColor(0xFFFFFFFF);
            mBinding.viewCommonGiftLine.setVisibility(INVISIBLE);
            mBinding.viewGuardGiftLine.setVisibility(VISIBLE);
        });
        ViewBindUtils.RxClicks(mBinding.sendNumberTv, o -> {
            ViewBindUtils.setVisible(mBinding.numberLayout, !mBinding.numberLayout.isShown());
        });
        ViewBindUtils.RxClicks(mBinding.allMapTv, o -> {
            mUserAdapter.setWholeMicro();
        });
        ViewBindUtils.RxClicks(mBinding.rechargeTv, o -> {
            if (mListener != null) {
                mListener.onRecharge();
            }
        });
        ViewBindUtils.RxClicks(mBinding.sendTv, o -> {

        });
    }

    private List<MultiSend.GiveGiftsMicros> getMicrosList() {
        List<MultiSend.GiveGiftsMicros> list = new ArrayList<>();
        MultiSend.GiveGiftsMicros micro = null;
        List<MicroUser> micros = mUserAdapter.getList();
        for (MicroUser item : micros) {
            micro = new MultiSend.GiveGiftsMicros();
            micro.setLevel(item.getType());
            micro.setNumber(item.getNumber());
            list.add(micro);
        }
        return list;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mBinding.numberLayout.isShown()) {
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                mBinding.numberLayout.setVisibility(GONE);
            }
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setOnPresentListener(OnPresentListener listener) {
        this.mListener = listener;
    }

    public void resetSendNum() {
        mSendNum = 1;
        mBinding.numberRv.post(() -> mBinding.numberRv.scrollToPosition(0));
        mBinding.sendNumberTv.setText("x1");
        mBinding.numberLayout.setVisibility(GONE);
    }

    /**
     * 设置背包数据
     *
     * @param list
     */
    public void setPackData(List<GiftBean> list) {
        ViewPager item = mPresentAdapter.getItem(1);
        if (item != null) {
            ((MePackView) item).setData(list);
            mPresentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置礼物数据
     *
     * @param list
     */
    public void setGiftData(List<GiftBean> list) {
        ViewPager item = mPresentAdapter.getItem(0);
        if (item != null) {
            ((GiftView) item).setData(list);
            mPresentAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置全麦用户数据
     *
     * @param datas
     */
    public void setReceiverData(List<MicroUser> datas) {
        isSingleSend = false;
        mBinding.allMapTv.setVisibility(View.VISIBLE);
//        mBinding.tvSendGiftDesc.setVisibility(GONE);
        mUserAdapter.setNewData(datas);
    }

    /**
     * 设置账户余额
     *
     * @param balance
     */
    public void setBalance(String balance) {
        mBinding.amountTv.setText(FormatUtils.subZeroAndDot(balance));
    }

    /**
     * 设置打赏用户信息
     *
     * @param user
     */
    public void setSingleData(MicroUser user, boolean isAddFriend) {
        isSingleSend = !user.isIsOnMicro();
        mUserAdapter.setNewData(Collections.singletonList(user));
        mUserAdapter.setWholeMicro();
        mBinding.allMapTv.setVisibility(View.GONE);
        if (isAddFriend) {
            ViewBindUtils.setText(mBinding.tvSendGiftDesc, "相亲房内送礼物，无需确认，自动加好友");
        }
    }

    private static class PresentAdapter extends PagerAdapter {

        private SparseArray<ViewPager> views;
        private String[] PRESENT_TITLES = {"礼物", "背包"};

        private Context mContext;

        public PresentAdapter(Context context) {
            this.mContext = context;
            views = new SparseArray<>();
        }

        @Override
        public int getCount() {
            return PRESENT_TITLES.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        public ViewPager getItem(int pos) {
            ViewPager itemView = null;
            if (pos < views.size()) {
                itemView = views.get(pos);
            }

            if (itemView == null) {
                if (pos == 0) {
                    itemView = new GiftView(mContext);
                }
                if (pos == 1) {
                    itemView = new MePackView(mContext);
                }
                views.put(pos, itemView);
            }

            return itemView;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            ViewPager itemView = getItem(position);
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PRESENT_TITLES[position];
        }
    }

    private static class MpUserAdapter extends BaseBindingAdapter<MicroUser, ItemMpUserBinding> {

        private List<MicroUser> mList;

        public MpUserAdapter(Context context) {
            super(context);
            mList = new ArrayList<>();
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_mp_user;
        }

        public boolean isWholeMicro() {
            return getItemCount() == mList.size();
        }

        public void setWholeMicro() {
            mList.clear();
            if (getData() != null) {
                mList.addAll(getData());
            }
            notifyItemRangeChanged(0, getItemCount(), 0);
        }

        @Override
        public void setNewData(List<MicroUser> data) {
            mList.clear();
            super.setNewData(data);
        }

        public List<MicroUser> getList() {
            return mList;
        }

        public void setOnClickPos(int position) {
            MicroUser item = getItem(position);
            if (mList.contains(item)) {
                mList.remove(item);
            } else {
                mList.add(item);
            }
            notifyItemChanged(position);
        }

        @Override
        public void onBindViewHolder(@NonNull BindingViewHolder<ItemMpUserBinding> holder, int position, @NonNull List<Object> payloads) {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
                return;
            }

            MicroUser item = getItem(position);
            holder.setVisible(holder.binding.mpSelectIv, mList.contains(item));
            holder.binding.mpUserIv.setSelected(mList.contains(item));
        }

        @Override
        protected void bind(BindingViewHolder<ItemMpUserBinding> holder, MicroUser item) {
            GlideUtils.circleImage(holder.binding.mpUserIv, item.getUser().getHeadImageUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
            holder.setVisible(holder.binding.mpSelectIv, mList.contains(item));
            holder.binding.mpUserIv.setSelected(mList.contains(item));
        }
    }

    private static class SendNumAdapter extends BaseBindingAdapter<SendNumModel, ItemSendNumberBinding> {

        public SendNumAdapter(Context context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.item_send_number;
        }

        @Override
        protected void bind(BindingViewHolder<ItemSendNumberBinding> holder, SendNumModel item) {
            holder.binding.numberTv.setText(String.valueOf(item.getSendNum()));
            holder.binding.descTv.setText(item.getDesc());
        }
    }
}
