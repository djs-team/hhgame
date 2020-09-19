//
//  AgoraRtcEngineManager.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "AgoraRtcEngineManager.h"
//#import <YYModel/YYModel.h>
#import <YYKit/YYKit.h>


#ifdef DEBUG
//static const DDLogLevel ddLogLevel = DDLogLevelVerbose;
#else
//static const DDLogLevel ddLogLevel = DDLogLevelInfo;
#endif  /* DEBUG */

@interface AgoraRtcEngineManager () <AgoraRtcEngineDelegate>

//@property AgoraRtcEngineKit * engine;

//@property (nonatomic, nullable) NSString * appid;

//@property (nonatomic, nullable) NSString * roomId;
//@property (nonatomic, assign) NSUInteger uid;
//@property (nonatomic, assign) NSInteger volume;

//@property (nonatomic, nullable) NSString * joinRoomId;
//@property (nonatomic, assign) NSUInteger joinRoomUid;

//@property (nonatomic, nullable) NSString * leaveRoomId;
//
//@property (nonatomic, assign) AgoraClientRole role;
//@property (nonatomic, nullable) NSNumber * roleRequest;

//@property (nonatomic, assign) BOOL muteLocalAudioStream;
//
//@property (nonatomic, assign) BOOL microphoneEnabled;
//@property (nonatomic, nullable) NSNumber * microphoneEnabledRequest;



@end

#define Role(BC) (BC?AgoraClientRoleBroadcaster:AgoraClientRoleAudience)

@implementation AgoraRtcEngineManager

static AgoraRtcEngineManager * _sharedInstance;

+ (instancetype)instanceWithAppId:(NSString*)appid {
    if (!_sharedInstance) {
        _sharedInstance = [[AgoraRtcEngineManager alloc] initWithAppId:appid];
    }
    return _sharedInstance;
}

+ (void)destroy {
    if (_sharedInstance) {
        _sharedInstance.engine = nil;
        [AgoraRtcEngineKit destroy];
        _sharedInstance = nil;
    }
}

- (instancetype)initWithAppId:(NSString*)appid {
//    DDLogInfo(@"%s", __FUNCTION__);
    if (self = [super init]) {
        self.appid = appid;
        
//        _broadcaster = NO;
//        _disableBroadcast = NO;
//        _microphoneEnabled = YES;
//        _role = _broadcaster ? AgoraClientRoleBroadcaster : AgoraClientRoleAudience;
//        _muteLocalAudioStream = _disableBroadcast || _offMic;
        
//        _joinedUid = [NSMutableArray new];
//        _joinedUser = [NSMutableDictionary new];
        
        _engine = [AgoraRtcEngineKit sharedEngineWithAppId:_appid delegate:self];
//        [_engine enableDualStreamMode:YES];
        [_engine setChannelProfile:AgoraChannelProfileLiveBroadcasting];
        [_engine setVideoEncoderConfiguration:[[AgoraVideoEncoderConfiguration alloc]initWithSize:CGSizeMake(480, 576)
                                                                                        frameRate:AgoraVideoFrameRateFps10
                                                                                        bitrate:AgoraVideoBitrateStandard
                                                                                  orientationMode:AgoraVideoOutputOrientationModeFixedPortrait]];
        
        
        [_engine enableVideo];
        [_engine enableWebSdkInteroperability:YES];
        [_engine setAudioProfile:AgoraAudioProfileMusicStandard scenario:AgoraAudioScenarioGameStreaming];
//        [_engine setDefaultAudioRouteToSpeakerphone:YES];
        [_engine setClientRole:AgoraClientRoleBroadcaster];
        _offMic = YES;
        
        
        // 设置日志
        // 将日志输出等级设置为 AgoraLogFilterDebug
        // iOS：App Sandbox/Library/caches/agorasdk.log
//        [_engine setLogFilter: AgoraLogFilterDebug];
//        [_engine setLogFileSize:1024*1024*100];
//        // 获取当前目录
//        NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
//        // 获取文件路径
//        // 获取时间戳
//        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
//        [formatter setDateFormat:@"ddMMyyyyHHmm"];
//        NSDate *currentDate = [NSDate date];
//        NSString *dateString = [formatter stringFromDate:currentDate];
//        NSString *logFilePath = [NSString stringWithFormat:@"%@/%@.log", [paths objectAtIndex:0], dateString];
//        // 设置日志文件的默认地址
//        [_engine setLogFile:logFilePath];
    }
    return self;
}

