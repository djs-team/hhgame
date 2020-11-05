package com.deepsea.mua.voice.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.PermissionChecker;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.cxz.networklib.NetworkManager;
import com.cxz.networklib.annotation.Network;
import com.cxz.networklib.type.NetType;
import com.deepsea.mua.core.alipay.Alipay;
import com.deepsea.mua.core.alipay.PayResult;
import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.utils.NetWorkUtils;
import com.deepsea.mua.core.utils.ToastUtils;
import com.deepsea.mua.core.wxpay.WxCons;
import com.deepsea.mua.core.wxpay.WxPay;
import com.deepsea.mua.core.wxpay.WxpayBroadcast;
import com.deepsea.mua.faceliveness.helper.AuditHelper;
import com.deepsea.mua.faceliveness.listener.RPAuditListener;
import com.deepsea.mua.stub.adapter.BaseBindingAdapter;
import com.deepsea.mua.stub.apiaddress.AddressCenter;
import com.deepsea.mua.stub.apiaddress.BaseAddress;
import com.deepsea.mua.stub.app.ActivityCache;
import com.deepsea.mua.stub.base.BaseActivity;
import com.deepsea.mua.stub.base.BaseObserver;
import com.deepsea.mua.stub.base.ProgressObserver;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.client.soket.SocketCons;
import com.deepsea.mua.stub.controller.IRoomController;
import com.deepsea.mua.stub.controller.MicroSortHandler;
import com.deepsea.mua.stub.controller.RoomController;
import com.deepsea.mua.stub.controller.RoomJoinController;
import com.deepsea.mua.stub.controller.RoomMsgHandler;
import com.deepsea.mua.stub.data.BaseApiResult;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.dialog.AAlertDialog;
import com.deepsea.mua.stub.dialog.AuthenticationAlertDialog;
import com.deepsea.mua.stub.dialog.AutoHideLoading;
import com.deepsea.mua.stub.dialog.GuardRenewDialog;
import com.deepsea.mua.stub.dialog.RechargeFirstWelfareDialog;
import com.deepsea.mua.stub.entity.AuditBean;
import com.deepsea.mua.stub.entity.EmojiBean;
import com.deepsea.mua.stub.entity.FirstRechargeVo;
import com.deepsea.mua.stub.entity.FriendInfoBean;
import com.deepsea.mua.stub.entity.FriendInfoListBean;
import com.deepsea.mua.stub.entity.GiftBean;
import com.deepsea.mua.stub.entity.InviteOnmicroData;
import com.deepsea.mua.stub.entity.LocalVoiceReverbPresetVo;
import com.deepsea.mua.stub.entity.MicroUserBean;
import com.deepsea.mua.stub.entity.RecommendRoomResult;
import com.deepsea.mua.stub.entity.RedPacket;
import com.deepsea.mua.stub.entity.RenewInitVo;
import com.deepsea.mua.stub.entity.ShareBeanItem;
import com.deepsea.mua.stub.entity.UserRedPacket;
import com.deepsea.mua.stub.entity.VoiceBanner;
import com.deepsea.mua.stub.entity.WxOrder;
import com.deepsea.mua.stub.entity.model.RoomMsgBean;
import com.deepsea.mua.stub.entity.socket.CountDown;
import com.deepsea.mua.stub.entity.socket.EmotionBean;
import com.deepsea.mua.stub.entity.socket.receive.JoinUser;
import com.deepsea.mua.stub.entity.socket.MicroOrder;
import com.deepsea.mua.stub.entity.socket.MicroSort;
import com.deepsea.mua.stub.entity.socket.MicroUser;
import com.deepsea.mua.stub.entity.socket.MpInvited;
import com.deepsea.mua.stub.entity.socket.MultiSend;
import com.deepsea.mua.stub.entity.socket.OnlineUser;
import com.deepsea.mua.stub.entity.socket.ReceiveMessage;
import com.deepsea.mua.stub.entity.socket.receive.ReceivePresent;
import com.deepsea.mua.stub.entity.socket.RoomData;
import com.deepsea.mua.stub.entity.socket.SingleSend;
import com.deepsea.mua.stub.entity.socket.WsUser;
import com.deepsea.mua.stub.entity.socket.receive.BaseMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.DownMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.ShowGuardAnimationToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.entity.socket.receive.SyncMicroRose;
import com.deepsea.mua.stub.entity.socket.receive.UpMicroMsg;
import com.deepsea.mua.stub.entity.socket.receive.UpdateFinishTaskToClientParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateGuardSignToClientParam;
import com.deepsea.mua.stub.entity.socket.send.JoinRoom;
import com.deepsea.mua.stub.model.RoomModel;
import com.deepsea.mua.stub.network.download.DownloadListener;
import com.deepsea.mua.stub.network.download.DownloadUtils;
import com.deepsea.mua.stub.utils.AppConstant;
import com.deepsea.mua.stub.utils.ArouterConst;
import com.deepsea.mua.stub.utils.ArouterUtils;
import com.deepsea.mua.stub.utils.CacheUtils;
import com.deepsea.mua.stub.utils.CollectionUtils;
import com.deepsea.mua.stub.utils.Constant;
import com.deepsea.mua.stub.utils.FileUtils;
import com.deepsea.mua.stub.utils.FormatUtils;
import com.deepsea.mua.stub.utils.FriendsUtils;
import com.deepsea.mua.stub.utils.GridItemDecoration;
import com.deepsea.mua.stub.utils.MobEventUtils;
import com.deepsea.mua.stub.utils.PageJumpUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.eventbus.DownloadEvent;
import com.deepsea.mua.stub.utils.eventbus.ExitRoomEvent;
import com.deepsea.mua.stub.utils.eventbus.InviteInRoomEvent;
import com.deepsea.mua.stub.utils.eventbus.MarqueeEvent;
import com.deepsea.mua.stub.utils.eventbus.SyncSongProgressEvent;
import com.deepsea.mua.stub.utils.eventbus.UpdateApplyMicEvent;
import com.deepsea.mua.stub.utils.eventbus.UpdateInRoomMemberEvent;
import com.deepsea.mua.stub.utils.notch.HwNotchUtils;
import com.deepsea.mua.stub.view.RedPacketView;
import com.deepsea.mua.voice.MyNotifyService;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.adapter.IMicroEvent;
import com.deepsea.mua.voice.adapter.MicroRecommendAdapter;
import com.deepsea.mua.voice.adapter.MicroSortAdapter;
import com.deepsea.mua.voice.adapter.RoomAudioAdapter;
import com.deepsea.mua.voice.adapter.RoomBannerAdapter;
import com.deepsea.mua.voice.adapter.RoomMpAdapter;
import com.deepsea.mua.voice.adapter.RoomMsgAdapter;
import com.deepsea.mua.voice.adapter.RoomRankingAdapter;
import com.deepsea.mua.voice.databinding.ActivityVoiceRoomBinding;
import com.deepsea.mua.voice.dialog.ApplyMicroDialog;
import com.deepsea.mua.voice.dialog.EmojiDialog;
import com.deepsea.mua.voice.dialog.ForbiddenUserDialog;
import com.deepsea.mua.voice.dialog.GuardBayWindowDialog;
import com.deepsea.mua.voice.dialog.GuardGroupDialog;
import com.deepsea.mua.voice.dialog.GuardSuccessDialog;
import com.deepsea.mua.voice.dialog.InputTextDialog;
import com.deepsea.mua.voice.dialog.MicManagerDialog;
import com.deepsea.mua.voice.dialog.MicManagerForServenDialog;
import com.deepsea.mua.voice.dialog.MicroTimerDialog;
import com.deepsea.mua.voice.dialog.NotifyHelpDialog;
import com.deepsea.mua.voice.dialog.RedpackageComingDialog;
import com.deepsea.mua.voice.dialog.RedpackageResultDialog;
import com.deepsea.mua.voice.dialog.RedpackageRuleDialog;
import com.deepsea.mua.voice.dialog.RoomGiftDialog;
import com.deepsea.mua.voice.dialog.RoomOwnerDialog;
import com.deepsea.mua.voice.dialog.RoomPlayDialog;
import com.deepsea.mua.voice.dialog.ShareResultDialog;
import com.deepsea.mua.voice.dialog.SongAlertDialog;
import com.deepsea.mua.voice.dialog.SongChooseAlertDialog;
import com.deepsea.mua.voice.dialog.SongGuideDialog;
import com.deepsea.mua.voice.dialog.SongLrcDialog;
import com.deepsea.mua.voice.dialog.SongManagerDialog;
import com.deepsea.mua.voice.dialog.SongSingingAlertDialog;
import com.deepsea.mua.voice.dialog.SortCheckDialog;
import com.deepsea.mua.voice.dialog.SortManageDialog;
import com.deepsea.mua.voice.dialog.UserAvatarDialog;
import com.deepsea.mua.voice.dialog.UserFansDialog;
import com.deepsea.mua.stub.utils.ForbiddenStateUtils;
import com.deepsea.mua.stub.utils.InMicroMemberUtils;
import com.deepsea.mua.voice.lrc.ILrcBuilder;
import com.deepsea.mua.voice.lrc.ILrcView;
import com.deepsea.mua.voice.lrc.impl.DefaultLrcBuilder;
import com.deepsea.mua.voice.lrc.impl.LrcRow;
import com.deepsea.mua.voice.utils.HeartControl;
import com.deepsea.mua.voice.utils.MatchMakerUtils;
import com.deepsea.mua.stub.utils.SongStateUtils;
import com.deepsea.mua.voice.utils.SvgaUtils;
import com.deepsea.mua.voice.utils.inter.OnManageListener;
import com.deepsea.mua.voice.view.MicroFaceView;
import com.deepsea.mua.voice.view.TextureVideoViewOutlineProvider;
import com.deepsea.mua.voice.view.present.OnPresentListener;
import com.deepsea.mua.voice.viewmodel.RoomViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hyphenate.chat.EMClient;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMMin;
import com.uuzuche.lib_zxing.DisplayUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import io.agora.rtc.Constants;
import io.reactivex.disposables.Disposable;

import static io.agora.rtc.Constants.AUDIO_RECORDING_QUALITY_HIGH;
import static io.agora.rtc.Constants.AUDIO_REVERB_OFF;
import static io.agora.rtc.Constants.AUDIO_REVERB_ROOM_SIZE;

/**
 * Created by JUN on 2019/3/25
 */
@Route(path = ArouterConst.PAGE_ROOM)
public class RoomActivity extends BaseActivity<ActivityVoiceRoomBinding>
        implements SvgaUtils.SvgaParserCallback, IRoomController.IRoomView {
    private RoomMsgAdapter mMsgAdapter;
    private RoomAudioAdapter mMpAdapter;
    private RoomRankingAdapter mRankingAdapter;
    @Inject
    ViewModelProvider.Factory mFactory;
    private RoomViewModel mViewModel;
    private SvgaUtils mSvgaUtils;
    private InputTextDialog mInputDialog;
    private RoomOwnerDialog mOwnerDialog;
    private UserAvatarDialog mAvatarDialog;
    private AAlertDialog mAlertDlg;
    private MicroTimerDialog mTimerDialog;
    private RoomGiftDialog mGiftDialog;
    private EmojiDialog mEmojiDialog;
    private SortManageDialog mSortManageDialog;
    private SortCheckDialog mCheckDialog;
    private MicManagerDialog micManagerDialog;
    private MicManagerForServenDialog micManagerForServenDialog;
    private String mRoomId;
    private MicroSortHandler mSortHandler;
    private User mUser;
    private RoomModel mRoomModel;
    private RoomData mRoomData;
    private boolean isEmotionSending;
    private boolean isResumed;
    private final int msg_send_lyric = 1;
    private final int msg_download_lrc = 1003;
    private final int msg_start_play_song = 1004;
    private final int msg_start_play_song_with_lrc = 1005;
    private final int msg_lyric_reset = 1006;
    private final int msg_lyric_show = 1007;
    private final int msg_lyric_receive = 1008;
    private final int msg_start_download_song = 1009;
    private final int msg_download_song_success = 1010;
    private final int msg_download_song_fails = 1011;
    private final int msg_download_lrc_andshow = 1012;
    String songName;
    RecyclerView vicePhoneMusicRv;
    RecyclerView vicePhoneRv;
    RecyclerView rv_second_music_view;
    RecyclerView rv_second_view;

    MicroFaceView mainFaceView;
    MicroFaceView mainFaceMusicView;
    private SongLrcDialog lrcDialog;
    private JoinRoom mJoinRoom;
    @Inject
    RoomJoinController mRoomJump;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msg_send_lyric:
                    if (mBinding.leadsingerLrcView.getVisibility() == View.INVISIBLE) {
                        mHandler.sendEmptyMessage(msg_lyric_show);
                    }
                    String m = (String) msg.obj;
                    mViewModel.notifySongLyricToClientParam(m);
                    int pos = AgoraClient.create().rtcEngine().getAudioMixingCurrentPosition();
                    int max = AgoraClient.create().rtcEngine().getAudioMixingDuration();
                    Log.d("msg_send_lyric", pos + ":" + max);
                    EventBus.getDefault().post(new SyncSongProgressEvent(pos, max));
                    if (lrcDialog != null) {
                        lrcDialog.setLrcProcess(pos, max);
                    }
                    break;
                case msg_download_lrc:
                    SongInfo songInfo = (SongInfo) msg.obj;
                    downLoadSongLrc(songInfo);
                    break;
                case msg_start_play_song:
                    songName = (String) msg.obj;
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(msg_download_song_success);

                    }
                    break;
                case msg_start_play_song_with_lrc:
                    songName = (String) msg.obj;
                    List<LrcRow> rows = setSongLric(songName);
                    if (rows != null && rows.size() > 0) {
                        setLyricSetting(rows);
                    }
                    if (mHandler != null) {
                        mHandler.sendEmptyMessage(msg_download_song_success);
                    }
                    starPlaySong(songName);
                    break;
                case msg_lyric_reset:
                    try {
                        mBinding.leadsingerLrcView.setVisibility(View.VISIBLE);
                        mBinding.leadsingerLrcView.setText("");
                        mBinding.lrcView.setVisibility(View.INVISIBLE);
                        mBinding.lrcView.setLoadingTipText("");
                        mBinding.lrcView.setLrc(null);
                        mBinding.audienceLrcView.setVisibility(View.INVISIBLE);
                        mBinding.audienceLrcView.setText("");
                    } catch (Exception e) {
                        Log.d("Exception", e.toString());
                    }
                    break;
                case msg_lyric_show:
                    mBinding.leadsingerLrcView.setVisibility(View.INVISIBLE);
                    mBinding.leadsingerLrcView.setText("");
                    mBinding.lrcView.setVisibility(View.INVISIBLE);
                    mBinding.lrcView.setLoadingTipText("");
                    mBinding.lrcView.setLrc(null);
                    mBinding.audienceLrcView.setVisibility(View.VISIBLE);
                    mBinding.audienceLrcView.setText("");
                    break;
                case msg_lyric_receive:
                    LrcRow lrcRow = (LrcRow) msg.obj;
                    mLrcView.setLrc(null);
                    if (lrcDialog != null) {
                        lrcDialog.setLrcSetting(null);
                    }
                    mAudienceLrcView.setText(lrcRow.getContent());
                    break;
                case msg_start_download_song:
                    mViewModel.startDownloadSongParam();
                    SongStateUtils.getSingleton2().setDownloadState(3);
                    EventBus.getDefault().post(new DownloadEvent(3));
                    break;
                case msg_download_song_success:
                    mViewModel.downloadSongSuccessParam();
                    SongStateUtils.getSingleton2().setDownloadState(4);
                    EventBus.getDefault().post(new DownloadEvent(4));
                    break;
                case msg_download_song_fails:
                    SongStateUtils.getSingleton2().setDownloadState(5);
                    EventBus.getDefault().post(new DownloadEvent(5));
                    break;
                case msg_download_lrc_andshow:
                    showLrcDialog();
                    break;
            }
        }
    };
    @Autowired(name = "isInvite")
    boolean isInvite;//是否收到邀请进入房间
    @Autowired(name = "inviteMicroId")
    int inviteMicroId;//邀请记录表的id
    private int hongId;//红娘id
    @Autowired(name = "free")
    int free;//是否免费
    @Autowired(name = "cost")
    int cost;//邀请记录表的id
    IWXAPI iwxapi;
    private int microType = -1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_voice_room;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NetworkManager.getDefault().registerObserver(this);
        EventBus.getDefault().register(this);
        SongStateUtils.getSingleton2().initReverbPresetList();
        SongStateUtils.getSingleton2().initReverbPresetList();
        Intent intent = new Intent(mContext, MyNotifyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MarqueeEvent event) {
        if (!TextUtils.isEmpty(event.content)) {
            mBinding.rlMarquee.setVisibility(View.VISIBLE);
            mBinding.tvMarquee.setMarqueeNum(-1);
            mBinding.tvMarquee.setText(event.content);
        } else {
            mBinding.rlMarquee.setVisibility(View.GONE);
            mBinding.tvMarquee.setMarqueeNum(0);
            mBinding.tvMarquee.setText("暂无公告");
        }
    }

    /**
     * 自定义LrcView，用来展示歌词
     */
    ILrcView mLrcView;
    TextView mAudienceLrcView;
    /**
     * 更新歌词的频率，每100ms更新一次
     */
    private int mPlayerTimerDuration = 1000;
    /**
     * 更新歌词的定时器
     */
    private Timer mTimer;
    /**
     * 更新歌词的定时任务
     */
    private TimerTask mTask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        SharedPrefrencesUtil.saveData(mContext, "inroom", "inroom", true);
    }

    @Override
    protected void initView() {
        Log.d("roomCh", "initView");
        vicePhoneMusicRv = mBinding.vicePhoneMusicRv;
        vicePhoneRv = mBinding.vicePhoneRv;
        rv_second_music_view = mBinding.rvSecondMusicView;
        rv_second_view = mBinding.rvSecondView;
        mainFaceView = mBinding.mainFaceView;
        mainFaceMusicView = mBinding.mainFaceMusicView;
        mLrcView = (ILrcView) findViewById(R.id.lrcView);


        mAudienceLrcView = (TextView) findViewById(R.id.audience_lrcView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mViewModel = ViewModelProviders.of(this, mFactory).get(RoomViewModel.class);
        initWsLiveData();

        mBinding.notchView.setVisibility(HwNotchUtils.hasNotchInScreen(mContext) ? View.VISIBLE : View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider viewOutlineProvider = new TextureVideoViewOutlineProvider(DisplayUtil.dip2px(mContext, 8));

            mainFaceView.setOutlineProvider(viewOutlineProvider);
            mainFaceView.setClipToOutline(true);

            mBinding.mainSecondOneView.setOutlineProvider(viewOutlineProvider);
            mBinding.mainSecondOneView.setClipToOutline(true);

            mBinding.mainSecondTwoMusicView.setOutlineProvider(viewOutlineProvider);
            mBinding.mainSecondTwoMusicView.setClipToOutline(true);
        }
        RoomController.getInstance().setRoomView(this);
        mRoomModel = RoomController.getInstance().getRoomModel();
        mJoinRoom = AppConstant.getInstance().getJoinRoom();

        if (mRoomModel != null) {
            mRoomData = mRoomModel.getRoomData();
            com.deepsea.mua.voice.utils.AppConstant.getInstance().setMicroCost(mRoomData.getOnMicroCost());
            mViewModel.setRoomModel(mRoomModel);
            mSortHandler = RoomController.getInstance().getMicroSortHandler();
            mUser = UserUtils.getUser();
            mRoomId = mRoomData.getRoomData().getId();
            SharedPrefrencesUtil.saveData(mContext, "mRoomId", "mRoomId", mRoomId);
            mBinding.rankingRv.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.performClick();
                }
                return false;
            });
            mSvgaUtils = new SvgaUtils(mContext, mBinding.svgaIv);
            mSvgaUtils.setSvgaParserCallback(this);
            initTitle();
            initCommonMp();
            initMicro();
            initRank();
            initMpMsg();
            initBottom();
            initMicroSort();
            initGoldEgg();
            getBanners();
            initRole();
            initRecommend();
        }
        getFriendList(false);
