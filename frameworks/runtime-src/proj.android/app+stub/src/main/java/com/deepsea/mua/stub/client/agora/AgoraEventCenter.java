package com.deepsea.mua.stub.client.agora;

import android.util.Log;

import com.deepsea.mua.core.log.Logg;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import io.agora.rtc.IRtcEngineEventHandler;

import static io.agora.rtc.Constants.CONNECTION_STATE_FAILED;

/**
 * Created by JUN on 2019/8/7
 */
public class AgoraEventCenter {

    private static final String TAG = "AgoraEventCenter";

    private final ConcurrentHashMap<IAgoraEventHandler, Integer> mEventHandlers = new ConcurrentHashMap<>();

    public void addAgoraEventHandler(IAgoraEventHandler handler) {
        this.mEventHandlers.put(handler, 0);
    }

    public void removeAgoraEventHandler(IAgoraEventHandler handler) {
        this.mEventHandlers.remove(handler);
    }

    public void clearAgoraEventHandler() {
        this.mEventHandlers.clear();
    }

    public final IRtcEngineEventHandler mAgoraEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onConnectionStateChanged(int state, int reason) {
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onConnectionStateChanged(state, reason);
            }
        }


        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Logg.d(TAG, "onJoinChannelSuccess: channel = " + channel + " uid = " + uid + " elapsed = " + elapsed);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onJoinChannelSuccess(channel, uid, elapsed);
            }
        }

        @Override
        public void onRejoinChannelSuccess(String channel, int uid, int elapsed) {
//            Log.d(TAG, "onRejoinChannelSuccess: channel = " + channel + " uid = " + uid + " elapsed = " + elapsed);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onRejoinChannelSuccess(channel, uid, elapsed);
            }
        }

        @Override
        public void onAudioVolumeIndication(AudioVolumeInfo[] speakers, int totalVolume) {
//            Log.d(TAG, "onAudioVolumeIndication");
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onAudioVolumeIndication(speakers, totalVolume);
            }
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
//            Log.d(TAG, "onUserJoined: uid = " + uid + " elapsed = " + elapsed);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onUserJoined(uid, elapsed);
            }
        }

        @Override
        public void onUserOffline(int uid, int reason) {
//            Log.d(TAG, "onUserOffline: uid = " + uid + " reason = " + reason);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onUserOffline(uid, reason);
            }
        }

        @Override
        public void onUserMuteAudio(int uid, boolean muted) {
//            Log.d(TAG, "onUserMuteVideo: uid = " + uid + " muted = " + muted);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onUserMuteAudio(uid, muted);
            }
        }

        @Override
        public void onMicrophoneEnabled(boolean enabled) {
//            Log.d(TAG, "onMicrophoneEnabled: enabled = " + enabled);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onMicrophoneEnabled(enabled);
            }
        }

        @Override
        public void onClientRoleChanged(int oldRole, int newRole) {
//            Log.d(TAG, "onClientRoleChanged: oldRole = " + oldRole + " newRole = " + newRole);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onClientRoleChanged(oldRole, newRole);
            }
        }

        @Override
        public void onFirstRemoteVideoFrame(int uid, int width, int height, int elapsed) {
//            Log.d(TAG, "onFirstRemoteVideoFrame: uid = " + uid + " width = " + width + " height = " + height + " elapsed = " + elapsed);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onFirstRemoteVideoFrame(uid, width, height, elapsed);
            }
        }

        @Override
        public void onRemoteVideoStateChanged(int uid, int state, int reason, int elapsed) {
//            Log.d(TAG, "onRemoteVideoStateChanged: uid = " + uid + " state = " + state + " reason = " + reason + " elapsed = " + elapsed);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onRemoteVideoStateChanged(uid, state, reason, elapsed);
            }
        }

        @Override
        public void onLeaveChannel(IRtcEngineEventHandler.RtcStats stats) {
            Logg.d(TAG, "onLeaveChannel: stats = " + stats);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onLeaveChannel(stats);
            }
        }

        @Override
        public void onWarning(int warn) {
//            Log.d(TAG, "onWarning: warn = " + warn);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onWarning(warn);
            }
        }

        @Override
        public void onError(int err) {
            Logg.d(TAG, "onError: err = " + err);
            Iterator<IAgoraEventHandler> it = mEventHandlers.keySet().iterator();
            while (it.hasNext()) {
                IAgoraEventHandler handler = it.next();
                handler.onError(err);
            }
        }
    };
}