//- (void)reset {
//    DDLogInfo(@"%s", __FUNCTION__);
//
//    if (!_engine) return;
//
//    [AgoraRtcEngineKit destroy];
//
//    _microphoneEnabled = YES;
//    _role = _broadcaster ? AgoraClientRoleBroadcaster : AgoraClientRoleAudience;
//    _muteLocalAudioStream = _disableBroadcast || _offMic;
//
////    _joinedUid = [NSMutableArray new];
////    _joinedUser = [NSMutableDictionary new];
//
//    _engine = [AgoraRtcEngineKit sharedEngineWithAppId:_appid delegate:self];
//    //        [_engine enableDualStreamMode:YES];
//    [_engine setChannelProfile:AgoraChannelProfileLiveBroadcasting];
//    [_engine setVideoEncoderConfiguration:[[AgoraVideoEncoderConfiguration alloc]initWithSize:AgoraVideoDimension640x360
//                                                                                    frameRate:AgoraVideoFrameRateFps15
//                                                                                      bitrate:AgoraVideoBitrateStandard
//                                                                              orientationMode:AgoraVideoOutputOrientationModeAdaptative]];
//    [_engine setClientRole:AgoraClientRoleBroadcaster];
//    [_engine enableVideo];
//
//    [_engine setDefaultAudioRouteToSpeakerphone:YES];
//}


- (BOOL)joinRoom:(NSString*)roomId withUID:(NSUInteger)uid success:(nullable void(^)(AgoraRtcEngineManager*sender))success {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(uid), roomId);
    
    if (!_engine) return NO;
//
//    if (!roomId.length) return NO;
//
//    if (self.joinRoomId) return NO;
//    if (self.roomId) return NO;
//    if (self.leaveRoomId) return NO;
//
    void(^joinSuccessBlock)(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) = nil;
//    if (success) {
//        __weak typeof (self) wself = self;
//        joinSuccessBlock = ^(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed) {
////            if ([wself.joinRoomId isEqualToString:channel]) {
////                wself.roomId = channel;
////                wself.uid = uid;
////                wself.joinRoomId = nil;
////                success (wself);
////            }
//        };
//    }
#pragma mark 到这一步 需要把本地视频图像传递进来，然后监听远程视频图像  还有一个问题，视频图像加在哪里？ 怎么联动？
    
//    [_engine setParameters:@"{\"che.audio.chorus.mode\":true}"];
//    [_engine setParameters:@"{\"che.audio.specify.codec\":\"OPUSFB\"}"];
//    _packetProcessing = [[AgoraPacketProcessing alloc] init];
//    _packetProcessing.delegate = self;
    
    // Music
//    [_engine setParameters:@"{\"che.audio.chorus.mode\":true}"];
//    [_engine setParameters:@"{\"che.audio.specify.codec\":\"OPUSFB\"}"];
//    _packetProcessing = [[AgoraPacketProcessing alloc] init];
//    _packetProcessing.delegate = self;
//    [_packetProcessing registerPacketProcessing:_engine];
    
    if ([_engine joinChannelByToken:nil channelId:roomId info:nil uid:uid joinSuccess:joinSuccessBlock] == 0) {
//        self.joinRoomId = roomId;
//        self.joinRoomUid = uid;
        return YES;
    }
    return NO;
}

- (BOOL)leaveRoom:(nullable void(^)(AgoraRtcEngineManager*sender))success {
//    DDLogInfo(@"%s", __FUNCTION__);
    
    if (!_engine) return NO;
//
//    if (self.joinRoomId) return NO;
//    if (!self.roomId) return NO;
//    if (self.leaveRoomId) return NO;
//
    void(^leaveChannelBlock)(AgoraChannelStats * _Nonnull stat) = nil;
//    if (success) {
//        __weak typeof (self) wself = self;
//        leaveChannelBlock = ^(AgoraChannelStats * _Nonnull stat) {
//            if (wself.leaveRoomId) {
//                wself.leaveRoomId = nil;
//                success (wself);
//            }
//        };
//    }
    
    if ([_engine leaveChannel:leaveChannelBlock] == 0) {
//        self.leaveRoomId = self.roomId;
//        self.roomId = nil;
//        self.uid = _joinRoomUid;
//        [_packetProcessing deregisterPacketProcessing:_engine];
//        _packetProcessing = nil;
        return YES;
    }
    return NO;
}