//        HeartControl.getInstance(mContext).startHeartBeatObservable(mViewModel, new HeartControl.CallBack() {
//            @Override
//            public void onResult() {
//                onMicroDownExitRoom();
//            }
//        });
        inviteUpMicro(inviteMicroId);

        iwxapi = WXAPIFactory.createWXAPI(this, WxCons.APP_ID_WX);
        String marqueeData = AppConstant.getInstance().getMarquee();
        if (!TextUtils.isEmpty(marqueeData) && marqueeData.length() > 0) {
            mBinding.tvMarquee.setMarqueeNum(-1);
            mBinding.tvMarquee.setText(marqueeData);
        } else {
            mBinding.tvMarquee.setMarqueeNum(0);
            mBinding.tvMarquee.setText("暂无公告");
        }
//        if (mHandler != null) {
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
        mViewModel.getPlaySongParam();

//                }
//            }, 2000);
//        }
        if (!isOnReconnected) {
            pushRoomMsg();
        }
        isOnReconnected = false;
    }


    private void initRecommend() {
        mBinding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ViewBindUtils.RxClicks(mBinding.tvRecommendClose, o -> {
            mBinding.drawer.closeDrawer(Gravity.RIGHT);
        });
        mBinding.rvRecommend.setLayoutManager(new LinearLayoutManager(mContext));
        MicroRecommendAdapter recommendAdapter = new MicroRecommendAdapter(mContext, mJoinRoom.getRoomMode());
        recommendAdapter.setOnItemClickListener(new BaseBindingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (!isChangeView) {
                    RoomData.MicroInfosBean mainM = mRoomModel.getHostMicro();
                    mainM.setUser(null);
                    mBinding.mainFaceView.setMicroData(mainM);
                } else {
                    mainMpInfo.setUser(null);
                    mainFaceMusicView.setMicroData(mainMpInfo);
                }
                for (int i = 0; i < mMpAdapter.getData().size(); i++) {
                    mMpAdapter.getData().get(i).setUser(null);
                }
                mMpAdapter.notifyDataSetChanged();

                RoomController.getInstance().releaseHyphenateAndAgora();

                RecommendRoomResult.ListBean bean = recommendAdapter.getData().get(position);
                JoinRoom joinRoom = new JoinRoom();
                joinRoom.setMsgId(SocketCons.JOIN_ROOM);
                joinRoom.setRoomId(bean.getId());
                RoomController.getInstance().sendRoomMsg(JsonConverter.toJson(joinRoom));
            }
        });
        mBinding.rvRecommend.setAdapter(recommendAdapter);
        mBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mViewModel.refreshRecommen(mRoomId).observe(RoomActivity.this, new BaseObserver<RecommendRoomResult>() {
                    @Override
                    public void onSuccess(RecommendRoomResult result) {
                        RecommendRoomResult.PageInfoBean pageInfo = result.getPageInfo();
                        recommendAdapter.setNewData(result.getList());
                        mBinding.refreshLayout.finishRefresh();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());

                    }

                    @Override
                    public void onError(String msg, int code) {
                        toastShort(msg);
                        mBinding.refreshLayout.finishRefresh();
                    }
                });
            }
        });
        mBinding.refreshLayout.setOnLoadMoreListener(refreshLayout -> {
            mViewModel.loadMoreRecommend(mRoomId).observe(this, new BaseObserver<RecommendRoomResult>() {
                @Override
                public void onSuccess(RecommendRoomResult result) {
                    if (result != null) {
                        RecommendRoomResult.PageInfoBean pageInfo = result.getPageInfo();
                        recommendAdapter.addData(result.getList());
                        mBinding.refreshLayout.finishLoadMore();
                        mBinding.refreshLayout.setEnableLoadMore(Integer.valueOf(pageInfo.getPage()) < pageInfo.getTotalPage());
                    }
                }

                @Override
                public void onError(String msg, int code) {
                    toastShort(msg);
                    mBinding.refreshLayout.finishLoadMore();
                    mViewModel.pageNumber--;
                }
            });
        });
    }

    /**
     * 从assets目录下读取歌词文件内容
     *
     * @param fileName
     * @return
     */
    public String getFromAssets(String fileName) {

        try {
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader inputReader = new InputStreamReader(fis, AddressCenter.getAddress().getLrcCode());
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals(""))
                    continue;
                result += line + "\r\n";
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 展示歌曲的定时任务
     */
    class LrcTask extends TimerTask {
        @Override
        public void run() {
            //获取歌曲播放的位置
            int currentPosition = AgoraClient.create().rtcEngine().getAudioMixingCurrentPosition();
            int max = AgoraClient.create().rtcEngine().getAudioMixingDuration();
            Log.d("msg_send_lyric", "LrcTask:" + currentPosition + "," + max);
            sendLyc(currentPosition);
            boolean tioajian1 = currentPosition >= max - 5000;
            boolean tioajian2 = currentPosition != 0 && max != 0;
            boolean isChange = tioajian1 && tioajian2;

            if (isChange) {

                mViewModel.changeSongParam();
                if (mTask != null) {
                    mTask.cancel();
                }
                if (mTimer != null) {
                    mTimer.cancel();
                }
            }
        }
    }

    private void getFriendList(boolean refresh) {
        mViewModel.getFriendList().observe(this, new BaseObserver<FriendInfoListBean>() {
            @Override
            public void onSuccess(FriendInfoListBean result) {
                if (result != null) {
                    if (result.getList() != null && result.getList().size() > 0) {

                        if (refresh && mMpAdapter != null) {
                            mMpAdapter.notifyDataSetChanged();
//                            initMicro();
                        }
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    private void pushRoomMsg() {
        mViewModel.pushRoomMsg().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {

            }

            @Override
            public void onError(String msg, int code) {

            }
        });
    }

    private void inviteUser(String inviteeId, int free, int micro_level) {
        int inviterId = hongId;//邀请者
        mViewModel.inviteUp(inviterId, inviteeId, Integer.valueOf(mRoomId), free, micro_level, mRoomData.getOnMicroCost()).observe(RoomActivity.this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                if (result.getCode() == 200) {
                    toastShort("已发送邀请请求");
                    if (micManagerDialog != null) {
                        micManagerDialog.dismiss();
                    }
                    if (micManagerForServenDialog != null) {
                        micManagerForServenDialog.dismiss();
                    }
                } else {
                    toastShort(result.getDesc());
                }
            }

            @Override
            public void onError(String msg, int code) {
                toastShort(msg);
            }
        });
    }

    private void initRole() {
        if (PermissionChecker.checkPermission(mContext, Manifest.permission.CAMERA, Process.myPid(), Process.myUid(), getPackageName()) == 0) {
        }

        RxPermissions permissions = new RxPermissions(this);

        permissions.request(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
//                        RoomController.getInstance().initAgoraEngineAndJoinChannel();
                    }
                });
    }

    private void initWsLiveData() {
        mViewModel.mWsLiveData.observe(this, integer -> {
            if (!NetWorkUtils.IsNetWorkEnable(mContext) || !RoomController.getInstance().inRoom()) {
                return;
            }
            if (integer != null && integer.intValue() != 300) {
                if (mWsLoading == null) {
                    mWsLoading = new AutoHideLoading(mContext);
                }
                mWsLoading.tag = integer;
                mWsLoading.show();
            }
        });
    }

    private AutoHideLoading mWsLoading;

    private void dealSendGift(int success, boolean isSingle) {
        switch (success) {
            case 1:
                updatePacks();
//                if (mGiftDialog != null) {
//                    mGiftDialog.dismiss();
//                }
//                getFriendList();
                break;
            //余额不足
            case 4:
                if (isSingle) {
                    showBalanceAlert();
                }
                break;
            //余额不足
            case 6:
                if (!isSingle) {
                    showBalanceAlert();
                }
                break;
            //青少年模式、家长模式
            case 10:
            case 20:
                break;
            //礼物数量不足
            case 30:
                showRechargeTip();
                break;
            case 2:
                toastShort("该用户不在房间");
                break;
            default:
                break;
        }
    }

    private void showRechargeTip() {
        if (mAlertDlg == null) {
            mAlertDlg = new AAlertDialog(mContext);
        }
        mAlertDlg.setCloseVisible();
        mAlertDlg.setMessage("礼物数量不够，请充值购买", R.color.gray, 16);
        mAlertDlg.setRightButton("确定", R.color.gray, (v, dialog) -> {
            dialog.dismiss();
            PageJumpUtils.jumpToRechargeDialog(this, "", false, "", "");

        });
        mAlertDlg.setLeftButton("取消", R.color.gray, (v, dialog) -> {
            dialog.dismiss();
        });
        mAlertDlg.show();
    }

    /**
     * 账户余额不足弹框
     */
    private void showBalanceAlert() {
        judgeFirstRecharge(false, false, null, "0");
    }

    private void showRechargAlerteDialog() {
        if (mAlertDlg == null) {
            mAlertDlg = new AAlertDialog(mContext);
        }
        mAlertDlg.setMessage("余额不足，请充值后再试。", R.color.black, 15);
        mAlertDlg.setLeftButton("取消", R.color.gray, null);
        mAlertDlg.setRightButton("充值", R.color.primary_pink, (v, dialog1) -> {
            PageJumpUtils.jumpToRechargeDialog(this, "", false, "", "");
            dialog1.dismiss();
        });
        mAlertDlg.show();
    }

    /**
     * 首冲福利
     */
    private void showFistRechargeWelfare(FirstRechargeVo data, boolean isShowGift, boolean single, MicroUser microUser, String status) {
        RechargeFirstWelfareDialog dialog = new RechargeFirstWelfareDialog(mContext);
        dialog.init(data);
        dialog.setOnClickListener(new RechargeFirstWelfareDialog.OnClickListener() {
            @Override
            public void onClick() {
                PageJumpUtils.jumpToRechargeDialog(RoomActivity.this, "", true, data.getRmb(), data.getChargeid());
                if (mGiftDialog != null && mGiftDialog.isShowing()) {
                    mGiftDialog.dismiss();
                }
            }

            @Override
            public void onMyDismiss(boolean isClickRecharge) {
                if (!isShowGift) {
                    if (!isClickRecharge) {
                        PageJumpUtils.jumpToRechargeDialog(RoomActivity.this, "", false, "", "");
                    }
                } else {
                    if (!isClickRecharge) {
                        showGiftDialog(single, status, microUser);
                    }
                }
            }
        });
        dialog.showAtBottom();

    }

    /**
     * 判断是否是首次充值
     */
    private void judgeFirstRecharge(boolean isShowGift, boolean single, MicroUser microUser, String status) {
        mViewModel.judgeFirstRecharge().observe(this, new BaseObserver<FirstRechargeVo>() {
            @Override
            public void onSuccess(FirstRechargeVo result) {
                if (result != null) {
                    int firstRecharge = result.getIs_first();
                    if (!isShowGift) {
                        if (firstRecharge == 1) {
                            //是首冲
                            showFistRechargeWelfare(result, false, single, microUser, status);
                        } else {
                            showRechargAlerteDialog();
                        }
                    } else {
                        if (firstRecharge == 1) {
                            showFistRechargeWelfare(result, true, single, microUser, status);
                        } else {
                            showGiftDialog(single, status, microUser);
                        }
                    }
                }
            }
        });
    }

    //非自由麦排序
    private void initMicroSort() {
        mSortHandler.setNewMicroSorts(mRoomData.getMicroOrders());
        setMicroSort();
    }

    private void setMicroSort() {
        setMpIntroText();
        if (mSortManageDialog != null && mSortManageDialog.isShowing()) {
            mSortManageDialog.setNewData(mSortHandler.getOrders());
        }
        if (mCheckDialog != null && mCheckDialog.isShowing()) {
            mCheckDialog.setData(mSortHandler.getSortsBySex());
        }
        if (micManagerDialog != null) {
            micManagerDialog.setApplyData(mSortHandler.getOrders(), selectMicManagerSex);
            EventBus.getDefault().post(new UpdateInRoomMemberEvent());
        }
        if (micManagerForServenDialog != null) {
            micManagerForServenDialog.setApplyData(mSortHandler.getSorts());
            EventBus.getDefault().post(new UpdateInRoomMemberEvent());
        }

    }

    private void inviteUpMicro(int inviteMicroId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isInvite) {

//                    AAlertDialog dialog = new AAlertDialog(mContext);
//                    dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialog) {
//                            isInvite = false;
//                        }
//                    });
//                    dialog.setTitle("主持邀请您上麦，是否同意上麦？", R.color.black, 15);
//                    String suffix = "";
//                    if (free != 1) { //付费
//                        suffix = "（" + cost + "朵玫瑰）";
//                        dialog.setMessage(suffix);
//                    }
//                    dialog.setRightButton("同意", R.color.primary_pink, (v, dialog1) -> {
//                        dialog.setOnDismissListener(null);
//                        dialog1.dismiss();
                    microType = 2;
                    mViewModel.agreeInviteMpOutRoom(true, inviteMicroId, false);
                    isInvite = false;
//                    });
//                    dialog.setLeftButton("拒绝", R.color.gray, (v, dialog1) -> {
//                        dialog1.dismiss();
//
//                    });
//                    dialog.show();
//                    mHandler.postDelayed(dialog::dismiss, 5000);
                } else if (isInRoomInvite) {
                    MpInvited bean = new MpInvited();
                    bean.setFree(free);
                    bean.setCost(String.valueOf(cost));
                    onMpInvited(bean);
                }
            }
        }, 1000);
    }

    private void setMpIntroText() {

        boolean hasMicroNum = !CollectionUtils.isEmpty(mSortHandler.getSorts()) && mSortHandler.getSorts().size() > 0;
        if (MatchMakerUtils.isRoomOwner()) {
            ViewBindUtils.setText(mBinding.tvMicroManageDesc, "上麦管理");

        } else {

            if (mRoomData.getWheatCardCount() > 0 && !mRoomModel.isOnMp()) {
                ViewBindUtils.setText(mBinding.tvMicroManageDesc, "免费上麦");
                ViewBindUtils.setText(mBinding.tvMicroManageCost, mUser.getSex().equals("1") ? "剩余上麦卡x" + mRoomData.getWheatCardCount() : "");
            } else {
                ViewBindUtils.setText(mBinding.tvMicroManageDesc, mRoomModel.isOnMp() ? "申请下麦" : "申请上麦");
                if (mJoinRoom.getRoomMode() == 9) {
                    ViewBindUtils.setText(mBinding.tvMicroManageCost, mRoomModel.isOnMp() ? "" : String.format("%d玫瑰", mRoomData.getOnMicroCost()));

                } else {
                    ViewBindUtils.setText(mBinding.tvMicroManageCost, mRoomModel.isOnMp() || mUser.getSex().equals("2") ? "" : String.format("%d玫瑰", mRoomData.getOnMicroCost()));
                }
            }
        }
        ViewBindUtils.setText(mBinding.tvMicroManageNum, hasMicroNum ? String.valueOf(mSortHandler.getSorts().size()) : "");
        ViewBindUtils.setVisible(mBinding.llMicmanageNum, hasMicroNum && MatchMakerUtils.isRoomOwner());
    }

    private void initTitle() {
        mBinding.roomNameTv.setText(mRoomData.getRoomData().getRoomName());
        long heartValue = mRoomData.getVisitorNum();
        String prettyId = mRoomData.getRoomData().getPrettyId();
        if (TextUtils.isEmpty(prettyId)) {
            prettyId = mRoomId;
        }
        onOnlineGuestChanged(mRoomData.getOnlineLeftNumber(), mRoomData.getOnlineRightNumber());
        onRequestMicroGuestChanged(mRoomData.getWaitMicroLeftNumber(), mRoomData.getWaitMicroRightNumber());
        mBinding.roomIdTv.setText("ID:" + mRoomId);

        GlideUtils.circleImage(mBinding.ownerIv, mRoomData.getRoomOwnerHeadUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
        updateSongAppointmentNum(mRoomData.getSongCount());


    }

    private RoomData.MicroInfosBean mainMpInfo = null;

    private void initSecondRoom() {
        if (mRoomModel.getHostMicro().getUser() != null) {
            hongId = Integer.valueOf(mRoomModel.getHostMicro().getUser().getUserId());
        }

        Log.d("initSecondRoom", "initSecondRoom");
        if (!isChangeView) {
            Log.d("initSecondRoom", JsonConverter.toJson(mRoomModel.getHostMicro()));

            Log.d("initSecondRoom", JsonConverter.toJson(mRoomModel.getMicros().get(0)));
            mBinding.mainSecondOneView.setMicroData(mRoomModel.getHostMicro());
            mainMpInfo = mRoomModel.getHostMicro();

            List<RoomData.MicroInfosBean> list = new ArrayList<>();
            list.add(mRoomModel.getMicros().get(0));
            mMpAdapter.setNewData(list);
            Log.d("initSecondRoom", String.valueOf(mRoomModel.getHostMicro().getUser() == null));

            if (mRoomModel.getHostMicro().getUser() == null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initSecondRoom();
                    }
                }, 500);
            }

        } else {
            RoomData.MicroInfosBean singerInfo = null;
            RoomData.MicroInfosBean guestInfo = null;
            WsUser hostUser = mRoomModel.getHostMicro().getUser();
            if (SongStateUtils.getSingleton2().getConsertUserId().equals(hostUser.getUserId())) {
                singerInfo = mRoomModel.getHostMicro();
                guestInfo = mRoomModel.getMicros().get(0);

            } else {
                guestInfo = mRoomModel.getHostMicro();
                singerInfo = mRoomModel.getMicros().get(0);

            }
            mainMpInfo = singerInfo;
            List<RoomData.MicroInfosBean> list = new ArrayList<>();
            list.add(guestInfo);
            mMpAdapter.setNewData(list);
            mBinding.mainSecondTwoMusicView.setMicroData(singerInfo);
        }
        initMainMp(mainMpInfo);
    }

    private void initMicro() {
        int roomType = mJoinRoom.getRoomMode();
        if (roomType == 9) {
            initSecondRoom();
        } else {
            if (!isChangeView) {
                List<RoomData.MicroInfosBean> microInfosBeansMain = new ArrayList<>();
                List<RoomData.MicroInfosBean> infos = mRoomModel.getMicros();
                for (RoomData.MicroInfosBean bean : infos) {
                    switch (bean.getType()) {
                        //红娘麦
                        case 0:
                            mRoomModel.setHostMicro(bean);
                            break;
                        default:
                            microInfosBeansMain.add(bean);
                            break;
                    }
                    mRoomModel.setMicros(microInfosBeansMain);
                }
                mainMpInfo = mRoomModel.getHostMicro();
                initMainMp(mRoomModel.getHostMicro());
                mMpAdapter.setNewData(mRoomModel.getMicros());
            } else {
                List<RoomData.MicroInfosBean> microInfosBeansMain = new ArrayList<>();
                List<RoomData.MicroInfosBean> infos = mRoomModel.getMicros();
                for (RoomData.MicroInfosBean bean : infos) {
                    switch (bean.getType()) {
                        //红娘麦
                        case 0:
                            mRoomModel.setHostMicro(bean);
                            break;
                        default:
                            microInfosBeansMain.add(bean);
                            break;
                    }
                    mRoomModel.setMicros(microInfosBeansMain);
                }
                WsUser hostUser = mRoomModel.getHostMicro().getUser();
                if (singerUserId.equals(hostUser.getUserId())) {
                    initMainMp(mRoomModel.getHostMicro());
                    mainMpInfo = mRoomModel.getHostMicro();
                    mMpAdapter.setNewData(mRoomModel.getMicros());
                    return;
                } else {
                    List<RoomData.MicroInfosBean> microInfosBeanList = new ArrayList<>();
                    RoomData.MicroInfosBean houstMicro = null;
                    microInfosBeanList.add(mRoomModel.getHostMicro());
                    if (mRoomModel.getHostMicro() != null) {
                        RoomData.MicroInfosBean hongMiroInfo = mRoomModel.getHostMicro();
                        hongId = Integer.valueOf(hongMiroInfo.getUser().getUserId());
                        InMicroMemberUtils.getInstance().saveMicroMembers(String.valueOf(hongMiroInfo.getType()), String.valueOf(hongId));
                        houstMicro = mRoomModel.getHostMicro();
                    }
                    for (RoomData.MicroInfosBean bean : mRoomModel.getMicros()) {
                        if (bean.getUser() != null && bean.getUser().getUserId().equals(singerUserId)) {
                            houstMicro = bean;
                        } else {
                            microInfosBeanList.add(bean);
                        }
                    }
                    mainMpInfo = houstMicro;
                    initMainMp(houstMicro);
                    mMpAdapter.setNewData(microInfosBeanList);
                }
            }
        }

    }

    private int mpMicroClickUi;//1 mainFaceView

    //主麦
    private void initMainMp(RoomData.MicroInfosBean bean) {
        if (mJoinRoom.getRoomMode() != 9) {
            if (!isChangeView) {
                if (bean.getUser() != null) {
                    hongId = Integer.valueOf(bean.getUser().getUserId());
                    InMicroMemberUtils.getInstance().saveMicroMembers(String.valueOf(bean.getType()), String.valueOf(hongId));
                }
                mainFaceView.setMicroData(bean);
                mainFaceView.setOnMicUserListener(onMicUserListener);
                mainFaceView.setOnMpClickListener(new MicroFaceView.OnMpClickListener() {
                    @Override
                    public void onClick(WsUser user) {
                        RoomData.MicroInfosBean bean = mRoomModel.getHostMicro();
                        if (bean.getUser() != null) {
                            initAvatarDialog(0, bean.getUser().getUserId());
                            return;
                        }
                        if (mRoomModel.isOnMp()) {
                            return;
                        }
                        if (MatchMakerUtils.isRoomOwner()) {
                            setRoleBroadcast(bean.getType(), bean.getNumber());
                        } else {
                            toastShort("此位置需要主持身份");
                        }
                    }
                });
            } else {
                mainFaceMusicView.setMicroData(bean);
                mainFaceMusicView.setOnMicUserListener(onMicUserListener);
                mainFaceMusicView.setOnMpClickListener(new MicroFaceView.OnMpClickListener() {
                    @Override
                    public void onClick(WsUser user) {
                        RoomData.MicroInfosBean bean = mainMpInfo;
                        if (bean.getUser() != null) {
                            initAvatarDialog(0, bean.getUser().getUserId());
                            return;
                        }
                        if (mRoomModel.isOnMp()) {
                            return;
                        }
                        if (MatchMakerUtils.isRoomOwner()) {
                            setRoleBroadcast(bean.getType(), bean.getNumber());
                        } else {
                            toastShort("此位置需要主持身份");
                        }
                    }
                });
            }
        } else {
            if (!isChangeView) {
                mBinding.mainSecondOneView.setOnMicUserListener(onMicUserListener);
            } else {
                mBinding.mainSecondTwoMusicView.setMicroData(bean);
            }
            mBinding.mainSecondOneView.setOnMicUserListener(onMicUserListener);
            mBinding.mainSecondOneView.setOnMpClickListener(onMpClickListener);
            mBinding.mainSecondTwoMusicView.setOnMicUserListener(onMicUserListener);
            mBinding.mainSecondTwoMusicView.setOnMpClickListener(onMpClickListener);

        }

    }

    MicroFaceView.OnMpClickListener onMpClickListener = new MicroFaceView.OnMpClickListener() {
        @Override
        public void onClick(WsUser user) {
            WsUser item = user;
            if (user != null) {
                initAvatarDialog(0, user.getUserId());
            } else {
                boolean isRoomOwner = MatchMakerUtils.isRoomOwner();
                if (isRoomOwner) {
                    showMicManageDialog(10 - inMicroManNum);
                } else {
                    showMicroManageDialog(1);

                }
            }
        }

    };


    MicroFaceView.OnMicUserListener onMicUserListener = new MicroFaceView.OnMicUserListener() {
        @Override
        public void sendSingleGift(WsUser user) {
            MicroUser microUser = new MicroUser();
            microUser.setUser(user);
            judgeFirstRecharge(true, true, microUser, "0");
        }

        @Override
        public void addFriend(String uid, String imgUrl, String nickName) {
            PageJumpUtils.jumpToFriendAddForResult(RoomActivity.this, uid, imgUrl, nickName, UserUtils.getUser().getUid().equals(String.valueOf(hongId)), "1");
        }

        @Override
        public void fansList(WsUser user) {
            showFansListDialog(user);
        }

        @Override
        public void sendOneRose(String userId) {
            RoomActivity.this.sendOneRose(userId);
        }

        @Override
        public void downMicro(String userId, int level, int number) {
            if (MatchMakerUtils.isRoomOwner() && !UserUtils.getUser().getUid().equals(userId)) {
                //红娘强制别人下麦
//                ToastUtils.showToast("功能待定");
                AAlertDialog alertDialog = new AAlertDialog(mContext);
                alertDialog.setMessage("是否将该用户移下麦");
                alertDialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        mViewModel.downMicro(level, number);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            } else {
                //主动下麦
                showAudience();
            }
        }

        @Override
        public void microOperate(String userId, boolean isOpen) {
            updateMicroState(userId, isOpen);
        }
    };

    private void updateMicroState(String userId, boolean isOpen) {
        if (UserUtils.getUser().getUid().equals(userId)) {
            //是自己   闭麦
            int result = AgoraClient.create().muteLocalAudioStream(!isOpen);
            if (result == 0) {
                ForbiddenStateUtils.saveForbiddenMicState(userId, !isOpen);
                ToastUtils.showToast("操作成功");
            }
        } else {
            //禁言
            int result = AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(userId), !isOpen);
            if (result == 0) {
                ForbiddenStateUtils.saveForbiddenLBState(userId, !isOpen);
                ToastUtils.showToast("操作成功");
            }
        }
    }

    private void upMpMicroUi(int isFromAdapter, int level, int num, boolean state, String userId) {
        if (isFromAdapter == -1) {
            mMpAdapter.upDateMpMicro(level, num, state);
        } else {
            RoomData.MicroInfosBean host = mRoomModel.getHostMicro();
            RoomData.MicroInfosBean guest = mRoomModel.getMicros().get(0);
            if (mJoinRoom.getRoomMode() == 9) {
                if (isChangeView) {
                    if ((host.getUser() != null && host.getUser().getUserId().equals(singerUserId)) || (guest.getUser() != null && guest.getUser().getUserId().equals(singerUserId))) {
                        mBinding.mainSecondTwoMusicView.upDataMicroState(state);
                    }
                } else {
                    if (host.getUser() != null && userId.equals(host.getUser().getUserId())) {
                        mBinding.mainSecondOneView.upDataMicroState(state);
                    }

                }
            } else {
                if (isChangeView) {
                    if (host.getUser() != null && userId.equals(host.getUser().getUserId())) {
                        mBinding.mainFaceMusicView.upDataMicroState(state);
                    }
                } else {

                    if (host.getUser() != null && userId.equals(host.getUser().getUserId())) {
                        mBinding.mainFaceView.upDataMicroState(state);
                    }
                }
            }
        }
    }

    private void initMainAnim(String url) {
        if (mJoinRoom.getRoomMode() != 9) {
            if (isChangeView) {
                mBinding.animMusicIv.loadNormalAnim(url);
            } else {
                mBinding.animIv.loadNormalAnim(url);
            }
        } else {
            if (isChangeView) {
                mBinding.animSecondTwoMusicIv.loadNormalAnim(url);
            } else {
                mBinding.animSecondOneIv.loadNormalAnim(url);
            }
        }
    }

    public void initGoldEgg() {
//        ViewBindUtils.setVisible(mBinding.smashEggIv, mRoomData.isCanBreakEgg());
    }

    //榜单
    private void initRank() {
        mBinding.rankingRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mBinding.rankingRv.setNestedScrollingEnabled(false);
        mRankingAdapter = new RoomRankingAdapter(mContext);
        mBinding.rankingRv.setAdapter(mRankingAdapter);

    }

    //连麦
    private void initCommonMp() {
        int rooomType = mJoinRoom.getRoomMode();
        if (rooomType == 9) {
            ViewBindUtils.setVisible(mBinding.cnsMicrosCommonViews, false);
            ViewBindUtils.setVisible(mBinding.cnsSecondViews, true);
            if (!isChangeView) {
                rv_second_view.setLayoutManager(new GridLayoutManager(mContext, 1));
                if (rv_second_view.getItemAnimator() != null) {
                    rv_second_view.getItemAnimator().setChangeDuration(0);
                }
            } else {
                rv_second_music_view.setLayoutManager(new GridLayoutManager(mContext, 1));
                if (rv_second_music_view.getItemAnimator() != null) {
                    rv_second_music_view.getItemAnimator().setChangeDuration(0);
                }
            }
        } else {
            ViewBindUtils.setVisible(mBinding.cnsMicrosCommonViews, true);
            ViewBindUtils.setVisible(mBinding.cnsSecondViews, false);
            if (!isChangeView) {
                vicePhoneRv.setLayoutManager(new GridLayoutManager(mContext, 2));
                vicePhoneRv.setNestedScrollingEnabled(false);
                if (vicePhoneRv.getItemAnimator() != null) {
                    vicePhoneRv.getItemAnimator().setChangeDuration(0);
                }
            } else {
                vicePhoneMusicRv.setLayoutManager(new GridLayoutManager(mContext, 1));
                vicePhoneMusicRv.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        if (parent.getChildAdapterPosition(view) == 1)
                            outRect.top = DisplayUtil.dip2px(mContext, 8);
                    }
                });
                vicePhoneMusicRv.setNestedScrollingEnabled(false);
                if (vicePhoneMusicRv.getItemAnimator() != null) {
                    vicePhoneMusicRv.getItemAnimator().setChangeDuration(0);
                }
            }
        }

        mMpAdapter = new RoomAudioAdapter(mContext);
        mMpAdapter.setOnMPClickLister((position) -> {
            RoomData.MicroInfosBean item = mMpAdapter.getItem(position);
            if (item.getUser() != null) {
                initAvatarDialog(-1, item.getUser().getUserId());
            } else {
                boolean isRoomOwner = MatchMakerUtils.isRoomOwner();
                if (mJoinRoom.getRoomMode() == 9) {
                    if (isRoomOwner) {
                        showMicManageDialog(10 - inMicroManNum);
                    } else {
                        showMicroManageDialog(1);
                    }
                } else {
                    if (!isRoomOwner) {
                        boolean isOnMp = mRoomModel.isOnMp();
                        if (!isOnMp) {
                            if (TextUtils.equals(mUser.getSex(), String.valueOf(mMpAdapter.getItem(position).getType()))) {
                                setRoleBroadcast(item.getType(), 100);
                            }
                        }
                    } else {

                        selectMicManagerSex = position + 1;
                        showMicManageDialog(String.valueOf(selectMicManagerSex), 10 - inMicroWomanNum);

                    }
                }
            }
        });
        mMpAdapter.setOnMicUserListener(new RoomMpAdapter.OnMicUserListener() {
            @Override
            public void sendSingleGift(WsUser user) {
                MicroUser microUser = new MicroUser();
                microUser.setUser(user);
//                showGiftDialog(true, "0", microUser);
                judgeFirstRecharge(true, true, microUser, "0");
            }

            @Override
            public void addFriend(String uid, String imgUrl, String nickName) {
                PageJumpUtils.jumpToFriendAddForResult(RoomActivity.this, uid, imgUrl, nickName, UserUtils.getUser().getUid().equals(String.valueOf(hongId)), "1");
            }

            @Override
            public void fansList(WsUser user) {
                showFansListDialog(user);
            }

            @Override
            public void sendOneRose(String userId) {
                RoomActivity.this.sendOneRose(userId);
            }

            @Override
            public void downMicro(String userId, int level, int number) {
                Log.d("AG_EX_AV", "downMicro：" + userId + level + "," + number);

                if (MatchMakerUtils.isRoomOwner() && !UserUtils.getUser().getUid().equals(userId)) {
                    //红娘强制别人下麦
                    AAlertDialog alertDialog = new AAlertDialog(mContext);
                    alertDialog.setMessage("是否将该用户移下麦");
                    alertDialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, Dialog dialog) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                        @Override
                        public void onClick(View v, Dialog dialog) {
                            mViewModel.downMicro(level, number);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    showAudience();
                }
            }

            @Override
            public void microOperate(int pos, String userId, boolean isOpen) {
                updateMicroState(userId, isOpen);

            }
        });
        if (rooomType != 9) {
            if (!isChangeView) {
                mMpAdapter.setMusicType(false);
                vicePhoneRv.setAdapter(mMpAdapter);
            } else {
                mMpAdapter.setMusicType(true);
                vicePhoneMusicRv.setAdapter(mMpAdapter);

            }
        } else {
            if (!isChangeView) {
                mMpAdapter.setMusicType(false);
                rv_second_view.setAdapter(mMpAdapter);
            } else {
                mMpAdapter.setMusicType(true);
                rv_second_music_view.setAdapter(mMpAdapter);

            }
        }

    }

    private void showFansListDialog(WsUser user) {
        UserFansDialog userFansDialog = new UserFansDialog(mContext, mViewModel);
        userFansDialog.setMsg(user);
        userFansDialog.showAtBottom();
    }


    //消息
    private void initMpMsg() {
        mMsgAdapter = new RoomMsgAdapter();
        if (mBinding.roomMsgRv.getItemAnimator() != null) {
            mBinding.roomMsgRv.getItemAnimator().setChangeDuration(0);
        }
        mMsgAdapter.setOnMsgEventListener(new RoomMsgHandler.OnMsgEventListener() {
            @Override
            public void onMsgClick(String uid) {
                initAvatarDialog(0, uid);
            }

            @Override
            public void onMsgLongClick(String uName) {
                mInputDialog = new InputTextDialog(mContext);
                mInputDialog.setMsg("@" + uName + "  ");
                mInputDialog.setOnTextSendListener(msg -> {
                    if (TextUtils.isEmpty(msg))
                        return;
                    if (mRoomData.isDisableMsg()) {
                        toastShort("您已被禁言，请联系管理员或房主进行解禁");
                        return;
                    }
                    RoomController.getInstance().sendTextMsg(msg);
                });
                mInputDialog.show();
            }
        });
        mBinding.roomMsgRv.setAdapter(mMsgAdapter);
        mBinding.roomMsgRv.setBufferTime(200);
        mBinding.roomMsgRv.setMaxChatNum(RoomController.DEFAULT_MSG_MAX_NUM);
        mBinding.roomMsgRv.setUp();
        mBinding.tvRule.setText(mRoomData.getTips());

    }

    //底部功能栏
    private void initBottom() {


        setMpIntroText();
        if (mJoinRoom != null) {
//            ViewBindUtils.setVisible(mBinding.llRoomMusic, mJoinRoom.isOpenPickSong());//是否开启点歌
            ViewBindUtils.setVisible(mBinding.llRoomMedia, mJoinRoom.isOpenMediaLibrary());//是否开启媒体库

        }
    }

    private void getBanners() {
        mBinding.banner.setPageIndicator(new int[]{R.drawable.room_indicator, R.drawable.room_indicator_s});
        mViewModel.getBanners().observe(this, new BaseObserver<List<VoiceBanner.BannerListBean>>() {
            @Override
            public void onSuccess(List<VoiceBanner.BannerListBean> result) {
                if (result != null && result.size() > 0) {
                    setBanner(result);
                }
            }
        });
    }


    private void setBanner(List<VoiceBanner.BannerListBean> list) {
        if (mRoomData.isFirstCharge()) {
            VoiceBanner.BannerListBean firstWelfareBean = new VoiceBanner.BannerListBean(R.drawable.icon_first_welfare_banner);
            firstWelfareBean.setLink_type("3");
            firstWelfareBean.setUi_type("99");
            firstWelfareBean.setTitle("首冲福利");
            firstWelfareBean.setLocalImage(true);
            list.add(firstWelfareBean);
        }
        mBinding.banner.setPages(new RoomBannerAdapter(), list);
        mBinding.banner.setPointViewVisible(list.size() > 1);
        mBinding.banner.setOnItemClickListener(position -> {
            VoiceBanner.BannerListBean bean = list.get(position);
            if (bean.getLink_type().equals("3")) {
                switch (bean.getUi_type()) {
                    case "1":

                        break;
                    case "99":
                        judgeFirstRecharge(false, false, null, "0");
                        break;
                    default:
                        break;
                }

            } else {
                PageJumpUtils.jumpToWeb(bean.getTitle(), bean.getLinkurl());
            }
        });
        if (list.size() > 1) {
            mBinding.banner.startTurning(5000);
        } else {
            mBinding.banner.stopTurning();
        }
    }

    /**
     * 上麦
     *
     * @param type   麦位类型
     * @param number 麦序
     */
    private int upMicroType;
    private int upMicroNum;

    private void setRoleBroadcast(int type, int number) {
        if (TextUtils.equals(mUser.getSex(), "2")) {  //1男 2女
            microType = 3;
            upMicroType = type;
            upMicroNum = number;
            mViewModel.upMicro(type, number, false);
        } else {
            mViewModel.microCost(type, number);
        }
    }


    /**
     * 下麦界面
     */
    private void showAudience() {
        AAlertDialog aAlertDialog = new AAlertDialog(mContext);
        aAlertDialog.setTitle("是否确定下麦？", com.deepsea.mua.stub.R.color.black, 15);
        aAlertDialog.setRightButton("确定", com.deepsea.mua.stub.R.color.primary_pink, new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                aAlertDialog.dismiss();
                if (hongId == Integer.valueOf(UserUtils.getUser().getUid())) {
                    mViewModel.exitRoom();
                } else {
                    mViewModel.downMicro(0, 100);
                }
            }
        });
        aAlertDialog.setLeftButton("取消", com.deepsea.mua.stub.R.color.gray, new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                aAlertDialog.dismiss();
            }
        });
        aAlertDialog.show();

    }

    /**
     * Activity是否在前台
     *
     * @param context
     * @return
     */
    private boolean isOnForground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();
        if (appProcessInfoList == null) {
            return false;
        }

        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo processInfo : appProcessInfoList) {
            if (processInfo.processName.equals(packageName) && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (foren) {
//            foren = true;
        AgoraClient.create().rtcEngine().enableLocalVideo(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMpAdapter.notifyDataSetChanged();

            }
        }, 1000);

//        }
        Log.d("StateChanged", foren + "");

//        onNewMessage(RoomController.getInstance().getHyphenateUnreadCount());
        isResumed = true;
    }

    boolean foren = true;

    @Override
    protected void onStop() {
        isResumed = false;
        foren = !isOnForground(getApplicationContext());
        if (foren) {
            AgoraClient.create().rtcEngine().enableLocalVideo(false);
        }
        Log.d("StateChanged", foren + "");
        super.onStop();
    }

    private void initAvatarDialog(int isFromAdapter, String uid) {
        mViewModel.getUserInfo(uid);
        mAvatarDialog = new UserAvatarDialog(mContext);
        mAvatarDialog.setOnAvatarListener(new UserAvatarDialog.OnAvatarListener() {

            @Override
            public void onUpDownMicro(boolean isOnMicro) {
                if (isOnMicro) {
                    showAudience();
                } else {
                    setRoleBroadcast(0, 100);
                }
            }

            @Override
            public void onForceDownMp(String uid, int level, int number) {
                mViewModel.downMicro(level, number);

            }

            @Override
            public void onForbidden(WsUser wsUser, boolean isDisableMsg) {
                if (isDisableMsg) {
                    ForbiddenUserDialog forbiddenUserDialog = new ForbiddenUserDialog(mContext, mViewModel);
                    forbiddenUserDialog.setData(wsUser);
                    forbiddenUserDialog.setOnClickListener(new ForbiddenUserDialog.OnClickListener() {
                        @Override
                        public void onClickOk(int disableMsgId) {
                            mViewModel.forbidChat(true, Integer.valueOf(wsUser.getUserId()), disableMsgId);

                        }
                    });
                    forbiddenUserDialog.show();
                } else {
                    mViewModel.forbidChat(false, Integer.valueOf(wsUser.getUserId()), 0);
                }

            }

            @Override
            public void onProfile(String uid) {
                PageJumpUtils.jumpToProfile(uid);
            }


            @Override
            public void onSendGift(String uid) {
                judgeFirstRecharge(true, true, null, "0");
            }

            @Override
            public void onRemove(String uid) {
                AAlertDialog removeDialog = new AAlertDialog(mContext);
                removeDialog.setMessage("是否将该用户移除房间？");
                removeDialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        removeDialog.dismiss();
                    }
                });
                removeDialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        mViewModel.removeRoom(uid);
                        removeDialog.dismiss();
                    }
                });
                removeDialog.show();
            }

            @Override
            public void onCleanHeart(int level, int number) {
                mViewModel.cleanXd(level, number);
            }

            @Override
            public void onDownTimer(int level, int number) {
                if (mTimerDialog == null) {
                    mTimerDialog = new MicroTimerDialog(mContext);
                }
                mTimerDialog.setOnTimerListener(time -> mViewModel.countDown(level, number, time));
                mTimerDialog.show();
            }

            @Override
            public void onCloseMicro(String uid, boolean isDisabledMicro, int level, int number) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        boolean isMySelf = UserUtils.getUser().getUid().equals(uid);
                        if (isMySelf) {
                            //闭麦
                            int result = AgoraClient.create().muteLocalAudioStream(!isDisabledMicro);
                            if (result == 0) {
                                ForbiddenStateUtils.saveForbiddenMicState(uid, !isDisabledMicro);
                                upMpMicroUi(isFromAdapter, level, number, !isDisabledMicro, uid);
                                Log.d("onCloseMicro", isFromAdapter + ":" + !isDisabledMicro);
                                ToastUtils.showToast("操作成功");
                            }
                        } else {
                            //禁言
                            int result = AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(uid), !isDisabledMicro);
                            if (result == 0) {
                                ForbiddenStateUtils.saveForbiddenLBState(uid, !isDisabledMicro);
                                Log.d("onCloseMicro", isFromAdapter + ":" + !isDisabledMicro);
                                upMpMicroUi(isFromAdapter, level, number, !isDisabledMicro, uid);

                                ToastUtils.showToast("操作成功");
                            }
                        }
                    }
                });

            }

            @Override
            public void inviteMphone(String name, String uid, int free) {
                List<InviteOnmicroData> ids = new ArrayList<>();
                InviteOnmicroData inviteOnmicroData = new InviteOnmicroData(Integer.valueOf(uid));
                ids.add(inviteOnmicroData);
                mViewModel.inviteMphone(free, ids, 0);
            }

            @Override
            public void addFriend(String uid, String imgUrl, String nickName, boolean isMyfriend) {
                if (isMyfriend) {
                    PageJumpUtils.jumpToChat(uid, nickName, "1");
                } else {
                    PageJumpUtils.jumpToFriendAddForResult(RoomActivity.this, uid, imgUrl, nickName, UserUtils.getUser().getUid().equals(String.valueOf(hongId)), "1");
                }

            }

            @Override
            public void chatUser(String uname) {
                mInputDialog = new InputTextDialog(mContext);
                mInputDialog.setMsg("@" + uname + "  ");
                mInputDialog.setOnTextSendListener(msg -> {
                    if (TextUtils.isEmpty(msg))
                        return;
                    if (mRoomData.isDisableMsg()) {
                        toastShort("您已被禁言，请联系管理员或房主进行解禁");
                        return;
                    }
                    RoomController.getInstance().sendTextMsg(msg);
                });
                mInputDialog.show();
            }

            @Override
            public void blockUser(String uid, boolean isBlock) {
                AAlertDialog aAlertDialog = new AAlertDialog(mContext);
                aAlertDialog.setTitle("拉黑对方");
                String msgDesc = "";
                if (isBlock) {
                    msgDesc = "拉黑对方后，将无法再接收到Ta的任何消息。";
                } else {
                    msgDesc = "是否取消拉黑该用户";
                }
                aAlertDialog.setMessage(msgDesc);
                aAlertDialog.setLeftButton("取消", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        aAlertDialog.dismiss();
                    }
                });
                aAlertDialog.setRightButton("确定", new AAlertDialog.OnClickListener() {
                    @Override
                    public void onClick(View v, Dialog dialog) {
                        if (isBlock) {
                            defriend(uid);
                        } else {
                            blackout(uid);
                        }
                        aAlertDialog.dismiss();
                    }
                });
                aAlertDialog.show();

            }

            @Override
            public void reportUser(String uid) {
                ArouterUtils.build(ArouterConst.PAGE_REPORT)
                        .withString("uid", uid)
                        .navigation();
            }
        });
    }

    private void defriend(String uid) {
        mViewModel.defriend(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        EMClient.getInstance().chatManager().deleteConversation(uid, true);
                        toastShort(result);
                    }
                });
    }

    private void blackout(String uid) {
        mViewModel.blackout(uid).observe(this,
                new ProgressObserver<BaseApiResult>(mContext) {
                    @Override
                    public void onSuccess(BaseApiResult result) {
                        toastShort(result);
                    }
                });
    }

    private String toOnWheatUid = "";

    private void showMicroSortDialog(int tab) {
        mSortManageDialog = new SortManageDialog(mContext, tab);
        mSortManageDialog.setOnMicroListener(new MicroSortAdapter.OnMicroListener() {
            @Override
            public void onTopMicro(String uid) {
                mViewModel.topMicro(uid);
            }

            @Override
            public void onOnWheat(String uid) {
                microType = 4;
                toOnWheatUid = uid;
                mViewModel.toOnWheat(uid, false);
            }

            @Override
            public void onRemove(String uid) {
                mViewModel.refuseUpMicro(uid);
            }
        });
        mSortManageDialog.setNewData(mSortHandler.getOrders());
        mSortHandler.getSorts();
        mSortManageDialog.showAtBottom();
    }

    /**
     * 麦位管理
     */
    private int onlineVisitorPageNum = 0;

    private void showMicManageDialog(int inMicroNum) {
        List<MicroOrder> microOrders = new ArrayList<>();
        if (mSortHandler.getSorts() != null && mSortHandler.getSorts().size() > 0) {
            List<MicroOrder> microOrders1 = mSortHandler.getSorts();
            microOrders.addAll(microOrders1);

            InMicroMemberUtils.getInstance().saveMicroNum(microOrders);
        }
        mViewModel.onlineUser(0, 0);
        micManagerForServenDialog = MicManagerForServenDialog.newInstance(microOrders, onlineVisitorPageNum, inMicroNum, mRoomData.getOnMicroCost());
        micManagerForServenDialog.setmListener(new OnManageListener() {
            @Override
            public void onTopMicro(String uid) {
                mViewModel.topMicro(uid);
            }

            @Override
            public void onOnWheat(String uid) {
                microType = 4;
                toOnWheatUid = uid;
                mViewModel.toOnWheat(uid, false);
            }

            @Override
            public void onRemove(String uid) {
                mViewModel.refuseUpMicro(uid);
            }

            @Override
            public void onInviteClick(List<InviteOnmicroData> ids, int free, int tabType) {
                mViewModel.inviteMphone(free, ids, 3);
                if (micManagerForServenDialog != null) {
                    micManagerForServenDialog.dismiss();
                }
            }

            @Override
            public void onInviteClick(String uid, int free, int typeType) {

                inviteUser(uid, free, 3);
            }

            @Override
            public void onSortInRoomRefreshClick() {
                onlineVisitorPageNum = 0;
                mViewModel.onlineUser(onlineVisitorPageNum, Integer.valueOf(0));

            }

            @Override
            public void onSortInRoomLoadMoreClick() {
                onlineVisitorPageNum++;
                mViewModel.onlineUser(onlineVisitorPageNum, Integer.valueOf(0));

            }
        });
        micManagerForServenDialog.show(getSupportFragmentManager(), "tag");
    }

    private void showMicManageDialog(String sex, int inMicroNum) {
        List<MicroOrder> microOrders = new ArrayList<>();
        if (mSortHandler.getOrders() != null && mSortHandler.getOrders().size() > 0) {
            List<MicroOrder> microOrders1 = mSortHandler.getOrders().get((Integer.valueOf(sex) - 1));
            microOrders.addAll(microOrders1);
            InMicroMemberUtils.getInstance().saveMicroNum(microOrders);
        }
        mViewModel.onlineUser(0, Integer.valueOf(sex));

        micManagerDialog = MicManagerDialog.newInstance(microOrders, sex, onlineVisitorPageNum, inMicroNum, mRoomData.getOnMicroCost());
        micManagerDialog.setmListener(new OnManageListener() {
            @Override
            public void onTopMicro(String uid) {
                mViewModel.topMicro(uid);
            }

            @Override
            public void onOnWheat(String uid) {
                microType = 4;
                toOnWheatUid = uid;
                mViewModel.toOnWheat(uid, false);
            }

            @Override
            public void onRemove(String uid) {
                mViewModel.refuseUpMicro(uid);
            }

            @Override
            public void onInviteClick(List<InviteOnmicroData> ids, int free, int tabType) {
                mViewModel.inviteMphone(free, ids, 3);
                if (micManagerDialog != null) {
                    micManagerDialog.dismiss();
                }
            }

            @Override
            public void onInviteClick(String uid, int free, int tabType) {

                inviteUser(uid, free, 3);
            }

            @Override
            public void onSortInRoomRefreshClick() {
                onlineVisitorPageNum = 0;
                mViewModel.onlineUser(onlineVisitorPageNum, Integer.valueOf(sex));

            }

            @Override
            public void onSortInRoomLoadMoreClick() {
                onlineVisitorPageNum++;
                mViewModel.onlineUser(onlineVisitorPageNum, Integer.valueOf(sex));

            }
        });
        micManagerDialog.show(getSupportFragmentManager(), "tag");
    }

    private void showSortCheckedDialog() {
        if (mCheckDialog == null) {
            mCheckDialog = new SortCheckDialog(mContext);
            mCheckDialog.setOnCancelSortListener(() -> operateCancleSort());
        }
        mCheckDialog.setData(mSortHandler.getSortsBySex());
        mCheckDialog.showAtBottom();
    }

    private void operateCancleSort() {
        isInvite = false;
        mViewModel.cancelSort();
    }

    /**
     * 赠礼
     *
     * @param single 是否是打赏单个用户
     */
    private void showGiftDialog(boolean single, String status, MicroUser microUser) {

        mGiftDialog = new RoomGiftDialog(mContext);
        mGiftDialog.setOnPresentListener(new OnPresentListener() {
            @Override
            public void onMultiSend(MultiSend sendModel) {
                mViewModel.multiSend(sendModel);
            }

            @Override
            public void onSingleSend(SingleSend sendModel) {
                mViewModel.singleSend(sendModel);
            }

            @Override
            public void onRecharge() {
                isInvite = false;
                isInRoomInvite = false;
                PageJumpUtils.jumpToRechargeDialog(RoomActivity.this, "", false, "", "");
            }


        });

        if (single) {
            if (microUser == null) {
                mGiftDialog.setSingleData(mAvatarDialog.getMicroUser(), status.equals("1"));
            } else {
                mGiftDialog.setSingleData(microUser, status.equals("1"));
            }
        } else {
            List<RoomData.MicroInfosBean> list = new ArrayList<>(mRoomModel.getMicros());
            if (MatchMakerUtils.isCanSendGift()) {
                list.add(0, mRoomModel.getHostMicro());
            }
            mGiftDialog.setMicroData(list);
        }
        mGiftDialog.setBalance(mRoomData.getBalance());
//        String status = "0";
//        if (isAddFriend) {
//            status = "1";
//        }

        mViewModel.getGifts(status).observe(this, new ProgressObserver<List<GiftBean>>(mContext) {
            @Override
            public void onSuccess(List<GiftBean> result) {
                mGiftDialog.setGiftData(result);
                mGiftDialog.showAtBottom();
            }
        });
        updatePacks();
    }

    private void updatePacks() {
        mViewModel.getGuardGifts("3").observe(this, new ProgressObserver<List<GiftBean>>(mContext) {
            @Override
            public void onSuccess(List<GiftBean> result) {
                if (mGiftDialog != null) {
                    mGiftDialog.setPackData(result);
                }
            }
        });

    }

    private void showEmojiDialog() {
        if (mEmojiDialog == null) {
            mEmojiDialog = new EmojiDialog(mContext);
            mEmojiDialog.setOnEmojioCheckedListener(emoji -> {
                mEmojiDialog.dismiss();
                if (mRoomData.isDisableMsg()) {
                    toastShort("您已被禁言，请联系管理员或房主进行解禁");
                    return;
                }

                if (TextUtils.equals(emoji.getType(), "2")) {
                    mViewModel.sendEmotion(emoji.getFace_id());
                } else {
                    RoomController.getInstance().sendEmoJiMsg(emoji);
                }
            });
        }
        mViewModel.getEmojis().observe(this, new ProgressObserver<List<EmojiBean.EmoticonBean>>(mContext) {
            @Override
            public void onSuccess(List<EmojiBean.EmoticonBean> result) {
                if (result != null) {
                    mEmojiDialog.setEmojiData(result, mRoomModel.isOnMp(), isEmotionSending);
                    mEmojiDialog.showAtBottom();
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            closeRoom();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void closeRoom() {
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setMessage("是否结束当前视频相亲？", com.deepsea.mua.stub.R.color.black, 15);
        dialog.setRightButton("确定", com.deepsea.mua.stub.R.color.primary_pink, (v, dialog1) -> {
            dialog1.dismiss();
            if (RoomController.getInstance().getSocketStatus() == RoomController.getInstance().DISCONNECT) {
                finish();
                return;
            }
            mViewModel.exitRoom();
        });
        dialog.setLeftButton("取消", com.deepsea.mua.stub.R.color.gray, (v, dialog1) -> {
            dialog1.dismiss();
        });
        dialog.show();
    }

    @Override
    public void finish() {
        Log.d("exit", "finish");
        super.finish();

        try {
            if (aAlertDialog != null && aAlertDialog.isShowing()) {
                aAlertDialog.dismiss();
                aAlertDialog = null;
            }
            for (int i = 0; i < mRoomModel.getMicros().size(); i++) {
                mRoomModel.getMicros().get(i).setUser(null);
            }
            mMpAdapter.setNewData(mRoomModel.getMicros());
            if (mViewModel == null) {
                mViewModel = ViewModelProviders.of(this, mFactory).get(RoomViewModel.class);
            }
            mViewModel.exitRoom();
            if (mWsLoading != null) {
                mWsLoading.dismiss();
            }
            if (mSvgaUtils != null) {
                mSvgaUtils.clear();
            }
            mBinding.roomMsgRv.release();
            RoomController.getInstance().release();
//            HeartControl.getInstance(mContext).stopHeartBeatObservable();
            System.gc();
        } catch (Exception e) {

        }

    }

    private void showMicroManageDialog(int manageSex) {
        if (mRoomModel.isOnMp()) {
            if (MatchMakerUtils.isRoomOwner()) {
                showMicroSortDialog(manageSex);
            } else {
                showAudience();
            }
        } else {
            if (mSortHandler.sortContainsMySelf()) {
                showSortCheckedDialog();
            } else {
                isInvite = false;
                isInRoomInvite = false;
                setRoleBroadcast(0, 100);
            }
        }
    }

    @Override
    protected void initListener() {
        //玩法
        subscribeClick(mBinding.playTv, o -> {
            RoomPlayDialog dialog = new RoomPlayDialog(mContext);
            dialog.setPlayIntro(mRoomData.getRoomData().getRoomDesc());
            dialog.show();
        });

        //上麦、下麦、查看麦序
        subscribeClick(mBinding.rlMicroManage, o -> {
            showMicroManageDialog(1);
        });

        //跳转守护团
        subscribeClick(mBinding.llGuardGroup, o -> {
            GuardGroupDialog groupDialog = new GuardGroupDialog(mContext, mViewModel);
            groupDialog.setOnClickListener(new GuardGroupDialog.OnClickListener() {
                @Override
                public void onClick(String userId) {
                    fetchRenew(userId);
                }
            });
            groupDialog.setMsg(String.valueOf(hongId));
            groupDialog.showAtBottom();
        });


        //礼物
        subscribeClick(mBinding.llRoomGift, o -> {
            judgeFirstRecharge(true, false, null, "0");
        });


        //发消息
        subscribeClick(mBinding.llRoomChat, o -> {
            mInputDialog = new InputTextDialog(mContext);
            mInputDialog.setOnTextSendListener(msg -> {
                if (TextUtils.isEmpty(msg))
                    return;
                if (mRoomData.isDisableMsg()) {
                    toastShort("您已被禁言，请联系管理员或房主进行解禁");
                    return;
                }
                RoomController.getInstance().sendTextMsg(msg);
            });
            mInputDialog.show();
        });
        //房主头像
        subscribeClick(mBinding.llRoomSetting, o -> {
            //普通用户
            if (mRoomData.getUserIdentity() == 0) {
                initAvatarDialog(0, mRoomData.getRoomData().getOwnerUserId());
            }
            //管理员和房主
            else {
                isRequestOwnerMsg = true;
                mViewModel.getUserInfo(mRoomData.getRoomData().getOwnerUserId());
                mOwnerDialog = new RoomOwnerDialog(mContext);
                mOwnerDialog.setOnOperateDlgListener(() -> mViewModel.lockRoom(!mRoomData.getRoomData().isRoomLock()));
            }
        });
        //在线用户
        subscribeClick(mBinding.ownerRl, o -> {
//            showOnlineDialog(true);
        });

        subscribeClick(mBinding.llRoomShare, o -> {
            mViewModel.getShareDataParam();
        });
        subscribeClick(mBinding.llRoomMusic, o -> {
            boolean hasNewData = SharedPrefrencesUtil.getData(mContext, "hasClickNewFuction", "hasClickNewFuction", false);
            if (!hasNewData) {
                //新功能引导页
                SongGuideDialog songGuideDialog = new SongGuideDialog(mContext);
                songGuideDialog.setOnClickListener(new SongGuideDialog.OnClickListener() {
                    @Override
                    public void onClickKnow() {
                        SharedPrefrencesUtil.saveData(mContext, "hasClickNewFuction", "hasClickNewFuction", true);
                        showMusicManage();
                    }
                });
                songGuideDialog.show();
                return;
            }
            showMusicManage();
        });
        subscribeClick(mBinding.llRuleClick, o -> {
            int maxLine = mBinding.tvRule.getMaxLines();
            if (maxLine == 1) {
                mBinding.tvRule.setSingleLine(false);
                mBinding.tvRule.setEllipsize(null);
                ViewBindUtils.setImageRes(mBinding.ivRule, R.drawable.icon_rule_single);
            } else {
                mBinding.tvRule.setSingleLine(true);
                mBinding.tvRule.setEllipsize(TextUtils.TruncateAt.END);
                ViewBindUtils.setImageRes(mBinding.ivRule, R.drawable.icon_rule_more);
            }
        });
        //好友
        subscribeClick(mBinding.llRoomFriend, o -> {
            ArouterUtils.build(ArouterConst.PAGE_MSG_MESSAGEMAIN).navigation();
        });
        subscribeClick(mBinding.rlRoomClose, o -> {
            AAlertDialog aAlertDialog = new AAlertDialog(mContext);
            aAlertDialog.setTitle("是否要结束直播");
            aAlertDialog.setLeftButton("否", new AAlertDialog.OnClickListener() {
                @Override
                public void onClick(View v, Dialog dialog) {
                    aAlertDialog.dismiss();

                }
            });
            aAlertDialog.setRightButton("是", new AAlertDialog.OnClickListener() {
                @Override
                public void onClick(View v, Dialog dialog) {
                    aAlertDialog.dismiss();
                    if (hongId == Integer.valueOf(UserUtils.getUser().getUid())) {
                        if (hongId == Integer.valueOf(UserUtils.getUser().getUid())) {
                            mViewModel.exitRoom();
                        } else {
                            mViewModel.downMicro(0, 100);
                        }
                    } else {
                        if (RoomController.getInstance().getSocketStatus() == RoomController.getInstance().DISCONNECT) {
                            finish();
                            return;
                        }
                        mViewModel.exitRoom();
                    }
                }
            });
            aAlertDialog.show();
        });
        subscribeClick(mBinding.llApplause, o -> {
            sendOneRose(singerUserId);
        });
        subscribeClick(mBinding.ivDiscGif, o -> {
            if (songInfo == null || !songInfo.getConsertUserId().equals(UserUtils.getUser().getUid())) {
                return;
            }
            String path = CacheUtils.getSongLrcDir(songInfo.getSongName());
            boolean isFileExist = FileUtils.isFileExist(path);
            if (!isFileExist) {
                downLoadLrcAndShow();
                return;
            }
            showLrcDialog();

        });
        subscribeClick(mBinding.llRecommend, o -> {
            mBinding.drawer.openDrawer(Gravity.RIGHT);
            mBinding.refreshLayout.autoRefresh();
        });
        subscribeClick(mBinding.llRedpackageRule, o -> {
            mViewModel.getRedPacketPlayDesc();
        });
        subscribeClick(mBinding.ivRedpackageShow, o -> {
            mViewModel.getRedPacketPlayDesc();
        });
        subscribeClick(mBinding.llRedpackageHide, o -> {
            ViewBindUtils.setVisible(mBinding.llRedpackageHide, false);
            ViewBindUtils.setVisible(mBinding.rlRedpackageShow, true);
        });
        subscribeClick(mBinding.rlReadpackageArrow, o -> {
            if (mBinding.llRedpackageHide.getVisibility() == View.VISIBLE) {
                ViewBindUtils.setVisible(mBinding.llRedpackageHide, false);
                ViewBindUtils.setVisible(mBinding.rlRedpackageShow, true);
            } else {
                ViewBindUtils.setVisible(mBinding.llRedpackageHide, true);
                ViewBindUtils.setVisible(mBinding.rlRedpackageShow, false);
            }
        });


    }

    //续费
    private void fetchRenew(String target_id) {
        mViewModel.initGuard(target_id).observe(this, new BaseObserver<RenewInitVo>() {
            @Override
            public void onSuccess(RenewInitVo result) {
                GuardRenewDialog renewCloseDialog = new GuardRenewDialog(mContext);
                renewCloseDialog.setData(result);
                renewCloseDialog.setOnClickListener(new GuardRenewDialog.OnClickListener() {
                    @Override
                    public void onClickOk(int payType, String coin, String chargeId, String guard_id, String long_day) {
                        if (payType == 0) {
                            wxpay(coin, chargeId, target_id, guard_id, long_day);
                        } else {
                            alipay(coin, chargeId, target_id, guard_id, long_day);
                        }
                    }
                });
                renewCloseDialog.show();
            }
        });

    }

    private void alipay(String coin, String chargeId, String target_id, String guard_id, String long_day) {

        mViewModel.alipay(coin, chargeId, target_id, guard_id, long_day)
                .observe(this, new ProgressObserver<String>(mContext) {
                    @Override
                    public void onSuccess(String result) {
                        RxPermissions permissions = new RxPermissions(RoomActivity.this);
                        permissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .as(autoDisposable())
                                .subscribe(aBoolean -> {
                                    if (aBoolean) {
                                        onStartAlipay(result);
                                    }
                                });
                    }
                });
    }

    private void onStartAlipay(String signature) {
        Alipay alipay = new Alipay(RoomActivity.this);
        alipay.startPay(signature);
        alipay.setAlipayListener(new Alipay.AlipayListener() {
            @Override
            public void onSuccess(PayResult result) {
                mBinding.refreshLayout.autoRefresh();
            }

            @Override
            public void onError(String msg) {
                toastShort(msg);
            }
        });
    }

    private WxpayBroadcast.WxpayReceiver mWxpayReceiver = new WxpayBroadcast.WxpayReceiver() {
        @Override
        public void onSuccess() {
            unregisterWxpayResult();
//            requestBalance();
//            MobEventUtils.onRechargeEvent(mContext, mRechargeAmount);
//            mRechargeAmount = "0";
            mBinding.refreshLayout.autoRefresh();
        }

        @Override
        public void onError() {
            unregisterWxpayResult();
        }
    };
    private WxPay mWxPay;

    private void unregisterWxpayResult() {
        if (mWxPay != null) {
            mWxPay.unregisterWxpayResult(mWxpayReceiver);
        }
    }


    private void wxpay(String coin, String chargeId, String target_id, String guard_id, String long_day) {

        mViewModel.wxpay(coin, chargeId, target_id, guard_id, long_day)
                .observe(this, new ProgressObserver<WxOrder>(mContext) {
                    @Override
                    public void onSuccess(WxOrder result) {
                        if (result != null) {
                            WxPay.WXPayBuilder builder = new WxPay.WXPayBuilder();
                            builder.setAppId(result.getAppid());
                            builder.setNonceStr(result.getNoncestr());
                            builder.setPackageValue(result.getPackageX());
                            builder.setPartnerId(result.getPartnerid());
                            builder.setPrepayId(result.getPrepayid());
                            builder.setSign(result.getSign());
                            builder.setTimeStamp(result.getTimestamp());
                            mWxPay = builder.build();
                            mWxPay.startPay(mContext);
                            mWxPay.registerWxpayResult(mWxpayReceiver);
                        }
                    }
                });
    }

    /**
     * 开始下红包雨
     */
    private void startRedRain() {

        mBinding.redPacketsView.setRedpacketCount(100);
        mBinding.redPacketsView.startRain();
        mBinding.redPacketsView.setOnRedPacketClickListener(new RedPacketView.OnRedPacketClickListener() {
            @Override
            public void onRedPacketClickListener(RedPacket redPacket) {

                mViewModel.robRedPacket();
            }
        });
    }

    /**
     * 送一朵玫瑰
     *
     * @param userId
     */
    private void sendOneRose(String userId) {
        if (TextUtils.isEmpty(userId) || userId.equals(UserUtils.getUser().getUid())) {
            return;
        }
        SingleSend sendModel = new SingleSend();
        sendModel.setCount(1);
        sendModel.setGiftId("1");
        sendModel.setId(userId);
        mViewModel.singleSend(sendModel);
    }

    private void showMusicManage() {

        RxPermissions permissions = new RxPermissions(this);

        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .as(autoDisposable())
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        if (songManagerDialog != null) {
                            songManagerDialog.setCuttentPos(appointNum > 0 ? 2 : -1);
                            songManagerDialog.show(getSupportFragmentManager(), "song");
                        } else {
                            songManagerDialog = SongManagerDialog.newInstance(mRoomId);
                            songManagerDialog.setmListener(new SongManagerDialog.OnManageListener() {
                                @Override
                                public void onLyricSetting(List<LrcRow> rows) {
                                    setLyricSetting(rows);
                                }

                                @Override
                                public void onResetLrc() {
                                    setResetLyric(false);
                                }

                                @Override
                                public void setLyricTips(String tips) {
                                    setLrcTips(tips);
                                }

                                @Override
                                public void onLyricSycn(int currentPos) {
                                    //获取歌曲播放的位置
                                    int currentPosition = currentPos;

                                    sendLyc(currentPosition);
                                }

                                @Override
                                public void onDownLoadSong(SongInfo songInfo) {
                                    startdownLoadSong(songInfo);
                                }
                            });
                            songManagerDialog.setCuttentPos(appointNum > 0 ? 2 : -1);
                            songManagerDialog.show(getSupportFragmentManager(), "song");
                        }

                    }
                });

    }


    String format = ".aac";
    private String strQutity = "高音质";
    String path = CacheUtils.getTestRecord() + format;

    int qutity = AUDIO_RECORDING_QUALITY_HIGH;
    SongManagerDialog songManagerDialog = null;

    private void downLoadLrcAndShow() {
        showProgress();
        DownloadUtils.download(songInfo.getLyricPath(), CacheUtils.getSongLrcDir(songInfo.getSongName()), new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onFinish(String path) {
                hideProgress();
//                Log.d("songDowload", "lrc-finish-" + path);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(msg_download_lrc_andshow);
                }
            }

            @Override
            public void onFail(String error) {
                hideProgress();
            }
        });
    }

    private void showLrcDialog() {
        lrcDialog = new SongLrcDialog(mContext);
        lrcDialog.setSongInfo(songInfo);
        List<LrcRow> rows = setSongLric(songInfo.getSongName());
        if (rows != null && rows.size() > 0) {
            lrcDialog.setLrcSetting(rows);
        }
        lrcDialog.showAtBottom();
    }

    private void setLrcTips(String tips) {
        mLrcView.setLoadingTipText(tips);
        mBinding.leadsingerLrcView.setText(tips);

    }

    private void setLyricSetting(List<LrcRow> rows) {
//        Log.d("songDowload", "设置歌词");
        mLrcView.setLrc(null);
        mLrcView.setLrc(rows);
    }


    private void setResetLyric(boolean isDelay) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (isDelay) {
            if (mHandler != null)
                mHandler.sendEmptyMessageDelayed(msg_lyric_reset, 3000);
        } else {
            if (mHandler != null)
                mHandler.sendEmptyMessage(msg_lyric_reset);
        }
    }

    private int sendLyRicFlag = 0;

    private void sendLyc(int currentPosition) {

        RoomActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                //滚动歌词
                if (lrcDialog != null) {
                    lrcDialog.setLrcScroll(mLrcView.getHightLightPos());
                }
                mLrcView.seekLrcToTime(currentPosition);
                LrcRow currentLrc = mLrcView.getCurrentLrc();
                Log.d("msg_send_lyric", "sendLyc" + JsonConverter.toJson(currentLrc));
                if (currentLrc != null) {
                    LrcRow copyCurrentLrc = new LrcRow();
                    LrcRow nextLrc = mLrcView.getNextLrc();
                    copyCurrentLrc.setEndTime(currentLrc.getEndTime());
                    String songContent = "";
                    if (nextLrc != null) {
                        songContent = (currentLrc.getContent() + "\n" + nextLrc.getContent());
                    } else {
                        songContent = currentLrc.getContent();
                    }
                    mBinding.leadsingerLrcView.setText(songContent);
                    copyCurrentLrc.setContent(songContent);
                    copyCurrentLrc.setSongEndTime(currentLrc.getSongEndTime());
                    copyCurrentLrc.setStartTime(currentLrc.getStartTime());
                    copyCurrentLrc.setStartTimeString(currentLrc.getStartTimeString());
//                    sendLyRicFlag = 0;
                    int max = AgoraClient.create().rtcEngine().getAudioMixingDuration();

                    copyCurrentLrc.currentTime = currentPosition;
                    copyCurrentLrc.songEndTime = max;
                    if (TextUtils.isEmpty(copyCurrentLrc.getContent())) {
                        copyCurrentLrc.content = mySongName;
                    }

                    if (currentPosition > 0) {
                        SongStateUtils.getSingleton2().setCurrentPos(currentPosition);
                    }
                    Message message = Message.obtain();
                    message.what = msg_send_lyric;
                    message.obj = JsonConverter.toJson(copyCurrentLrc);
                    if (mHandler != null) {
                        mHandler.sendMessageDelayed(message, 1500);
                    }
                } else {
//                    sendLyRicFlag++;
//                    if (sendLyRicFlag <= 30) {
                    LrcRow lrcRow = new LrcRow();
                    lrcRow.content = mySongName;
                    Message message = Message.obtain();
                    message.what = msg_send_lyric;
                    message.obj = JsonConverter.toJson(lrcRow);
                    if (mHandler != null) {
                        mHandler.sendMessageDelayed(message, 1500);
                    }
                    List<LrcRow> rows = setSongLric(mySongName);
                    if (rows != null && rows.size() > 0) {
                        setLyricSetting(rows);
                    }
//                    }
                }
            }
        });
    }


    @Override
    public void gotoCertification() {
        certification();
    }

    @Override
    public void resetInRoomInviteParam() {
        isInRoomInvite = false;
    }

    @Override
    public void onInmicroSuccessCallback() {
        isInRoomInvite = false;
        isInvite = false;
    }

    @Override
    public void share(ShareBeanItem shareBeanItem) {
        if (mRoomData.isHelpShare()) {
            List<RoomData.MicroInfosBean> data = new ArrayList<>();
            RoomData.MicroInfosBean host = mRoomModel.getHostMicro();
            List<RoomData.MicroInfosBean> microInfosBeanList = mRoomModel.getMicros();
            for (RoomData.MicroInfosBean bean : microInfosBeanList) {
                if (bean.getUser() == null) {
                    continue;
                }
                data.add(bean);
            }
            boolean hasHostMicro = false;
            for (RoomData.MicroInfosBean infosBean : data) {
                if (host.getUser() != null && infosBean.getUser().getUserId().equals(host.getUser().getUserId())) {
                    hasHostMicro = true;
                }
            }
            if (!hasHostMicro) {
                data.add(host);
            }

            NotifyHelpDialog dialog = new NotifyHelpDialog(mContext);
            dialog.setMsg(data);
            dialog.setOnClickListener(new NotifyHelpDialog.OnClickListener() {
                @Override
                public void onClick(WsUser bean) {
                    String path = shareBeanItem.getPath() + "&referrerId=" + UserUtils.getUser().getUid()
                            + "&toGetId=" + bean.getUserId() + "&referrerName=" + UserUtils.getUser().getNickname()
                            + "&toGetIdName=" + bean.getName();
                    shareBeanItem.setPath(path);
                    Log.d("shareParam", path);
                    wxShare(shareBeanItem);
                    dialog.dismiss();


                }
            });
            dialog.show();
        } else {
            wxShare(shareBeanItem);
        }


    }

    private void wxShare(ShareBeanItem shareBeanItem) {
        GlideUtils.getBitmap(shareBeanItem.getImageUrl(), this, new GlideUtils.GlideLoadBitmapCallback() {
            @Override
            public void getBitmapCallback(Bitmap bitmap) {
                UMMin umMin = new UMMin(shareBeanItem.getWebPageUrl());
//兼容低版本的网页链接
                umMin.setThumb(new UMImage(RoomActivity.this, bitmap));
// 小程序消息封面图片
                umMin.setTitle(shareBeanItem.getTitle());
// 小程序消息title
                umMin.setDescription(shareBeanItem.getTitle());
// 小程序消息描述
                Log.d("shareParam", shareBeanItem.getPath());

                umMin.setPath(shareBeanItem.getPath());
//小程序页面路径
                if (!BaseAddress.isRelease) {
                    com.umeng.socialize.Config.setMiniPreView();
                }

                umMin.setUserName(shareBeanItem.getUserName());
// 小程序原始id,在微信平台查询
                new ShareAction(RoomActivity.this)
                        .withMedia(umMin)
                        .setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {

                            }

                            @Override
                            public void onResult(SHARE_MEDIA share_media) {
//                                toastShort("分享成功啦");
                                if (mRoomData.isHelpShare()) {
                                    ShareResultDialog shareResultDialog = new ShareResultDialog(mContext);
                                    shareResultDialog.show();
                                }
                                shareCallback();
                            }

                            @Override
                            public void onError(SHARE_MEDIA share_media, Throwable t) {
                                toastShort("分享失败啦");
                                if (t != null) {
                                    Log.d("throw", "throw:" + t.getMessage());
                                }
                            }

                            @Override
                            public void onCancel(SHARE_MEDIA share_media) {
                                toastShort("分享取消了");
                            }
                        }).share();
            }
        });
    }

    private void shareCallback() {
        mViewModel.shareCallback().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {

            }
        });
    }

    SongInfo songInfo = null;

    private void startdownLoadSong(SongInfo playSongParam) {
        if (TextUtils.isEmpty(playSongParam.getSongPath())) {
            return;
        }
        songInfo = playSongParam;
        String path = CacheUtils.getSongMusicDir(songInfo.getSongName());
        boolean isFileExist = FileUtils.isFileExist(path);
        if (isFileExist) {
            if (!TextUtils.isEmpty(songInfo.getLyricPath())) {
                if (mHandler != null) {
                    Message message = Message.obtain();
                    message.what = msg_download_lrc;
                    message.obj = songInfo;
                    mHandler.sendMessage(message);
                }

            } else {
                if (mHandler != null) {
                    Message message = Message.obtain();
                    message.obj = songInfo.getSongName();
                    message.what = msg_start_play_song;
                    mHandler.sendMessage(message);
                }
            }
            return;
        }
        if (mHandler != null) {
            mHandler.sendEmptyMessage(msg_start_download_song);
        }
    }

    private void downLoad() {
        DownloadUtils.download(songInfo.getSongPath(), CacheUtils.getSongMusicDir(songInfo.getSongName()), new DownloadListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onProgress(long total, long current) {
//                Log.d("songDowload", "song-" + total + "-" + current);
            }

            @Override
            public void onFinish(String path) {
//                Log.d("songDowload", "song-finish-" + path);
                if (mHandler != null) {
                    if (!TextUtils.isEmpty(songInfo.getLyricPath())) {
                        Message message = Message.obtain();
                        message.what = msg_download_lrc;
                        message.obj = songInfo;
                        mHandler.sendMessage(message);
                    } else {
                        Message message = Message.obtain();
                        message.obj = songInfo.getSongName();
                        message.what = msg_start_play_song;
                        mHandler.sendMessage(message);
                        mHandler.sendEmptyMessage(msg_download_song_success);

                    }
//                    Log.d("AG_EX_AV", "downLoadSong over：" + path);
                }
            }

            @Override
            public void onFail(String error) {
//                Log.d("AG_EX_AV", "downLoadSong error：" + error);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(msg_download_song_fails);
                }
                hideProgress();
            }
        });
    }

    String songSinger;

    private void downLoadSongLrc(SongInfo playSongParam) {
        if (TextUtils.isEmpty(playSongParam.getLyricPath())) {
            return;
        }
        SongInfo songInfo = playSongParam;
        songSinger = songInfo.getSingerName();
        String path = CacheUtils.getSongLrcDir(songInfo.getSongName());
        boolean isFileExist = FileUtils.isFileExist(path);
        if (isFileExist && mHandler != null) {
            Message message = Message.obtain();
            message.obj = songInfo.getSongName();
            message.what = msg_start_play_song_with_lrc;
            mHandler.sendMessage(message);
            return;
        }
        DownloadUtils.download(songInfo.getLyricPath(), CacheUtils.getSongLrcDir(songInfo.getSongName()), new DownloadListener() {
            @Override
            public void onStart() {
//                Log.d("songDowload", "lrc-start");
            }

            @Override
            public void onProgress(long total, long current) {
//                Log.d("songDowload", "lrc-" + total + "-" + current);

            }

            @Override
            public void onFinish(String path) {
//                Log.d("songDowload", "lrc-finish-" + path);
                if (mHandler != null) {

                    Message message = Message.obtain();
                    message.obj = songInfo.getSongName();
                    message.what = msg_start_play_song_with_lrc;
                    mHandler.sendMessage(message);
//                    Log.d("AG_EX_AV", "downLoadSong over：" + path);
                    mHandler.sendEmptyMessage(msg_download_song_success);
                }
            }

            @Override
            public void onFail(String error) {
//                Log.d("AG_EX_AV", "downLoadSong error：" + error);
                if (mHandler != null) {
                    mHandler.sendEmptyMessage(msg_download_song_fails);

                }
            }
        });

    }

    private void starPlaySong(String songName) {
        if (!TextUtils.isEmpty(songName)) {
            ViewBindUtils.setText(mBinding.tvMusicSongName, songName);
        }
        if (!TextUtils.isEmpty(songSinger)) {
            ViewBindUtils.setText(mBinding.tvMusicSongSinger, "/演唱者：" + songSinger);
        }
//        isSendSong = true;
        if (mBinding.leadsingerLrcView.getVisibility() == View.INVISIBLE) {
            if (mHandler != null)
                mHandler.sendEmptyMessage(msg_lyric_show);
        }

        int loopCount = 1; //无限循环播放混音文件；设置为正整数表示混音文件播放的次数
        boolean shouldLoop = false; ////文件音频流是否发送给对端；如果设置为 true，文件音频流仅在本地可以听见，不会发送到对端
        boolean replaceMic = false; //不替换麦克风采集的音频
        AgoraClient.create().rtcEngine().startAudioMixing(CacheUtils.getSongMusicDir(songName), shouldLoop, replaceMic, loopCount);
        int pos = SongStateUtils.getSingleton2().getCurrentPos();
        if (pos != 0) {
            AgoraClient.create().rtcEngine().setAudioMixingPosition(pos);
        }
        if (!TextUtils.isEmpty(SongStateUtils.getSingleton2().getConsertUserId())) {
            int localVoiceReverbPreset = SongStateUtils.getSingleton2().getSelectEffect();
            List<LocalVoiceReverbPresetVo> voiceReverbPresetVoList = SongStateUtils.getSingleton2().getReverbPresetVoList();
            if (voiceReverbPresetVoList != null && voiceReverbPresetVoList.size() > 0) {
                LocalVoiceReverbPresetVo vo = voiceReverbPresetVoList.get(localVoiceReverbPreset - 1);
                AgoraClient.create().rtcEngine().setLocalVoiceReverb(AUDIO_REVERB_ROOM_SIZE, vo.getRoomSize());
                AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_DELAY, vo.getWetDelay());
                AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_STRENGTH, vo.getStrength());
                AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_LEVEL, vo.getWetLevel());
                AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_DRY_LEVEL, vo.getDryLevel());

            }
        }
        int volume = SongStateUtils.getSingleton2().getVolume();
        AgoraClient.create().rtcEngine().adjustAudioMixingVolume(volume);
        AgoraClient.create().rtcEngine().adjustRecordingSignalVolume(400);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        mTimer = new Timer();
        mTask = new LrcTask();
        mTimer.scheduleAtFixedRate(mTask, 0, mPlayerTimerDuration);


        setLrcTips(songName);
        mySongName = songName;
        SongStateUtils.getSingleton2().setConnect(false);

    }

    private String mySongName = "";

    private List<LrcRow> setSongLric(String songname) {
        //从assets目录下读取歌词文件内容
        String path = CacheUtils.getSongLrcDir(songname);
//        Log.d("lrcSong", "path:" + path);
        String lrc = getFromAssets(path);
//        Log.d("lrcSong", "lrc" + lrc);
        //解析歌词构造器
        ILrcBuilder builder = new DefaultLrcBuilder();
        //解析歌词返回LrcRow集合
        List<LrcRow> rows = builder.getLrcRows(lrc);
        Log.d("AG_EX_AV", "歌词：" + JsonConverter.toJson(rows));

        return rows;
    }

    @Override
    public void startPlaySong(SongInfo songInfo) {
        startdownLoadSong(songInfo);
    }

    @Override
    public void syncPlaySongVolume(int volume) {
        AgoraClient.create().rtcEngine().adjustAudioMixingVolume(volume);
    }

    @Override
    public void syncPlaySongPause(int isPause, int consertUserId) {
        String uid = UserUtils.getUser().getUid();
        try {
            SongStateUtils.getSingleton2().setSongState(isPause);
            boolean isSinger = (consertUserId == Integer.valueOf(uid));
            if (isPause == 1) {
                if (isSinger) {
                    AgoraClient.create().rtcEngine().pauseAudioMixing();
                }
                ViewBindUtils.setImageRes(mBinding.ivDiscGif, R.drawable.icon_music_diange_gif_bg);
            } else if (isPause == 2) {
                if (isSinger) {
                    AgoraClient.create().rtcEngine().resumeAudioMixing();
                }
                GlideUtils.loadGif(mBinding.ivDiscGif, R.drawable.icon_music_diange_gif_bg);
            } else {
                ViewBindUtils.setImageRes(mBinding.ivDiscGif, R.drawable.icon_music_diange_gif_bg);
            }
            boolean isCloseMicro = (consertUserId != 0 && mRoomModel.isOnMp() && consertUserId != Integer.valueOf(uid) && isPause == 2);
            Log.d("AG_EX_AV", "isCloseMicro:" + (consertUserId != 0) + ":" + mRoomModel.isOnMp() + ";" + (consertUserId != Integer.valueOf(uid)) + ";" + (isPause == 2) + ";" + "isPause:" + isPause);
            int result = AgoraClient.create().muteLocalAudioStream(isCloseMicro);
            if (result == 0) {
                ForbiddenStateUtils.saveForbiddenMicState(uid, isCloseMicro);
            }
        } catch (Exception e) {
            Log.d("Exception", e.toString());
            int result = AgoraClient.create().muteLocalAudioStream(false);
            if (result == 0) {
                ForbiddenStateUtils.saveForbiddenMicState(uid, false);
            }
        }

    }

