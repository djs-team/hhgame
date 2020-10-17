package com.deepsea.mua.voice.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.deepsea.mua.core.utils.GlideUtils;
import com.deepsea.mua.core.utils.JsonConverter;
import com.deepsea.mua.core.websocket.WsocketListener;
import com.deepsea.mua.core.websocket.WsocketManager;
import com.deepsea.mua.stub.base.BaseFragment;
import com.deepsea.mua.stub.client.agora.AgoraClient;
import com.deepsea.mua.stub.data.User;
import com.deepsea.mua.stub.entity.LocalVoiceReverbPresetVo;
import com.deepsea.mua.stub.entity.socket.receive.PlaySongParam;
import com.deepsea.mua.stub.entity.socket.receive.SongInfo;
import com.deepsea.mua.stub.entity.socket.receive.SyncSongStateParam;
import com.deepsea.mua.stub.entity.socket.receive.SyncSongUpdateWindowParam;
import com.deepsea.mua.stub.entity.socket.receive.SyncSongVoiceParam;
import com.deepsea.mua.stub.entity.socket.receive.UpdateDownloadSongStateParam;
import com.deepsea.mua.stub.utils.CacheUtils;
import com.deepsea.mua.stub.utils.FileUtils;
import com.deepsea.mua.stub.utils.SharedPrefrencesUtil;
import com.deepsea.mua.stub.utils.TimeUtils;
import com.deepsea.mua.stub.utils.UserUtils;
import com.deepsea.mua.stub.utils.ViewBindUtils;
import com.deepsea.mua.stub.utils.ViewModelFactory;
import com.deepsea.mua.stub.utils.eventbus.DownloadEvent;
import com.deepsea.mua.stub.utils.eventbus.SyncSongProgressEvent;
import com.deepsea.mua.voice.R;
import com.deepsea.mua.voice.databinding.FragmentSongPlayingBinding;
import com.deepsea.mua.voice.dialog.SongManagerDialog;
import com.deepsea.mua.voice.dialog.SongVoiceRegulationDialog;
import com.deepsea.mua.voice.utils.MatchMakerUtils;
import com.deepsea.mua.stub.utils.SongStateUtils;
import com.deepsea.mua.voice.viewmodel.SongOriginalViewModel;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import io.agora.rtc.Constants;
import okhttp3.Response;

import static io.agora.rtc.Constants.AUDIO_REVERB_DRY_LEVEL;
import static io.agora.rtc.Constants.AUDIO_REVERB_OFF;
import static io.agora.rtc.Constants.AUDIO_REVERB_ROOM_SIZE;
import static io.agora.rtc.Constants.AUDIO_REVERB_STRENGTH;
import static io.agora.rtc.Constants.AUDIO_REVERB_WET_DELAY;
import static io.agora.rtc.Constants.AUDIO_REVERB_WET_LEVEL;

/**
 * Created by JUN on 2019/5/5
 */
public class SongPlayingFragment extends BaseFragment<FragmentSongPlayingBinding> {
    private static SongManagerDialog.OnManageListener mListener;
    private static SongManagerDialog.onViewPagerHeightListener mPagerHeightListener;

    public SongPlayingFragment() {

    }


    public static SongPlayingFragment newInstance(SongManagerDialog.OnManageListener listener, SongManagerDialog.onViewPagerHeightListener pagerHeightListener) {
        mListener = listener;
        mPagerHeightListener = pagerHeightListener;
        SongPlayingFragment instance = new SongPlayingFragment();
        Bundle bundle = instance.getArguments();
        if (bundle == null) {
            bundle = new Bundle();
            instance.setArguments(bundle);
        }
        return instance;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_song_playing;
    }