// 暂停声网
- (void)pauseAgoraRtcEngineRoom {
    [self.engine setClientRole:AgoraClientRoleAudience];
}
// 恢复声网
- (void)resumeAgoraRtcEngineRoom {
    [self.engine setClientRole:AgoraClientRoleBroadcaster];
}

//- (void)setDisableBroadcast:(BOOL)is {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(is));
//    self.muteLocalAudioStream = is || _offMic;
//    _disableBroadcast = is;
//}
//
//- (void)setBroadcaster:(BOOL)is {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(is));
//    self.role = is ? AgoraClientRoleBroadcaster : AgoraClientRoleAudience;
//    _broadcaster = is;
//}

- (void)setOffMic:(BOOL)offMic {
    if ([_engine muteLocalAudioStream:offMic] == 0) {
        _offMic = offMic;
    } else {
        _offMic = !offMic;
    }
}

#pragma mark - internal method
//
//- (void)setRole:(AgoraClientRole)role {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(role));
////
////    if (_role == role) return;
////    
////    _role = role;
////
////    if (_roleRequest) return;
//
//    if ([_engine setClientRole:_role] == 0) {
//        self.roleRequest = @(_role);
//    }
//}
//
//- (void)setMicrophoneEnabled:(BOOL)enable {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(enable));
//
//    if (_microphoneEnabled == enable) return;
//
//    _microphoneEnabled = enable;
//
//    if (self.microphoneEnabledRequest) return;
//
//    int agoraCallRet = [_engine enableLocalAudio:_microphoneEnabled];
//    if (agoraCallRet == 0) {
//        self.microphoneEnabledRequest = @(_microphoneEnabled);
//    }
//}

//- (void)setMuteLocalAudioStream:(BOOL)mute {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(mute));
//
//    if (_muteLocalAudioStream == mute) return;
//
//    _muteLocalAudioStream = mute;
//
//    if (_role == AgoraClientRoleBroadcaster) {
//        [_engine muteLocalAudioStream:_muteLocalAudioStream];
//    }
//}