//    boolean isSendSong = true;

    @Override
    public void syncPlaySongRePlay() {
        AgoraClient.create().rtcEngine().setAudioMixingPosition(0);
        mBinding.lrcView.seekLrcToTime(0);
    }

    // 0为正常切歌（房主手动切的），1为下麦自动切歌，2为下载失败自动切歌，3为点歌者玫瑰不足自动切歌
    @Override
    public void syncPlaySongCutSong(int state, int consertUserId) {
        songInfo = null;
        if (lrcDialog != null) {
            lrcDialog.clearData();
        }
        SongStateUtils.getSingleton2().setCurrentPos(0);
//        isSendSong = false;
        AgoraClient.create().rtcEngine().stopAudioMixing();
        AgoraClient.create().rtcEngine().setLocalVoiceReverb(AUDIO_REVERB_ROOM_SIZE, 0);
        AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_DELAY, 0);
        AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_STRENGTH, 0);
        AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_LEVEL, 0);
        AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_DRY_LEVEL, 0);
        AgoraClient.create().rtcEngine().setLocalVoiceReverbPreset(AUDIO_REVERB_OFF);
//        AgoraClient.create().rtcEngine().setLocalVoiceReverb(AUDIO_REVERB_OFF);
//        Log.d("enableInEarMonitoring","false");
//        AgoraClient.create().rtcEngine().enableInEarMonitoring(false);
        setResetLyric(true);
        if (state == 0) {
            if (UserUtils.getUser().getUid().equals(String.valueOf(consertUserId))) {
                SongChooseAlertDialog chooseAlertDialog = new SongChooseAlertDialog(mContext);
//                chooseAlertDialog.setMsg("XX预约了歌曲，点击底部“KTV”图标， 进入“正在播放”开始播放点歌");
                chooseAlertDialog.show();
            }

        } else if (state == 1) {
            showCutSongDialog("演唱者走神啦，自动播放下一首歌");
        } else if (state == 2) {
            showCutSongDialog("网络走神啦，稍后下载，自动播放下一首歌 ");

        } else if (state == 3) {
            showCutSongDialog("点歌人玫瑰不足， 歌曲被取消");
        }
    }

    @Override
    public void onReconnectSuccess() {
        int pos = SongStateUtils.getSingleton2().getCurrentPos();
        Log.d("reconnect", pos + "");
        SongStateUtils.getSingleton2().setConnect(true);
        if (pos != 0) {
            mViewModel.sysnSongPause(true);
        }

        showAlert(1, "已重新连接聊天室");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMpAdapter != null) {
                    mMpAdapter.notifyDataSetChanged();
                }
                if (mJoinRoom.getRoomMode() == 9) {
                    if (!TextUtils.isEmpty(SongStateUtils.getSingleton2().getConsertUserId())) {
                        mBinding.mainSecondTwoMusicView.setMicroData(mainMpInfo);
                    } else {
                        mBinding.mainSecondOneView.setMicroData(mainMpInfo);
                    }
                } else {
                    if (!TextUtils.isEmpty(SongStateUtils.getSingleton2().getConsertUserId())) {
                        mainFaceMusicView.setMicroData(mainMpInfo);
                    } else {
                        mainFaceView.setMicroData(mainMpInfo);
                    }
                }

            }
        }, 1000);
    }

    @Override
    public void syncSongStartDwonLoad() {
        downLoad();
    }

    @Override
    public void syncSongSuccessDwonLoad(int success) {
        if (success == 1) {
            starPlaySong(songName);
        } else if (success == 2) {
            showCutSongDialog("找不到歌曲");
            AgoraClient.create().rtcEngine().stopAudioMixing();
        } else if (success == 3) {
            showCutSongDialog("玫瑰不足");

        } else if (success == 4) {
            showCutSongDialog("已经被切歌了");

        }

    }

    boolean isChangeView = false;
    private String singerUserId = "";

    @Override
    public void changeMicroView(SongInfo songInfo) {
        this.songInfo = songInfo;
        boolean isChangeForban = false;

        if (songInfo != null && !TextUtils.isEmpty(songInfo.getConsertUserName())) {
            //切换伴唱
            isChangeForban = (!SongStateUtils.getSingleton2().getConsertUserId().equals(songInfo.getConsertUserId()));
            SongStateUtils.getSingleton2().setConsertUserId(songInfo.getConsertUserId());
            isChangeView = true;
            if (!TextUtils.isEmpty(songInfo.getSongName())) {
                ViewBindUtils.setText(mBinding.tvMusicSongName, songInfo.getSongName().trim());
            }
            if (!TextUtils.isEmpty(songInfo.getSingerName())) {
                ViewBindUtils.setText(mBinding.tvMusicSongSinger, "/演唱者：" + songInfo.getSingerName());
            }
            if (songInfo.getConsertUserId().equals(UserUtils.getUser().getUid())) {
                SongChooseAlertDialog songChooseAlertDialog = new SongChooseAlertDialog(mContext);
                songChooseAlertDialog.setMsg(songInfo.getDemandUserName() + "给你点了首歌，戴上耳机\n" +
                        "演唱效果会更好~");
            }

        } else {
            if (lrcDialog != null && lrcDialog.isShowing()) {
                lrcDialog.dismiss();
                lrcDialog = null;
            }
            SongStateUtils.getSingleton2().setConsertUserId("");

            //切换非伴唱
            isChangeView = false;

            ViewBindUtils.setText(mBinding.tvMusicSongName, "");

            ViewBindUtils.setText(mBinding.tvMusicSongSinger, "");
        }

        if (SongStateUtils.getSingleton2().isChangeView() != isChangeView || isChangeForban) {
            List<RoomData.MicroInfosBean> data = mMpAdapter.getData();
            if (mMpAdapter != null && data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    data.get(i).setRelease(true);
                }
                mMpAdapter.notifyDataSetChanged();
            }

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeSongOperate(songInfo);
                }
            }, 200);
        } else {
            SongStateUtils.getSingleton2().setChangeView(isChangeView);

        }


    }

    @Override
    public void updateSongRankParam(int rank) {
        ViewBindUtils.setText(mBinding.tvApplauseNum, "X" + rank);
    }

    private int appointNum;

    @Override
    public void updateSongAppointmentNum(int num) {
        if (appointNum == num) {
            return;
        }
        appointNum = num;
//        ViewBindUtils.setText(mBinding.tvAppointmentNum, num > 0 ? "预约:" + num : "");
        if (num > 0) {
            ViewBindUtils.setVisible(mBinding.ivSongHint, true);
            GlideUtils.loadGif(mBinding.ivSongHint, R.drawable.icon_hint_add_a_song);
        } else {
            ViewBindUtils.setVisible(mBinding.ivSongHint, false);

        }
//        ViewBindUtils.setVisible(mBinding.ivRoomMusic, num == 0);

    }

    @Override
    public void notifyAddFriend(String uid) {
        FriendInfoBean bean = new FriendInfoBean();
        bean.setUser_id(uid);
        FriendsUtils.getInstance().addFriend(bean);
        int pos = mMpAdapter.getItemPosForUid(uid);
        if (pos != -1) {
            RoomData.MicroInfosBean microInfosBean = mMpAdapter.getData().get(pos);
            mMpAdapter.updateFriendState(pos, microInfosBean);
        }
        if (mJoinRoom.getRoomMode() != 9) {
            if (isChangeView) {
                mainFaceMusicView.updateFriendState(mainMpInfo);
            } else {
                mainFaceView.updateFriendState(mainMpInfo);
            }
        }
    }

    @Override
    public void receiveLrc(String lrc) {
        if (songInfo != null && songInfo.getConsertUserId().equals(UserUtils.getUser().getUid())) {
            return;
        }
        LrcRow lrcRow = JsonConverter.fromJson(lrc, LrcRow.class);
        Message message = Message.obtain();
        message.what = msg_lyric_receive;
        message.obj = lrcRow;
        if (mHandler != null)
            mHandler.sendMessage(message);
        if (mBinding.audienceLrcView.getVisibility() == View.INVISIBLE) {
            if (mHandler != null)
                mHandler.sendEmptyMessage(msg_lyric_show);
        }
        int pos = (int) lrcRow.currentTime;
        int max = (int) lrcRow.songEndTime;
        EventBus.getDefault().post(new SyncSongProgressEvent(pos, max));
    }

    SongSingingAlertDialog singingAlertDialog = null;

    @Override
    public void syncDemandSong(String name) {
        if (singingAlertDialog == null) {
            singingAlertDialog = new SongSingingAlertDialog(mContext);
        }
        singingAlertDialog.setMsg(String.format("%s预约了歌曲，点击底部“KTV”图标,进入“正在播放”开始播放点歌", name));
        singingAlertDialog.show();

    }

    @Override
    public void syncMicroRose(SyncMicroRose syncMicroRose) {
        int userLevel = syncMicroRose.getLevel();
        int userNumber = syncMicroRose.getNumber();

        int pos = mMpAdapter.getItemPosForMany(userLevel, userNumber);
        Log.d("AG_EX_AV", pos + ":pos");

        if (pos != -1) {
            mMpAdapter.getData().get(pos).setRolse(syncMicroRose.getRose());
            mMpAdapter.getData().get(pos).setRoseRanks(syncMicroRose.getRoseRanks());
            mMpAdapter.notifyItemChanged(pos, IMicroEvent.UpdateRanks);
        }
        boolean flag = mainMpInfo != null && mainMpInfo.getType() == userLevel && mainMpInfo.getNumber() == userNumber;
        Log.d("AG_EX_AV", flag + "");
        Log.d("AG_EX_AV", "userLevel" + userLevel + "userNumber:" + userNumber);
        Log.d("AG_EX_AV", "userLevel-mp" + mainMpInfo.getType() + "userNumber-mp:" + mainMpInfo.getNumber());

        if (mainMpInfo != null && mainMpInfo.getType() == userLevel && mainMpInfo.getNumber() == userNumber) {
            mainMpInfo.setRolse(syncMicroRose.getRose());
            mainMpInfo.setRoseRanks(syncMicroRose.getRoseRanks());
            if (mJoinRoom.getRoomMode() != 9) {

                if (!isChangeView) {
                    mBinding.mainFaceView.updateRankHeads(syncMicroRose.getRoseRanks());
                } else {
                    mBinding.mainFaceMusicView.updateRankHeads(syncMicroRose.getRoseRanks());
                }
            } else {
                if (!isChangeView) {
                    Log.d("AG_EX_AV", "设置heads");
                    mBinding.mainSecondOneView.updateRankHeads(syncMicroRose.getRoseRanks());
                } else {
                    mBinding.mainSecondTwoMusicView.updateRankHeads(syncMicroRose.getRoseRanks());
                }
            }
        }


    }

    private void updateMicroData(SyncMicroRose data) {
        RoomData.MicroInfosBean hostMicro = mRoomModel.getHostMicro();
        if (data.getLevel() == hostMicro.getType() && data.getNumber() == hostMicro.getNumber()) {
            hostMicro.setRoseRanks(data.getRoseRanks());
            return;
        }
        for (RoomData.MicroInfosBean bean : mRoomModel.getMicros()) {
            if (bean.getType() == data.getLevel() && bean.getNumber() == data.getNumber()) {
                bean.setRoseRanks(data.getRoseRanks());
                return;
            }
        }


    }

    @Override
    public void updateExclusiveHint(String msg) {
        aAlertDialog = new AAlertDialog(mContext);
        aAlertDialog.setMessage(msg, com.deepsea.mua.stub.R.color.black, 15);
        aAlertDialog.setButton("确定", com.deepsea.mua.stub.R.color.gray, null);
        aAlertDialog.setIsAutoDismiss(true);
        aAlertDialog.show();
    }

    @Override
    public void updateWheatCards(int cardNum) {
        mRoomData.setWheatCardCount(cardNum);
        if (cardNum > 0) {
            mBinding.tvMicroManageCost.setText("剩余上麦卡x" + cardNum);
        }
    }


    private void changeSongOperate(SongInfo songInfo) {
        List<RoomData.MicroInfosBean> data = mMpAdapter.getData();
        if (mMpAdapter != null && data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setRelease(false);
            }
        }
        if (mJoinRoom.getRoomMode() != 9) {

            if (songInfo != null && !TextUtils.isEmpty(songInfo.getConsertUserName())) {
                //切换伴唱
                singerUserId = songInfo.getConsertUserId();
                ViewBindUtils.setVisible(mBinding.cnsMicrosCommonViews, false);
                ViewBindUtils.setVisible(mBinding.rlMicrosMusicViews, true);
                ViewBindUtils.setVisible(mBinding.rlMusic, true);
                mBinding.mainFaceView.setSurfaceViewVisible();
                mBinding.mainFaceView.setVisibility(View.GONE);
                mBinding.rlMainFaceView.setVisibility(View.GONE);
                mBinding.rlGroupCommonRv.setVisibility(View.GONE);
            } else {
                //切换非伴唱
                singerUserId = "";

                mBinding.mainFaceView.setVisibility(View.VISIBLE);
                mBinding.rlMainFaceView.setVisibility(View.VISIBLE);
                ViewBindUtils.setVisible(mBinding.cnsMicrosCommonViews, true);
                ViewBindUtils.setVisible(mBinding.rlMicrosMusicViews, false);
                ViewBindUtils.setVisible(mBinding.rlMusic, false);
                mBinding.mainFaceMusicView.setSurfaceViewVisible();
                mBinding.rlGroupCommonRv.setVisibility(View.VISIBLE);
            }
        } else {
            if (songInfo != null && !TextUtils.isEmpty(songInfo.getConsertUserName())) {
                singerUserId = songInfo.getConsertUserId();
                mBinding.cnsSecondViews.setVisibility(View.GONE);
                mBinding.cnsSecondMusicViews.setVisibility(View.VISIBLE);
                mBinding.mainSecondOneView.setVisibility(View.GONE);
                mBinding.mainSecondOneView.setSurfaceViewVisible();

                mBinding.mainSecondTwoMusicView.setVisibility(View.VISIBLE);
                ViewBindUtils.setVisible(mBinding.rlMusic, true);
            } else {
                singerUserId = "";
                mBinding.cnsSecondViews.setVisibility(View.VISIBLE);
                mBinding.cnsSecondMusicViews.setVisibility(View.GONE);
                mBinding.mainSecondOneView.setVisibility(View.VISIBLE);

                mBinding.mainSecondTwoMusicView.setVisibility(View.GONE);
                mBinding.mainSecondTwoMusicView.setSurfaceViewVisible();

                ViewBindUtils.setVisible(mBinding.rlMusic, false);
            }

        }
        initCommonMp();
        initMicro();
        SongStateUtils.getSingleton2().setChangeView(isChangeView);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mMpAdapter.notifyDataSetChanged();
                if (mJoinRoom.getRoomMode() != 9) {
                    if (!TextUtils.isEmpty(SongStateUtils.getSingleton2().getConsertUserId())) {
                        mainFaceMusicView.setMicroData(mainMpInfo);
                    } else {
                        mainFaceView.setMicroData(mainMpInfo);
                    }
                } else {
                    if (!TextUtils.isEmpty(SongStateUtils.getSingleton2().getConsertUserId())) {
                        mBinding.mainSecondTwoMusicView.setMicroData(mainMpInfo);
                    } else {
                        mBinding.mainSecondOneView.setMicroData(mainMpInfo);
                    }
                }
            }
        }, 1000);

    }

    SongAlertDialog dialog;

    private void showCutSongDialog(String alert) {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
            }
            dialog = new SongAlertDialog(mContext);
            dialog.setMsg(alert);
            dialog.showAtBottom();
        } catch (Exception e) {
        }
    }

    private void certification() {
        Context context = ActivityCache.getInstance().getTopActivity();
        AAlertDialog dialog = new AAlertDialog(context);
        dialog.setTitle("实名认证");
        dialog.setMessage("实名认证信息受到用户隐私条款保护，不会向第三方透露。");
        dialog.setLeftButton("去认证", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                dialog.dismiss();
                fetchCertification();
            }
        });
        dialog.setRightButton("跳过", new AAlertDialog.OnClickListener() {
            @Override
            public void onClick(View v, Dialog dialog) {
                if (microType == 1) {
                    mViewModel.agreeInviteMp(true, true);
                } else if (microType == 2) {
                    mViewModel.agreeInviteMpOutRoom(true, inviteMicroId, true);
                } else if (microType == 3) {
                    mViewModel.upMicro(upMicroType, upMicroNum, true);
                } else if (microType == 4) {
                    mViewModel.toOnWheat(toOnWheatUid, true);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void fetchCertification() {
        RxPermissions permissions = new RxPermissions(this);
        Disposable subscribe = permissions.request(Manifest.permission.CAMERA)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        getVerifyToken();
                    }
                });
    }

    private void createApprove() {
        mViewModel.createapprove().observe(this, new BaseObserver<BaseApiResult>() {
            @Override
            public void onSuccess(BaseApiResult result) {
                toastShort(result.getDesc());
                if (result.getCode() == 200) {
                    if (isInvite) {
                        inviteUpMicro(inviteMicroId);
                    } else if (isInRoomInvite) {
                        showMpInvited();
                    }
                }
            }

            @Override
            public void onError(String msg, int code) {
                super.onError(msg, code);
                if (code == 2006) {
                    AuthenticationAlertDialog dialog = new AuthenticationAlertDialog(mContext);
                    dialog.setContent(msg);
                    dialog.show();
                } else {
                    toastShort(msg);
                }
            }
        });
    }

    /**
     * 实人认证
     */
    private void getVerifyToken() {
        mViewModel.getVerifyToken().observe(this, new BaseObserver<AuditBean>() {
            @Override
            public void onSuccess(AuditBean result) {
                if (result == null)
                    return;
                AuditHelper.start(result.getToken(), mContext, new RPAuditListener() {
                    @Override
                    public void onAuditPass() {
                        createApprove();

                    }

                    @Override
                    public void onAuditNot(String code) {
                        super.onAuditNot(code);
                        switch (code) {
                            case "-1":
                                ToastUtils.showToast("退出认证");
                                break;
                            case "3001":
                                ToastUtils.showToast("认证token无效或已过期");
                                break;
                            case "3101":
                                ToastUtils.showToast("用户姓名身份证实名校验不匹配");
                                break;
                            case "3102":
                                ToastUtils.showToast("实名校验身份证号不存在");
                                break;
                            case "3103":
                                ToastUtils.showToast("实名校验身份证号不合法");
                                break;
                            case "3104":
                                ToastUtils.showToast("认证已通过，重复提交");
                                break;
                            case "3204":
                            case "3206":
                                ToastUtils.showToast("非本人操作");
                                break;
                            case "3208":
                                ToastUtils.showToast("公安网无底照");
                                break;
                        }
                    }
                });
            }
        });
    }

    private int selectMicManagerSex = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constant.BALANCE_CODE) {
                String extra = data.getStringExtra(Constant.BALANCE);
                boolean rechargeWelfare = data.getBooleanExtra(Constant.RECHARGE_WELFARE, false);
                boolean rechargeSuccess = data.getBooleanExtra(Constant.RECHARGE_SUCCESS, false);
                if (mRoomData != null) {
                    mRoomData.setBalance(FormatUtils.removeDot(extra));
                    if (isInvite) {
                        inviteUpMicro(inviteMicroId);
                    } else if (isInRoomInvite) {
                        showMpInvited();
                    }
                }
                if (rechargeWelfare) {
                    showGiftDialog(false, "0", null);
                }
                if (rechargeSuccess) {
                    if (!TextUtils.isEmpty(extra) && !extra.equals("0")) {
                        mRoomData.setFirstCharge(false);
                        getBanners();
                    }
                }
            } else if (requestCode == Constant.RequestCode.RQ_addFriend) {
                Log.d("resultCode", "resultCode");
                if (data == null) {
                    return;
                }
                String uid = data.getStringExtra("uid");
                if (TextUtils.isEmpty(uid)) {
                    return;
                }
                notifyAddFriend(uid);
                if (mViewModel != null) {
                    mViewModel.notifyAddFriend(uid);
                }

            } else if (requestCode == Constant.RequestCode.RQ_TASK_CODE) {
                boolean isGotoTask = data.getBooleanExtra("isGotoTask", false);
                String taskId = data.getStringExtra("taskId");
                if (TextUtils.isEmpty(taskId)) {
                    return;
                }
                if (isGotoTask && !taskId.equals("17") || !taskId.equals("11") || !taskId.equals("18")) {
                    mViewModel.exitRoom();
                }
                if (taskId.equals("17")) {
                    mViewModel.getShareDataParam();
                }
            }
        }
    }

    @Override
    public void onSvgaStart(ReceivePresent present) {
        //麦位动画
        List<ReceivePresent.GiveGiftDatasBean> datas = present.getGiveGiftDatas();
        if (datas != null) {
            for (ReceivePresent.GiveGiftDatasBean giveBean : datas) {
                ReceivePresent.GiveGiftDatasBean.MicroBean micro = giveBean.getMicro();
                if (micro == null) {
                    continue;
                }
                String animation = present.getGiftData() != null ? present.getGiftData().getAnimation() : "";

                if (mainMpInfo != null && micro.getLevel() == mainMpInfo.getType()) {
                    initMainAnim(animation);
                    continue;
                }
                int itemPos = -1;
                for (int i = 0; i < mMpAdapter.getData().size(); i++) {
                    if (micro.getLevel() == mMpAdapter.getData().get(i).getType()) {
                        itemPos = i;
                    }
                }
                if (itemPos != -1) {
                    mMpAdapter.onGiftAnima(itemPos, animation);
                }

            }
        }
    }

    private void initSecondMainMp(int level, String gifUrl) {
        if (!isChangeView) {
            RoomData.MicroInfosBean hostMp = mRoomModel.getHostMicro();
            RoomData.MicroInfosBean guestMp = mRoomModel.getMicros().get(0);
            if (hostMp != null && level == hostMp.getType()) {
                mBinding.animSecondOneIv.loadNormalAnim(gifUrl);
            }
            if (guestMp != null && level == guestMp.getType()) {
//                mBinding.animSecondTwoIv.loadNormalAnim(gifUrl);
            }
        } else {
            RoomData.MicroInfosBean singerInfo = null;
            RoomData.MicroInfosBean guestInfo = null;
            WsUser hostUser = mRoomModel.getHostMicro().getUser();
            Log.d("music", SongStateUtils.getSingleton2().getConsertUserId() + songInfo.getConsertUserId() + singerUserId + ";" + hostUser.getUserId());
            if (SongStateUtils.getSingleton2().getConsertUserId().equals(hostUser.getUserId())) {
                singerInfo = mRoomModel.getHostMicro();
                guestInfo = mRoomModel.getMicros().get(0);
            } else {
                guestInfo = mRoomModel.getHostMicro();
                singerInfo = mRoomModel.getMicros().get(0);
            }
            if (singerInfo != null && singerInfo.getType() == level) {
                mBinding.animSecondTwoMusicIv.loadNormalAnim(gifUrl);
            }

        }

    }

    /*-------------------------------------------------------------------------------------*/

    @Override
    public void onHyphenateMessage(ReceiveMessage message) {
        if (!TextUtils.isEmpty(message.getEmojianim())) {
            String userId = message.getUser().getUserId();

            RoomData.MicroInfosBean hostMicro = mRoomModel.getHostMicro();
            if (hostMicro.getUser() != null && hostMicro.getUser().getUserId().equals(userId)) {
                initMainAnim(message.getEmojianim());
                return;
            }

            for (int i = 0; i < mMpAdapter.getData().size(); i++) {
                RoomData.MicroInfosBean item = mMpAdapter.getItem(i);
                if (item.getUser() != null && item.getUser().getUserId().equals(userId)) {
                    mMpAdapter.onNormalEmojiAnima(i, message.getEmojianim());
                    break;
                }
            }
        }
    }

    @Override
    public void onNewMessage(int unreadCount) {
//        ViewBindUtils.setVisible(mBinding.unreadMsg, unreadCount > 0);
    }

    @Override
    public void onClientRoleChanged(int oldRole, int newRole) {
        //主播
        if (newRole == Constants.CLIENT_ROLE_BROADCASTER) {
            setMicroSort();
        }
        //观众
        else if (newRole == Constants.CLIENT_ROLE_AUDIENCE) {
//            mBinding.mpIntroTv.setText(getString(R.string.apply_face));
//            mBinding.tvMicro.setText(getString(R.string.apply_face));
//            mBinding.muteIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onAudioVolumeIndication(List<String> uids) {
        //普通麦
        if (mMpAdapter == null || mMpAdapter.getData() == null) {
            return;
        }
        List<RoomData.MicroInfosBean> data = mMpAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            RoomData.MicroInfosBean bean = data.get(i);
            if (bean.getUser() != null && uids.contains(bean.getUser().getUserId())) {
                mMpAdapter.onVoiceWave(i);
            }
        }
    }

    @Override
    public void onOpen() {
        isEmotionSending = false;
    }

    @Override
    public void onMessage(String text) {
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(text).getAsJsonObject();
        int msgId = object.get("MsgId").getAsInt();
        if (mWsLoading != null && mWsLoading.isShowing()) {
            if (mWsLoading.tag == msgId) {
                mWsLoading.dismiss();
            }
        }
    }

    private long initMicroTimestr = 0;
    boolean isOnReconnected = false;

    @Override
    public void onReconnected() {
        long nowDate = new Date().getTime();
        if (initMicroTimestr != 0 && (nowDate - initMicroTimestr) < 3000) {
            initMicroTimestr = nowDate;
            return;
        }
        initMicroTimestr = nowDate;
        SongStateUtils.getSingleton2().setChangeView(false);
//        mViewModel.getPlaySongParam();
        isOnReconnected = true;
        initView();

//                initRank();
//                initBottom();
    }

    @Override
    public void onMsgClick(String uid) {
        initAvatarDialog(0, uid);
    }

    @Override
    public void onMsgLongClick(String uName) {

        mInputDialog = new InputTextDialog(mContext);
        mInputDialog.setMsg("@" + uName + "  ");
        mInputDialog.setOnTextSendListener(msg -> {
            if (TextUtils.isEmpty(msg))
                return;
            if (mRoomData.isDisableMsg()) {
                toastShort("您已被禁言，请联系管理员或房主进行解禁");
                return;
            }
            RoomController.getInstance().sendTextMsg(msg);
        });
        mInputDialog.show();
    }


    @Override
    public void onAddMsg(List<RoomMsgBean> list) {
        if (list == null)
            return;
        mBinding.roomMsgRv.sendMultiMsg(list);
    }

    @Override
    public void onVisitorNum(String visitorNum) {
//        mBinding.popularityTv.setText(visitorNum);
    }

    @Override
    public void onUpMicro(UpMicroMsg bean) {
        RoomData.MicroInfosBean updateBean = bean.getMicroInfo();
        InMicroMemberUtils.getInstance().saveMicroMembers(String.valueOf(updateBean.getType()), updateBean.getUser().getUserId());
        if (updateBean.getType() == 0) {
            initMainMp(updateBean);
        } else {
            mMpAdapter.onUpdateMicro(updateBean);
            int pos = mMpAdapter.getItemPos(updateBean.getType(), updateBean.getNumber());

            if (mMpAdapter.getData().get(pos).getUser() == null) {
                RoomData.MicroInfosBean infosBean = mMpAdapter.getData().get(pos);
                infosBean.setUser(updateBean.getUser());
                infosBean.setNumber(updateBean.getNumber());
                infosBean.setType(updateBean.getType());
                infosBean.setXinDongZhi(updateBean.getXinDongZhi());
                infosBean.setResultUrl(updateBean.getResultUrl());
                infosBean.setIsLocked(updateBean.isIsLocked());
                infosBean.setResultUrl(updateBean.getResultUrl());
                infosBean.setDaojishiShijiandian(updateBean.getDaojishiShijiandian());
                infosBean.setDaojishiShichang(updateBean.getDaojishiShichang());
            }
        }
        if (isChangeView) {
//                SongStateUtils.getSingleton2().setChangeView(!isChangeView);
            mViewModel.getPlaySongParam();
        }

        EventBus.getDefault().post(new UpdateInRoomMemberEvent());

    }

    @Override
    public void onDownMicro(DownMicroMsg bean) {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        if (mJoinRoom.getRoomMode() != 9 && bean.getLevel() == 0) {
            initMainMp(mRoomModel.getHostMicro());
        }
        if (mMpAdapter != null) {
            int itemPos = mMpAdapter.getItemPos(bean.getLevel(), bean.getNumber());
            if (bean.getLevel() == mMpAdapter.getData().get(itemPos).getType()) {
                InMicroMemberUtils.getInstance().removeMicroMember(String.valueOf(bean.getLevel()));
                mMpAdapter.getData().get(itemPos).setUser(null);
                mMpAdapter.notifyItemChanged(itemPos);
            }
        }

        EventBus.getDefault().post(new UpdateInRoomMemberEvent());
        EventBus.getDefault().post(new UpdateApplyMicEvent());
        setMpIntroText();
    }

    @Override
    public void onDownMicroSelf() {
        if (mHandler == null) {
            return;
        }
        AgoraClient.create().rtcEngine().stopAudioMixing();
        setResetLyric(false);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mViewModel.changeSongParam();
            }
        });
    }

    @Override
    public void onForbiddenMicro(BaseMicroMsg msg, boolean isForbidden) {
        int itemPos = mMpAdapter.getItemPos(msg.getLevel(), msg.getNumber());
        mMpAdapter.onForbiddenMicro(itemPos, isForbidden);
    }

    @Override
    public void onLockMicro(BaseMicroMsg msg, boolean isLock) {
        int itemPos = mMpAdapter.getItemPos(msg.getLevel(), msg.getNumber());

    }

    @Override
    public void onMicroSort() {
        setMicroSort();
    }

    private List<MicroUserBean> microUserBeans = new ArrayList<>();


    @Override
    public void leaveRoomUserToOther(String userId) {
        if (microUserBeans != null && microUserBeans.size() > 0) {
            for (int i = 0; i < microUserBeans.size(); i++) {
                if (microUserBeans.get(i).getUid().equals(userId)) {
                    microUserBeans.remove(i);
                    if (i == 0 && applyMicroDialog != null && applyMicroDialog.isShowing()) {
                        applyMicroDialog.dismiss();
                    }

                }
            }
        }

    }

    @Override
    public void updateGuardSign(UpdateGuardSignToClientParam bean) {
        if (!TextUtils.isEmpty(bean.getGuardSign())) {
            mRoomModel.getUser().setGuardSign(bean.getGuardSign());
        }
    }

    @Override
    public void updateMicro() {
//        initView2();
    }

    @Override
    public void GuardPersonJoinRoom(JoinUser joinUser) {
        showGuardBayWindowDiallog(joinUser);
    }

    @Override
    public void showGuardGif(ShowGuardAnimationToClientParam bean) {
        if (bean != null) {
            GuardSuccessDialog guardSuccessDialog = new GuardSuccessDialog(mContext);
            guardSuccessDialog.setData(bean);
            guardSuccessDialog.show();
        }
    }


    @Override
    public void showRedPackageRule(String content) {
        RedpackageRuleDialog redpackageRuleDialog = new RedpackageRuleDialog(mContext);
        redpackageRuleDialog.setMsg(content);
        redpackageRuleDialog.show();
    }

    @Override
    public void notifyRedPacketProgress(boolean visible, float progress) {
        if (progress > 1) {
            progress = 1;
        }
        if (visible) {
            ViewBindUtils.setImageRes(mBinding.seekbarRedpackageProgress, R.drawable.icon_redpackage_seek_red);
        } else {
            ViewBindUtils.setImageRes(mBinding.seekbarRedpackageProgress, R.drawable.icon_redpackage_seek_black);

        }
        ViewBindUtils.setText(mBinding.tvRedpackageProgress, (int) (progress * 100) + "%");
    }

    @Override
    public void startRobRedPacket(String alert) {
        if (!TextUtils.isEmpty(alert)) {

            showCenterHtmlToast(alert);

        }
        mBinding.redPacketsView.setVisibility(View.VISIBLE);
        startRedRain();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBinding.redPacketsView.setVisibility(View.GONE);
                mBinding.redPacketsView.stopRainNow();
            }
        }, 10000);
    }

    @Override
    public void showRobRedpacketResultList(List<UserRedPacket> redPackets) {
        mBinding.redPacketsView.setVisibility(View.GONE);
        mBinding.redPacketsView.stopRainNow();
        RedpackageResultDialog redpackageResultDialog = new RedpackageResultDialog(mContext);
        redpackageResultDialog.setMsg(redPackets);
        redpackageResultDialog.show();
    }

    @Override
    public void showCenterHtmlToast(String content) {
        if (!TextUtils.isEmpty(content)) {
            content = content.replace("\"", "").replace("\"", "");
            RoomMsgBean roomMsgBean = new RoomMsgBean();
            roomMsgBean.setLocalMsg(content);
            List<RoomMsgBean> list = new ArrayList<>();
            list.add(roomMsgBean);
            onAddMsg(list);
            ToastUtils.showHtmlCenter(mContext, content, 5000);
        }
    }

    @Override
    public void keepLive() {
        mViewModel.fetchKeepAlive();
    }

    @Override
    public void updateOnlineHeads(List<String> heads) {
        mRankingAdapter.setNewData(heads);
    }

    @Override
    public void upDateBalance(double balance) {
        mRoomModel.getRoomData().setBalance(String.valueOf(balance));
        if (mGiftDialog != null) {
            mGiftDialog.setBalance(mRoomData.getBalance());
        }
    }


    private void showGuardBayWindowDiallog(JoinUser joinUser) {
        if (joinUser != null && joinUser.isRoomGuard()) {
            GuardBayWindowDialog bayWindowDialog = new GuardBayWindowDialog(mContext);
            bayWindowDialog.setMsg(joinUser);
            bayWindowDialog.setAutoDismiss();
            bayWindowDialog.show();
        }
    }

    /**
     * 用户上麦提示
     */
    ApplyMicroDialog applyMicroDialog;

    private void showUserMicroDialog() {
        if (microUserBeans != null && microUserBeans.size() > 0) {
            MicroUserBean bean = microUserBeans.get(0);
            applyMicroDialog = new ApplyMicroDialog(mContext);
            applyMicroDialog.setMsg(bean.getuName(), bean.getuHeadUrl());
            applyMicroDialog.setAutoDismiss();
            applyMicroDialog.setmOnClickListener(new ApplyMicroDialog.OnClickListener() {
                @Override
                public void onClick() {
                    int type = bean.getType();
                    boolean hasMicro = false;
                    List<RoomData.MicroInfosBean> microUserBeans = mRoomModel.getMicros();
                    for (RoomData.MicroInfosBean bean1 : microUserBeans) {
                        if (type == bean1.getType() && bean1.getUser() != null) {
                            hasMicro = true;
                        }
                    }
                    if (!hasMicro) {
                        microType = 4;
                        toOnWheatUid = bean.getUid();
                        mViewModel.toOnWheat(toOnWheatUid, false);
                    } else {
                        showMicroManageDialog(type);
                    }
                }

                @Override
                public void onDismiss() {
                    microUserBeans.remove(0);
                }
            });

            Window window = applyMicroDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.y = DisplayUtil.dip2px(mContext, 52);
            window.setAttributes(params);
            applyMicroDialog.showAtBottom();
        }
    }

    @Override
    public void receiveMicroRequest(MicroSort sort) {
        setMicroSort();
        if (mRoomData.getUserIdentity() != 0 && MatchMakerUtils.isRoomOwner()) {
            String uid = sort.getMicroOrderData().getUser().getUserId();
            String uName = sort.getMicroOrderData().getUser().getName();
            String uHeadUrl = sort.getMicroOrderData().getUser().getHeadImageUrl();
            int type = sort.getMicroOrderData().getUser().getSex();
            microUserBeans.add(new MicroUserBean(uid, uName, uHeadUrl, type));
            showUserMicroDialog();
        }
    }

    @Override
    public void removeMicro() {
        if (!UserUtils.getUser().getUid().equals(String.valueOf(hongId))) ;
        {
            AAlertDialog dialog = new AAlertDialog(mContext);
            dialog.setMessage("您已被移除麦序", com.deepsea.mua.stub.R.color.black, 15);
            dialog.setButton("确定", com.deepsea.mua.stub.R.color.gray, null);
            dialog.show();
        }

    }

    @Override
    public void onMicroTypeChanged(boolean isFreeMicro) {
    }

    @Override
    public void onHeartValueOpened(boolean isOpen) {
        mMpAdapter.setOpenHeart(isOpen);
    }

    @Override
    public void onMultiSend(int code) {
        dealSendGift(code, false);
        if (mGiftDialog != null) {
            mGiftDialog.setBalance(mRoomData.getBalance());
        }
    }

    @Override
    public void onSingleSend(int code) {
        dealSendGift(code, true);
        if (mGiftDialog != null) {
            mGiftDialog.setBalance(mRoomData.getBalance());
        }

    }

    @Override
    public void onReceiveGift(ReceivePresent model) {
//        mBinding.popularityTv.setText(FormatUtils.formatTenThousand(model.getVisitorNum()));
        if (isResumed) {
            Log.d("onReceiveGift", "onReceiveGift");
            mSvgaUtils.startAnimator(model);
        }

    }

    @Override
    public void onHeartValue(BaseMicroMsg msg, int heartValue) {
        if (msg.getLevel() != 0) {
            if (mJoinRoom.getRoomMode() != 9) {

                int itemPos = mMpAdapter.getItemPos(msg.getLevel(), msg.getNumber());
                mMpAdapter.onHeartValue(itemPos, heartValue);
            }
        }
    }

    @Override
    public void onCountDown(CountDown bean) {
        int itemPos = mMpAdapter.getItemPos(bean.getLevel(), bean.getNumber());
        mMpAdapter.onCountDown(itemPos, bean.getSpeechTime(), bean.getDuration());
    }

    @Override
    public void onKickRoom(String msg) {
        ToastUtils.showToast(msg);
        mViewModel.exitRoom();

    }

    private boolean isRequestOwnerMsg;

    @Override
    public void onUserInfo(MicroUser user) { //显示头像弹框
        //房主头像
        if (isRequestOwnerMsg) {
            if (mOwnerDialog != null) {
                mRoomModel = RoomController.getInstance().getRoomModel();
                if (mRoomModel != null) {
                    mRoomData = mRoomModel.getRoomData();
                }
                mOwnerDialog.setData(mRoomData, user);
                mOwnerDialog.show();
            }
            isRequestOwnerMsg = false;
            return;
        }

        if (user.isIsOnMicro()) {
            if (mViewModel.isOnHostMicro(user.getUser().getUserId())) {
                RoomData.MicroInfosBean hostMicro = mRoomModel.getHostMicro();
                user.setType(hostMicro.getType());
                user.setNumber(hostMicro.getNumber());
            } else {
                List<RoomData.MicroInfosBean> microInfos = mRoomModel.getMicros();
                for (RoomData.MicroInfosBean bean : microInfos) {
                    if (bean.getUser() == null)
                        continue;
                    if (bean.getUser().getUserId().equals(user.getUser().getUserId())) {
                        user.setType(bean.getType());
                        user.setNumber(bean.getNumber());
                        break;
                    }
                }
            }
        }
        if (mAvatarDialog != null) {
            mAvatarDialog.setData(user);
            mAvatarDialog.showAtBottom();
        }
        MobEventUtils.onCheckMicro(mContext);
    }

    @Override
    public void roomNameChanged(String roomName) {
        mBinding.roomNameTv.setText(roomName);
    }

    @Override
    public void onlineUser(OnlineUser onlineUser) {
//        mOnlineDialog.setData(onlineUser);
        if (mJoinRoom != null && mJoinRoom.getRoomMode() == 9) {
            if (micManagerForServenDialog != null && micManagerForServenDialog.getDialog() != null
                    && micManagerForServenDialog.getDialog().isShowing()) {
                micManagerForServenDialog.setVisitorInroomData(onlineUser.getOnlineUsers());
            }
        } else {
            if (micManagerDialog != null && micManagerDialog.getDialog() != null
                    && micManagerDialog.getDialog().isShowing()) {
                micManagerDialog.setVisitorInroomData(onlineUser.getOnlineUsers());
            }
        }
    }


    @Override
    public void onSendEmoJi() { //todo 发送表情失败
        isEmotionSending = true;

    }

    @Override
    public void onShowEmoJi(EmotionBean bean) {
        if (bean.getMicroLevel() == 0) {
            if (isChangeView) {
                mBinding.animMusicIv.loadGameAnim(bean.getEmoticonAnimationUrl());
            } else {
                mBinding.animIv.loadGameAnim(bean.getEmoticonAnimationUrl());
            }
        } else {
            int itemPos = mMpAdapter.getItemPos(bean.getMicroLevel(), bean.getMicroNumber());
            mMpAdapter.onGameEmojiAnima(itemPos, bean.getEmoticonAnimationUrl());
        }
    }

    @Override
    public void onEmoJiResult(EmotionBean bean, String url) {
        if (TextUtils.isEmpty(url)) {
            isEmotionSending = false;
            if (mEmojiDialog != null && mEmojiDialog.isShowing()) {
                mEmojiDialog.setSending(false);
            }

        }

        if (bean.getMicroLevel() == 0) {
            if (isChangeView) {
                mBinding.animMusicIv.loadGameResult(url);

            } else {
                mBinding.animIv.loadGameResult(url);
            }
        } else {
            int itemPos = mMpAdapter.getItemPos(bean.getMicroLevel(), bean.getMicroNumber());
            mMpAdapter.onGameResult(itemPos, url);
        }
    }

    @Override
    public void onOwnerUpdated() {
        GlideUtils.circleImage(mBinding.ownerIv, mRoomData.getRoomOwnerHeadUrl(), R.drawable.ic_place_avatar, R.drawable.ic_place_avatar);
    }

    @Override
    public void onBalanceNoEnough() {
        showRechargAlerteDialog();
    }

    @Override
    public void onBalance(String balance) {
        if (mGiftDialog != null && mGiftDialog.isShowing()) {
            mGiftDialog.setBalance(mRoomData.getBalance());
        }
    }

    @Override
    public void onMicroCost(String cost) {
        AAlertDialog dialog = new AAlertDialog(mContext);
        boolean hasCard = (mRoomData.getWheatCardCount() > 0);
        if (hasCard) {
            dialog.setTitle("免费上麦");
        }
        dialog.setMessage(hasCard ? "您将消耗一张上麦卡，免费上麦" : "上麦需要花费" + cost + "朵玫瑰", R.color.black, 15);
        dialog.setRightButton("同意", R.color.primary_pink, (v, dialog1) -> {
            dialog1.dismiss();
            microType = 3;
            upMicroType = mViewModel.level;
            upMicroNum = mViewModel.number;
            mViewModel.upMicro(mViewModel.level, mViewModel.number, false);
        });
        dialog.setLeftButton("取消", R.color.gray, (v, dialog1) -> {
            dialog1.dismiss();
        });
        dialog.show();
    }

    boolean isInRoomInvite = false;
    private MpInvited mpInvitedBean = null;

    @Override
    public void onMpInvited(MpInvited bean) {
        mpInvitedBean = bean;
        showMpInvited();
    }

    public void showMpInvited() {
        if (mpInvitedBean == null) {
            return;
        }
        AAlertDialog dialog = new AAlertDialog(mContext);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isInRoomInvite = false;
            }
        });
        dialog.setTitle("主持邀请您上麦，是否同意上麦？", R.color.black, 15);
        String suffix = "";
        if (mpInvitedBean.getFree() != 1) { //付费
            suffix = "（" + mpInvitedBean.getCost() + "朵玫瑰）";
            dialog.setMessage(suffix);
        }
        dialog.setRightButton("同意", R.color.primary_pink, (v, dialog1) -> {
            dialog.setOnDismissListener(null);
            isInRoomInvite = true;
            dialog1.dismiss();
            microType = 1;
            mViewModel.agreeInviteMp(true, false);
        });
        dialog.setLeftButton("拒绝", R.color.gray, (v, dialog1) -> {
            isInRoomInvite = false;
            dialog1.dismiss();
            mViewModel.agreeInviteMp(false, false);
        });
        dialog.show();
        if (mHandler != null) {
            mHandler.postDelayed(dialog::dismiss, 5000);
        }
    }

    @Override
    public void onMatcherExited(int code, String msg) {
        if (code == 5) {
            return;
        }
        releaseMainface();
//        HeartControl.getInstance(mContext).stopHeartBeatObservable();
        if (!TextUtils.isEmpty(msg)) {
            toastShort(msg);
        }
        showProgress();
        if (mHandler != null) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    hideProgress();
                }
            }, 1000);
        }
    }

    public void releaseMainface() {
        if (mJoinRoom.getRoomMode() != 9) {
            mainFaceView.release();
            mainFaceMusicView.release();
        } else {
            mBinding.mainSecondOneView.release();
            mBinding.mainSecondTwoMusicView.release();
        }
    }

    private int inMicroManNum = 0;
    private int inMicroWomanNum = 0;

    @Override
    public void onRequestMicroGuestChanged(int inMicroManNum, int inMicroWomanNum) {
        this.inMicroManNum = inMicroManNum;
        this.inMicroWomanNum = inMicroWomanNum;
    }

    @Override
    public void onOnlineGuestChanged(int onlineManNum, int onlineWomanNum) {
//        mBinding.tvOnlineManNum.setText(String.valueOf(onlineManNum));
//        mBinding.tvOnlineWomanNum.setText(String.valueOf(onlineWomanNum));
    }

    @Override
    public void lockRoomExitUnInMic() {
        if (mRoomModel.isOnMp()) {
            return;
        } else {
            mViewModel.exitRoom();
        }
    }

    boolean isMatchmarkExitRoom = false;

    @Override
    public void exitRoom() {
        if (!MatchMakerUtils.isRoomOwner() && isMatchmarkExitRoom) {
            toastShort("该房间关闭，请去其他房间看看吧。");
        }
        releaseMainface();
//        HeartControl.getInstance(mContext).stopHeartBeatObservable();
        showProgress();
        if (mHandler == null) {
            return;
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
                finish();
            }
        }, 2000);

    }

    @Override
    public void sendReConnetMsg() {
        mViewModel.reConnect();
        RoomController.getInstance().setRetry(false);
    }

    @Override
    public void upDataMicInfo(List<RoomData.MicroInfosBean> bean) {
        if (mHandler == null) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                initCommonMp();
                initMicro();
            }
        });

    }

    @Override
    public void leaveSWChannel() {
        if (!mRoomModel.isOnMp())
            AgoraClient.create().leaveChannel();
    }

    @Override
    public void showAlert(int code, String msg) {
        showErrorAller(msg);


    }

    @Override
    public void onMicroDownExitRoom() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                RoomController.getInstance().release();
                exitRoom();
            }
        });

    }


    AAlertDialog aAlertDialog;

    private void showErrorAller(String msg) {
        Context context = ActivityCache.getInstance().getTopActivity();
//        if (aAlertDialog != null) {
//            aAlertDialog.dismiss();
//        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (aAlertDialog != null) {
                    aAlertDialog = new AAlertDialog(context);
                    aAlertDialog.setMessage(msg, com.deepsea.mua.stub.R.color.black, 15);
                    aAlertDialog.setButton("确定", com.deepsea.mua.stub.R.color.gray, null);
                    aAlertDialog.show();
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(InviteInRoomEvent event) {
        isInRoomInvite = true;
        inviteUpMicro(event.inviteMicroId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(ExitRoomEvent event) {
        mViewModel.exitRoom();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            unregisterWxpayResult();
            mRoomJump.destroy();
            SharedPrefrencesUtil.saveData(mContext, "inroom", "inroom", false);
            stopService(new Intent(mContext, MyNotifyService.class));
            SongStateUtils.getSingleton2().resetReverbPresetList();
            SongStateUtils.getSingleton2().setChangeView(false);
            SongStateUtils.getSingleton2().setConsertUserId("");
            SongStateUtils.getSingleton2().setCurrentPos(0);
            SongStateUtils.getSingleton2().setHeartCount(0);
            SongStateUtils.getSingleton2().reset();
            AgoraClient.create().rtcEngine().enableLocalVideo(true);

            NetworkManager.getDefault().unRegisterObserver(this);

            EventBus.getDefault().unregister(this);
            SharedPrefrencesUtil.saveData(mContext, "isFirstForApp", "isFirstForApp", false);
            if (mRoomData.getMicroInfos() != null) {
                List<RoomData.MicroInfosBean> beans = mRoomData.getMicroInfos();
                if (beans != null) {
                    for (int i = 0; i < beans.size(); i++) {
                        WsUser user = beans.get(i).getUser();
                        if (user != null) {
                            AgoraClient.create().muteRemoteAudioStream(Integer.valueOf(user.getUserId()), false);
                            AgoraClient.create().muteLocalAudioStream(false);
                        }
                    }
                }
            }
            ForbiddenStateUtils.clearForbiddenData();
            InMicroMemberUtils.getInstance().clear();
            SharedPrefrencesUtil.deleteData(mContext, "mRoomId", "mRoomId");
            RoomController.getInstance().release();
            if (mInputDialog != null && mInputDialog.isShowing()) {
                mInputDialog.dismiss();
                mInputDialog = null;
            }
            if (aAlertDialog != null && aAlertDialog.isShowing()) {
                aAlertDialog.dismiss();
                aAlertDialog = null;
            }
            if (mGiftDialog != null && mGiftDialog.isShowing()) {
                mGiftDialog.dismiss();
                mGiftDialog = null;
            }
            if (mHandler != null) {
                mHandler.removeMessages(msg_send_lyric);
                mHandler.removeMessages(msg_download_lrc);
                mHandler.removeMessages(msg_start_play_song);
                mHandler.removeMessages(msg_start_play_song_with_lrc);
                mHandler.removeMessages(msg_lyric_reset);
                mHandler.removeMessages(msg_lyric_show);
                mHandler.removeMessages(msg_lyric_receive);
                mHandler.removeMessages(msg_start_download_song);
                mHandler.removeMessages(msg_download_song_success);
                mHandler.removeCallbacksAndMessages(null);
            }
            if (mTimer != null) {
                mTimer.cancel();
                mTimer = null;
            }
            if (mTask != null) {
                mTask.cancel();
                mTask = null;
            }
            if (songManagerDialog != null) {
                songManagerDialog.dismiss();
            }
        } catch (Exception e) {

        }
    }

    @Network(netType = NetType.AUTO)
    public void network(NetType netType) {
        switch (netType) {
            case WIFI:
                Log.e(TAG, "WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.e(TAG, "有网络");
                if (SongStateUtils.getSingleton2().getSongState() == 2) {
                    mViewModel.sysnSongPause(true);
                }
                break;
            case NONE:
                Log.e(TAG, "没有网络");
                break;
        }
    }
}