    private SongOriginalViewModel mViewModel;
    @Inject
    ViewModelFactory mModelFactory;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SongPlayingFragment", "start");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("SongPlayingFragment", "resume");
        mViewModel.syncUpdateSongWindowParam();


    }

    @Override
    protected void initView(View view) {
        registerEventBus(this);
        GlideUtils.loadGif(mBinding.ivHintClickPlay, R.drawable.icon_hint_click_playsong);
        mViewModel = ViewModelProviders.of(this, mModelFactory).get(SongOriginalViewModel.class);
        mBinding.seekbarSongProgress.setEnabled(false);
        mBinding.seekSongVoice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d("songDowload", "" + seekBar.getProgress());
                AgoraClient.create().rtcEngine().adjustAudioMixingVolume(seekBar.getProgress());
                mViewModel.sysnSongVoice(seekBar.getProgress());

            }
        });
        boolean isMarchMaker = MatchMakerUtils.isRoomOwner();
        ViewBindUtils.setVisible(mBinding.rlSongOperate, isMarchMaker);
        addSocketListener();
        mViewModel.getPlaySongParam();
    }


    private int songState = 0;//0代表未播放  1 已播放 2 已暂停 3 下载  4 下载完成 5 下载失败

    private void changeSong() {
        mViewModel.changeSongParam();
        if (mListener != null) {
            mListener.onResetLrc();
        }
    }

    boolean isChangeSong = false;

    @Override
    protected void initListener() {
        super.initListener();
        subscribeClick(mBinding.ivSongReplay, o -> {
            SongStateUtils.getSingleton2().setHintPlay(true);
            ViewBindUtils.setVisible(mBinding.ivHintClickPlay, false);
            if (playSongParam == null) {
                return;
            }
            rePlaySong();
            mViewModel.sysnSongRePlay();
            isChangeSong = false;
        });

        subscribeClick(mBinding.ivSongNext, o -> {
            SongStateUtils.getSingleton2().setHintPlay(false);
            ViewBindUtils.setVisible(mBinding.ivHintClickPlay, false);
            if (playSongParam == null) {
                return;
            }
            isChangeSong = true;
            changeSong();

        });
        subscribeClick(mBinding.rlVoiceRegulation, o -> {
            showVoiceRegulationDialog();
        });
        subscribeClick(mBinding.ivSongPlay, o -> {

            if (playSongParam == null) {
                return;
            }
            isChangeSong = false;
            if (songState == 0) {
                SongStateUtils.getSingleton2().setHintPlay(true);
                ViewBindUtils.setVisible(mBinding.ivHintClickPlay, false);
                SongInfo curentSong = playSongParam.getSongInfo();
                if (curentSong == null) {
                    return;
                }
                User user = UserUtils.getUser();
                if (TextUtils.isEmpty(curentSong.getConsertUserName()) || curentSong.getConsertUserName().equals(user.getNickname())) {
                    if (mListener != null) {
                        mListener.onDownLoadSong(playSongParam.getSongInfo());
                    }
                } else {
                    if (mListener != null) {
                        mListener.onResetLrc();
                    }
                    mViewModel.startPlaySongParam();
                }
                songState = 1;
//                ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_pause);
//                mViewModel.sysnSongPause(false);

            } else if (songState == 1) {
                songState = 2;
                ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);
                AgoraClient.create().rtcEngine().pauseAudioMixing();
                AgoraClient.create().rtcEngine().enableInEarMonitoring(false);
                AgoraClient.create().rtcEngine().setLocalVoiceReverbPreset(AUDIO_REVERB_OFF);
                mViewModel.sysnSongPause(true);

            } else if (songState == 2) {
                songState = 1;
                ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_pause);
                SongInfo curentSong = playSongParam.getSongInfo();
                if (curentSong == null) {
                    return;
                }
                User user = UserUtils.getUser();
                if (TextUtils.isEmpty(curentSong.getConsertUserName()) || curentSong.getConsertUserName().equals(user.getNickname())) {
                    if (mListener != null) {
                        mListener.onDownLoadSong(playSongParam.getSongInfo());
                    }
                } else {
                    if (mListener != null) {
                        mListener.onResetLrc();
                    }
                    mViewModel.startPlaySongParam();
                }
                mViewModel.sysnSongPause(false);

            }

        });

    }

    private void showVoiceRegulationDialog() {
        SongVoiceRegulationDialog dialog = new SongVoiceRegulationDialog(mContext);
        dialog.setmClickListener(new SongVoiceRegulationDialog.OnClickListener() {
            @Override
            public void onVoiceEffecClick(int value) {
                if (value == AUDIO_REVERB_OFF) {
                    AgoraClient.create().rtcEngine().setLocalVoiceReverbPreset(AUDIO_REVERB_OFF);
                    return;
                }
                List<LocalVoiceReverbPresetVo> voiceReverbPresetVoList = SongStateUtils.getSingleton2().getReverbPresetVoList();
                if (voiceReverbPresetVoList != null && voiceReverbPresetVoList.size() > 0) {
                    LocalVoiceReverbPresetVo vo = voiceReverbPresetVoList.get(value - 1);
                    AgoraClient.create().rtcEngine().setLocalVoiceReverb(AUDIO_REVERB_ROOM_SIZE, vo.getRoomSize());
                    AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_DELAY, vo.getWetDelay());
                    AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_STRENGTH, vo.getStrength());
                    AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_WET_LEVEL, vo.getWetLevel());
                    AgoraClient.create().rtcEngine().setLocalVoiceReverb(Constants.AUDIO_REVERB_DRY_LEVEL, vo.getDryLevel());

                }
//                AgoraClient.create().rtcEngine().setLocalVoiceReverbPreset(value);
//                regulationVo.setLocalVoiceReverbPreset(value);
//                SharedPrefrencesUtil.saveData(mContext, "voiceRegulation", "voiceRegulation", JsonConverter.toJson(regulationVo));

            }

            @Override
            public void onVoicePitchClick(double value) {
//                AgoraClient.create().rtcEngine().setLocalVoicePitch(value);
//                regulationVo.setLocalVoicePitch(value);
//                SharedPrefrencesUtil.saveData(mContext, "voiceRegulation", "voiceRegulation", JsonConverter.toJson(regulationVo));

            }

            @Override
            public void onVoiceLocalVoiceEqualization(int index, int value) {
//                AgoraClient.create().rtcEngine().setLocalVoiceEqualization(index, value);
//                regulationVo.getLocalVoiceEqualization().put(index, value);
//                SharedPrefrencesUtil.saveData(mContext, "voiceRegulation", "voiceRegulation", JsonConverter.toJson(regulationVo));
            }

            @Override
            public void onVoiceLocalVoiceReverb(int index, int value) {
                int effectIndex = SongStateUtils.getSingleton2().getSelectEffect();
                LocalVoiceReverbPresetVo voiceReverbPresetVo = SongStateUtils.getSingleton2().getReverbPresetVoList().get(effectIndex - 1);
                switch (index) {
                    case AUDIO_REVERB_ROOM_SIZE:
                        voiceReverbPresetVo.setRoomSize(value);
                        break;
                    case AUDIO_REVERB_WET_DELAY:
                        voiceReverbPresetVo.setWetDelay(value);
                        break;
                    case AUDIO_REVERB_DRY_LEVEL:
                        voiceReverbPresetVo.setDryLevel(value);
                        break;

                    case AUDIO_REVERB_STRENGTH:
                        voiceReverbPresetVo.setStrength(value);
                        break;
                    case AUDIO_REVERB_WET_LEVEL:
                        voiceReverbPresetVo.setWetLevel(value);
                        break;

                }
            }
        });
        dialog.init();
        dialog.showAtBottom();
    }


    private void rePlaySong() {
        songState = 1;
        AgoraClient.create().rtcEngine().setAudioMixingPosition(0);
        mBinding.seekbarSongProgress.setProgress(0);
        String resetTime = TimeUtils.formatSongTime(0);
        ViewBindUtils.setText(mBinding.tvSongTimeStart, resetTime);
    }


    private void addSocketListener() {
        WsocketManager.create().addWsocketListener(mWsocketListener);
    }

    private void removeSocketListener() {
        WsocketManager.create().removeWsocketListener(mWsocketListener);
    }

    private void resetSongInfo() {
        AgoraClient.create().rtcEngine().stopAudioMixing();
        ViewBindUtils.setText(mBinding.tvCurrentSongName, "--");
//        ViewBindUtils.setText(mBinding.tvCurrentSongSinger, "--");
        ViewBindUtils.setText(mBinding.tvDemandUser, "点播：" + "--");
        ViewBindUtils.setText(mBinding.tvConserUser, "演唱者：" + "--");
        ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_download);
        ViewBindUtils.setVisible(mBinding.rlSongEmpty, true);
        ViewBindUtils.setVisible(mBinding.llSongExist, false);
    }

    private void setSongInfo() {
        if (playSongParam != null) {
            SongInfo currentSongInfo = playSongParam.getSongInfo();
            if (currentSongInfo != null) {
                ViewBindUtils.setVisible(mBinding.rlSongEmpty, false);
                ViewBindUtils.setVisible(mBinding.llSongExist, true);
                ViewBindUtils.setText(mBinding.tvCurrentSongName, currentSongInfo.getSongName());
//                ViewBindUtils.setText(mBinding.tvCurrentSongSinger, currentSongInfo.getSingerName());
                ViewBindUtils.setText(mBinding.tvDemandUser, "点播：" + currentSongInfo.getDemandUserName());
                if (!TextUtils.isEmpty(currentSongInfo.getConsertUserName())) {
                    ViewBindUtils.setText(mBinding.tvConserUser, "演唱者：" + currentSongInfo.getConsertUserName());
                } else {
                    ViewBindUtils.setText(mBinding.tvConserUser, "");
                }
                String uid = UserUtils.getUser().getUid();
                String consertUserId = currentSongInfo.getConsertUserId();
                boolean isCurrentUserSinger = uid.equals(consertUserId);
                boolean isOriOwner = currentSongInfo.getConsertUserId().equals("0") && MatchMakerUtils.isRoomOwner();
                ViewBindUtils.setVisible(mBinding.rlSongVoiceReverberationOperate, isCurrentUserSinger || isOriOwner);
                if (isCurrentUserSinger && MatchMakerUtils.isRoomOwner()) {
                    if (mPagerHeightListener != null) {
                        mPagerHeightListener.onHeightChanged(440);
                    }
                } else {
                    if (mPagerHeightListener != null) {
                        mPagerHeightListener.onHeightChanged(366);
                    }
                }
                if (MatchMakerUtils.isRoomOwner()) {
                    String lrcLocalPath = CacheUtils.getSongLrcDir(currentSongInfo.getSongName());
                    boolean isLrcExist = FileUtils.isFileExist(lrcLocalPath);
                    String musicLocalPath = CacheUtils.getSongMusicDir(currentSongInfo.getSongName());
                    boolean isMusicExist = FileUtils.isFileExist(musicLocalPath);
                    if (!currentSongInfo.getConsertUserId().equals("0")) {
                        //伴唱
                        boolean isHinPlay = SongStateUtils.getSingleton2().isHintPlay();
                        if (!isHinPlay) {
                            ViewBindUtils.setVisible(mBinding.ivHintClickPlay, true);
                        }
                        ViewBindUtils.setImageRes(mBinding.ivSongPlay, (isCurrentUserSinger && isMusicExist && isLrcExist) ? R.drawable.icon_song_play : R.drawable.icon_song_download);
                    } else {
                        //原唱
                        SongStateUtils.getSingleton2().setHintPlay(true);
                        ViewBindUtils.setVisible(mBinding.ivHintClickPlay, false);
                        boolean hasSong = isLrcExist && isMusicExist;
                        Log.d("songState", "hasSong" + hasSong);

                        ViewBindUtils.setImageRes(mBinding.ivSongPlay, hasSong ? R.drawable.icon_song_play : R.drawable.icon_song_download);

                    }
                }
            } else {
                resetSongInfo();
            }
            SongInfo nextSongInfo = playSongParam.getNextSongInfo();
            if (nextSongInfo != null) {
//                ViewBindUtils.setVisible(mBinding.llSongNext, true);
                ViewBindUtils.setText(mBinding.tvSongNextName, nextSongInfo.getSongName());
                ViewBindUtils.setText(mBinding.tvSongNextSinger, nextSongInfo.getSingerName());
                ViewBindUtils.setText(mBinding.tvSongNextDemandUser, "点播：" + nextSongInfo.getDemandUserName());
                if (!TextUtils.isEmpty(nextSongInfo.getConsertUserName())) {
                    ViewBindUtils.setText(mBinding.tvSongNextConserUser, "演唱上麦嘉宾：" + nextSongInfo.getConsertUserName());
                } else {
                    ViewBindUtils.setText(mBinding.tvSongNextConserUser, "");

                }
            } else {
//                ViewBindUtils.setVisible(mBinding.llSongNext, false);
                ViewBindUtils.setText(mBinding.tvSongNextName, "--");
                ViewBindUtils.setText(mBinding.tvSongNextSinger, "--");
                ViewBindUtils.setText(mBinding.tvSongNextDemandUser, "点播：" + "--");
                ViewBindUtils.setText(mBinding.tvSongNextConserUser, "演唱上麦嘉宾：" + "--");
            }

        } else {
//            ViewBindUtils.setVisible(mBinding.llSongNext, false);
        }


    }

    PlaySongParam playSongParam;
    private WsocketListener mWsocketListener = new WsocketListener() {
        public void onMessage(String message) {
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(message).getAsJsonObject();
            int msgId = object.get("MsgId").getAsInt();
            switch (msgId) {
                case 94:
//                    Log.d("AG_EX_AV", "94返回：" + message);
                    playSongParam = JsonConverter.fromJson(message, PlaySongParam.class);
                    setSongInfo();
                    break;
                case 99:
//                    Log.d("AG_EX_AV", "99返回：" + message);
                    break;
                case 93:
                    int success = object.get("Success").getAsInt();
                    AgoraClient.create().rtcEngine().stopAudioMixing();
                    if (success == 3) {
                        resetSongInfo();
                    } else if (success == 1) {
                        songState = 0;
                        ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);
                        AgoraClient.create().rtcEngine().stopAudioMixing();
                    }
//                    Log.d("AG_EX_AV", "93返回：" + message);
                    break;
                case 105:
//                    Log.d("songStateParam", "105返回：" + message);
                    SyncSongStateParam songStateParam = JsonConverter.fromJson(message, SyncSongStateParam.class);
                    SongStateUtils.getSingleton2().setSongState(songStateParam.isPause());

                    syncSongPause(songStateParam.isPause());
                    break;
                case 106:
//                    Log.d("sysnSongVoice", "106:" + message);
                    SyncSongVoiceParam syncSongVoiceParam = JsonConverter.fromJson(message, SyncSongVoiceParam.class);
                    syncSongVolume(syncSongVoiceParam.getVolume());
                    break;
                case 107:
                    syncSongRePlay();
                    break;
                case 109:
                    Log.d("sysnSongVoice", "109:" + message);
                    SyncSongUpdateWindowParam songUpdateWindowParam = JsonConverter.fromJson(message, SyncSongUpdateWindowParam.class);
                    if (songUpdateWindowParam != null) {
                        if (songUpdateWindowParam.isPause() == 1) {
                            songState = 2;
                            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);
                        } else if (songUpdateWindowParam.isPause() == 2) {
                            songState = 1;
//                            songUpdateWindowParam.isPause()==0||songUpdateWindowParam.isPause()==2
                            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_pause);
                        } else if (songUpdateWindowParam.isPause() == 0) {
                            songState = 0;
                            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);

                        }
                        syncSongVolume(songUpdateWindowParam.getVolume());
                    }
                    break;
                case 112:
                    Log.d("songStateParam", "songstate：-" + message);

                    UpdateDownloadSongStateParam updateDownloadSongStateParam = JsonConverter.fromJson(message, UpdateDownloadSongStateParam.class);
                    if (updateDownloadSongStateParam != null) {
                        // 0为下载中，1为下载失败，2为下载成功
                        Log.d("songStateParam", "songstate：-" + MatchMakerUtils.isRoomOwner());

                        if (!MatchMakerUtils.isRoomOwner()) {
                            int state = updateDownloadSongStateParam.getState();
                            if (state == 0) {
                                state += 3;
                            } else if (state == 1) {
                                state += 4;
                            } else if (state == 2) {
                                state += 2;
                            }
                            updatePlayState(state);
                        }
                    }
                    break;
            }
        }

        public void onFailure(Throwable t, Response response) {
        }
    };

    /**
     * 设置音量
     *
     * @param volume
     */
    private void syncSongVolume(int volume) {
        mBinding.seekSongVoice.setProgress(volume);
        AgoraClient.create().rtcEngine().adjustAudioMixingVolume(volume);
        SongStateUtils.getSingleton2().setVolume(volume);
    }

    /**
     * 同步暂停
     */
    private void syncSongPause(int isPause) {
        if (isPause == 0) {
            songState = isPause;
            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);
        } else if (isPause == 1) {
            songState = 2;
            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_play);
        } else if (isPause == 2) {
            songState = 1;
            ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_pause);
        }
        Log.d("songStateParam", "songstate：" + songState);

    }

    /**
     * 同步重唱
     */
    private void syncSongRePlay() {
        songState = 1;
        mBinding.seekbarSongProgress.setProgress(0);
        String resetTime = TimeUtils.formatSongTime(0);
        ViewBindUtils.setText(mBinding.tvSongTimeStart, resetTime);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SyncSongProgressEvent event) {
        if (event.max > 0) {
            mBinding.seekbarSongProgress.setMax(event.max);
            String endTimeStr = TimeUtils.formatSongTime(event.max);
            ViewBindUtils.setText(mBinding.tvSongTimeEnd, endTimeStr);
            mBinding.seekbarSongProgress.setProgress(event.currentPos);
            String startTimeStr = TimeUtils.formatSongTime(event.currentPos);
            ViewBindUtils.setText(mBinding.tvSongTimeStart, startTimeStr);
            Log.d("songStateParam", "SyncSongProgressEvent:" + "event.max" + event.max + "endTimeStr:" + endTimeStr + "startTimeStr:" + startTimeStr + "event.currentPos:" + event.currentPos);
        }
        if (songState == 0 && !isChangeSong) {
            songState = 2;
            Log.d("songStateParam", "onevent：" + songState);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DownloadEvent event) {
        updatePlayState(event.downloadState);
    }

    private void updatePlayState(int songState) {
        Log.d("songState", "updatePlayState" + songState);
        switch (songState) {
            case 3:
                //下载中
                GlideUtils.loadGif(mBinding.ivSongPlay, R.drawable.icon_song_download_loading);
                break;
            case 4:
                //下载成功
                ViewBindUtils.setImageRes(mBinding.ivSongPlay, R.drawable.icon_song_pause);
                break;
            case 5:
                songState = 0;
                //下载失败
                GlideUtils.loadGif(mBinding.ivSongPlay, R.drawable.icon_song_download_fail);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeSocketListener();
        unregisterEventBus(this);
    }
}