#pragma mark - AgoraRtcEngineDelegate
- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurWarning:(AgoraWarningCode)warningCode {
//    DDLogWarn(@"%s %@", __FUNCTION__, @(warningCode));
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurError:(AgoraErrorCode)errorCode {
//    DDLogError(@"%s %@", __FUNCTION__, @(errorCode));
    
    //    [self reset];
//
//    self.roleRequest = nil;
//    self.microphoneEnabledRequest = nil;
//
//    if (_joinRoomId) {
//        [_engine joinChannelByToken:nil channelId:_joinRoomId info:nil uid:_joinRoomUid joinSuccess:nil];
//    }
//    else if (_leaveRoomId) {
//        self.leaveRoomId = nil;
//    if (errorCode == 17) { // 已在房间，无需再进
//        return;
//    } else {
//        if (_delegate && [_delegate respondsToSelector:@selector(agoraRtcEngineManagerDidLeaveRoom:)]) {
//            [_delegate agoraRtcEngineManagerDidLeaveRoom:self];
//        }
//    }
    
//    }
//    else if (_roomId) {
//        [_engine joinChannelByToken:nil channelId:_joinRoomId info:nil uid:_joinRoomUid joinSuccess:nil];
//    }
}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didApiCallExecute:(NSInteger)error api:(NSString * _Nonnull)api result:(NSString * _Nonnull)result {
//    //    DDLogInfo(@"%s %@ %@ %@", __FUNCTION__, @(error), api, result);
//}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didJoinChannel:(NSString * _Nonnull)channel withUid:(NSUInteger)uid elapsed:(NSInteger) elapsed {
//    DDLogInfo(@"%s %@ %@ %@", __FUNCTION__, channel, @(uid), @(elapsed));
    if (_delegate && [_delegate respondsToSelector:@selector(agoraRtcEngineManagerDidJoinRoom:)]) {
        [_delegate agoraRtcEngineManagerDidJoinRoom:self];
    }
    
//
//    if ([self.joinRoomId isEqualToString:channel]) {
//        self.roomId = channel;
//        self.uid = uid;
//        self.joinRoomId = nil;
//
//    }
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didRejoinChannel:(NSString * _Nonnull)channel withUid:(NSUInteger)uid elapsed:(NSInteger) elapsed {
//    DDLogInfo(@"%s %@ %@ %@", __FUNCTION__, channel, @(uid), @(elapsed));
//    if ([self.roomId isEqualToString:channel]) {
//        self.roomId = channel;
//        self.uid = uid;
//    }
//    else if (_leaveRoomId) {
//        [_engine leaveChannel:nil];
//    }
}

- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didLeaveChannelWithStats:(AgoraChannelStats * _Nonnull)stats {
    //    DDLogInfo(@"%s %@", __FUNCTION__, [stats yy_modelToJSONString]);
//    DDLogInfo(@"%s %@", __FUNCTION__, [stats modelToJSONString]);
    
    if (_delegate && [_delegate respondsToSelector:@selector(agoraRtcEngineManagerDidLeaveRoom:)]) {
        [_delegate agoraRtcEngineManagerDidLeaveRoom:self];
    }
}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didClientRoleChanged:(AgoraClientRole)oldRole newRole:(AgoraClientRole)newRole {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(oldRole), @(newRole));
//    if (newRole == _role) {
//        self.roleRequest = nil;
//        if (newRole == AgoraClientRoleBroadcaster) {
//            [_engine muteLocalAudioStream:_muteLocalAudioStream];
//        }
//    }
//    else {
//        [_engine setClientRole:_role];
//    }
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didJoinedOfUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(uid), @(elapsed));
//    AgoraRtcEngineUser * user = [AgoraRtcEngineUser new];
//    user.uid = uid;
//    user.audioMuted = YES;
////    [_joinedUser setObject:user forKey:@(uid)];
////    [[self mutableArrayValueForKey:@"joinedUid"] addObject:@(uid)];
//
//    //这里获取GameSeatView的视频view， 然后播放  根据UID获取 room里面的indexpath，
//    //[self.room.users setObject:user forKey:user.UserId];  取到ModelGameUser 这里面有indexpath
//
//    [[[ModelClient instance].listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//        if ([obj respondsToSelector:@selector(modelClient:didRemoteVideoDecodedOfUid:)]) {
//            [obj modelClient:[ModelClient instance] didRemoteVideoDecodedOfUid:[NSNumber numberWithInteger:uid]];
//        }
//    }];
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOfflineOfUid:(NSUInteger)uid reason:(AgoraUserOfflineReason)reason {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(uid), @(reason));

//    [_joinedUser removeObjectForKey:@(uid)];
//    [[self mutableArrayValueForKey:@"joinedUid"] removeObject:@(uid)];
    //didAgoraRtcOfflineOfUid
//
//    [[[ModelClient instance].listener allObjects] enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//        if ([obj respondsToSelector:@selector(modelClient:didAgoraRtcOfflineOfUid:)]) {
//            [obj modelClient:[ModelClient instance] didAgoraRtcOfflineOfUid:[NSNumber numberWithInteger:uid]];
//        }
//    }];
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine connectionChangedToState:(AgoraConnectionStateType)state reason:(AgoraConnectionChangedReason)reason {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(state), @(reason));
//}
//
//- (void)rtcEngineConnectionDidLost:(AgoraRtcEngineKit * _Nonnull)engine {
//    DDLogInfo(@"%s", __FUNCTION__);
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine tokenPrivilegeWillExpire:(NSString *_Nonnull)token {
//    DDLogInfo(@"%s %@", __FUNCTION__, token);
//}
//
//- (void)rtcEngineRequestToken:(AgoraRtcEngineKit * _Nonnull)engine {
//    DDLogInfo(@"%s", __FUNCTION__);
//}

#pragma mark Media Delegate Methods
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didMicrophoneEnabled:(BOOL)enabled {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(enabled));
//    self.microphoneEnabledRequest = nil;
//    if (enabled != _microphoneEnabled) {
//        if ([_engine enableLocalAudio:_microphoneEnabled] == 0) {
//            self.microphoneEnabledRequest = @(_microphoneEnabled);
//        }
//    }
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine reportAudioVolumeIndicationOfSpeakers:(NSArray<AgoraRtcAudioVolumeInfo *> * _Nonnull)speakers totalVolume:(NSInteger)totalVolume {
//    //    这个日志输出太频繁了
//    //    DDLogInfo(@"%s %@ %@", __FUNCTION__, [speakers modelToJSONString], @(totalVolume));
//    [speakers enumerateObjectsUsingBlock:^(AgoraRtcAudioVolumeInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
////        if (obj.uid != 0) {
//////            [self.joinedUser objectForKey:@(obj.uid)].volume = obj.volume;
////        } else {
////            self.volume = obj.volume;
////        }
//        if (obj.volume > 1) {
//            if (self.delegate && [self.delegate respondsToSelector:@selector(agoraRtcEngineManager:didReceiveUser:audioVolumeIndication:)]) {
//                NSUInteger uid = obj.uid;
//                if (uid == 0) {
//                    uid = self.uid;
//                }
//                [self.delegate agoraRtcEngineManager:self didReceiveUser:@(uid) audioVolumeIndication:@(obj.volume)];
//            }
//        }
//    }];
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine activeSpeaker:(NSUInteger)speakerUid {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(speakerUid));
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine firstLocalAudioFrame:(NSInteger)elapsed {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(elapsed));
//
//}

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine firstRemoteAudioFrameOfUid:(NSUInteger)uid elapsed:(NSInteger)elapsed {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(uid), @(elapsed));
//
//    [_joinedUser objectForKey:@(uid)].audioMuted = NO;
//}

//#if 0
//
//- (void)rtcEngineVideoDidStop:(AgoraRtcEngineKit * _Nonnull)engine {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine firstLocalVideoFrameWithSize:(CGSize)size elapsed:(NSInteger)elapsed {
//
//}
//#pragma mark 接收到远程视频图像代理
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine firstRemoteVideoDecodedOfUid:(NSUInteger)uid size:(CGSize)size elapsed:(NSInteger)elapsed {
//    //可以把数据 回调到关联上gameView去吧
//
//    DDLogInfo(@"firstRemoteVideoDecodedOfUid");
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine firstRemoteVideoFrameOfUid:(NSUInteger)uid size:(CGSize)size elapsed:(NSInteger)elapsed {
//
//}
//
//#endif

//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didAudioMuted:(BOOL)muted byUid:(NSUInteger)uid {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(muted), @(uid));
//
//    [_joinedUser objectForKey:@(uid)].audioMuted = muted;
//}

//#if 0
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didVideoMuted:(BOOL)muted byUid:(NSUInteger)uid {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didVideoEnabled:(BOOL)enabled byUid:(NSUInteger)uid {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didLocalVideoEnabled:(BOOL)enabled byUid:(NSUInteger)uid {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine videoSizeChangedOfUid:(NSUInteger)uid size:(CGSize)size rotation:(NSInteger)rotation {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine remoteVideoStateChangedOfUid:(NSUInteger)uid state:(AgoraVideoRemoteState)state {
//
//}
//
//#endif
//
//#pragma mark Fallback Delegate Methods
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didLocalPublishFallbackToAudioOnly:(BOOL)isFallbackOrRecover {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(isFallbackOrRecover));
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didRemoteSubscribeFallbackToAudioOnly:(BOOL)isFallbackOrRecover byUid:(NSUInteger)uid {
//    DDLogInfo(@"%s %@ %@", __FUNCTION__, @(isFallbackOrRecover), @(uid));
//}
//
//
//#pragma mark Device Delegate Methods
//
//#if (!(TARGET_OS_IPHONE) && (TARGET_OS_MAC))
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine device:(NSString * _Nonnull)deviceId type:(AgoraMediaDeviceType)deviceType stateChanged:(NSInteger) state {
//
//}
//
//#endif
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didAudioRouteChanged:(AgoraAudioOutputRouting)routing {
//    DDLogInfo(@"%s %@", __FUNCTION__, @(routing));
//}
//
//#if 0
//
//- (void)rtcEngineCameraDidReady:(AgoraRtcEngineKit * _Nonnull)engine {
//    DDLogInfo(@"%s", __FUNCTION__);
//}
//
//#if TARGET_OS_IPHONE
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine cameraFocusDidChangedToRect:(CGRect)rect {
//
//}
//
//#endif
//
//#if TARGET_OS_IPHONE
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine cameraExposureDidChangedToRect:(CGRect)rect {
//
//}
//
//#endif
//
//#endif
//
//
//#pragma mark Statistics Delegate Methods
//
//#if 0
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine reportRtcStats:(AgoraChannelStats * _Nonnull)stats {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine lastmileQuality:(AgoraNetworkQuality)quality {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine networkQuality:(NSUInteger)uid txQuality:(AgoraNetworkQuality)txQuality rxQuality:(AgoraNetworkQuality)rxQuality {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine lastmileProbeTestResult:(AgoraLastmileProbeResult * _Nonnull)result {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine localVideoStats:(AgoraRtcLocalVideoStats * _Nonnull)stats {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine remoteVideoStats:(AgoraRtcRemoteVideoStats * _Nonnull)stats {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine remoteAudioStats:(AgoraRtcRemoteAudioStats * _Nonnull)stats {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine audioTransportStatsOfUid:(NSUInteger)uid delay:(NSUInteger)delay lost:(NSUInteger)lost rxKBitRate:(NSUInteger)rxKBitRate {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine videoTransportStatsOfUid:(NSUInteger)uid delay:(NSUInteger)delay lost:(NSUInteger)lost rxKBitRate:(NSUInteger)rxKBitRate {
//
//}
//
//#endif
//
//#pragma mark Audio Player Delegate Methods
//
//#if 0
//
//- (void)rtcEngineLocalAudioMixingDidFinish:(AgoraRtcEngineKit * _Nonnull)engine {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine localAudioMixingStateDidChanged:(AgoraAudioMixingStateCode)state errorCode:(AgoraAudioMixingErrorCode)errorCode {
//
//}
//
//- (void)rtcEngineRemoteAudioMixingDidStart:(AgoraRtcEngineKit * _Nonnull)engine {
//
//}
//
//- (void)rtcEngineRemoteAudioMixingDidFinish:(AgoraRtcEngineKit * _Nonnull)engine {
//
//}
//
//- (void)rtcEngineDidAudioEffectFinish:(AgoraRtcEngineKit * _Nonnull)engine soundId:(NSInteger)soundId {
//
//}
//
//#endif
//
//#pragma mark CDN Publisher Delegate Methods
//
//#if 0
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine streamPublishedWithUrl:(NSString * _Nonnull)url errorCode:(AgoraErrorCode)errorCode {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine streamUnpublishedWithUrl:(NSString * _Nonnull)url {
//
//}
//
//- (void)rtcEngineTranscodingUpdated:(AgoraRtcEngineKit * _Nonnull)engine {
//
//}
//
//#endif
//
//#pragma mark Inject Stream URL Delegate Methods
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine streamInjectedStatusOfUrl:(NSString * _Nonnull)url uid:(NSUInteger)uid status:(AgoraInjectStreamStatus)status {
//    DDLogInfo(@"%s %@ %@ %@", __FUNCTION__, url, @(uid), @(status));
//}
//
//#pragma mark Stream Message Delegate Methods
//
//#if 0
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine receiveStreamMessageFromUid:(NSUInteger)uid streamId:(NSInteger)streamId data:(NSData * _Nonnull)data {
//
//}
//
//- (void)rtcEngine:(AgoraRtcEngineKit * _Nonnull)engine didOccurStreamMessageErrorFromUid:(NSUInteger)uid streamId:(NSInteger)streamId error:(NSInteger)error missed:(NSInteger)missed cached:(NSInteger)cached {
//
//}
//
//#endif
//
//
//#pragma mark Miscellaneous Delegate Methods

//- (void)rtcEngineMediaEngineDidLoaded:(AgoraRtcEngineKit * _Nonnull)engine {
//    DDLogInfo(@"%s", __FUNCTION__);
//}
//
//- (void)rtcEngineMediaEngineDidStartCall:(AgoraRtcEngineKit * _Nonnull)engine {
//    DDLogInfo(@"%s", __FUNCTION__);
//}

#pragma mark - AgoraPacketProcessingDelegate
//- (void)packetProcessing:(AgoraPacketProcessing *)packetProcessing didReceivedAudioExternalData:(NSData *)data {
//    if (_delegate && [_delegate respondsToSelector:@selector(agoraRtcEngineManager:didReceivedAudioExternalData:)]) {
//        [_delegate agoraRtcEngineManager:self didReceivedAudioExternalData:data];
//
//    }
//}

@end
