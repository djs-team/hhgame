//
//  AgoraRtcEngineManager.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <AgoraRtcKit/AgoraRtcEngineKit.h>

//@class AgoraRtcEngineUser;

NS_ASSUME_NONNULL_BEGIN


@class AgoraRtcEngineManager;

@protocol AgoraRtcEngineManagerDelegate <NSObject>

@optional
- (void)agoraRtcEngineManagerDidJoinRoom:(AgoraRtcEngineManager*)manager;
- (void)agoraRtcEngineManagerDidLeaveRoom:(AgoraRtcEngineManager*)manager;
//- (void)agoraRtcEngineManager:(AgoraRtcEngineManager*)manager didReceiveUser:(NSNumber*)uid audioVolumeIndication:(NSNumber*)volume;
//
//- (void)agoraRtcEngineManager:(AgoraRtcEngineManager*)manager didReceivedAudioExternalData:(NSData *)data;

@end

@interface AgoraRtcEngineManager : NSObject

@property (weak, nonatomic) id<AgoraRtcEngineManagerDelegate> delegate;

+ (instancetype)new NS_UNAVAILABLE;
- (instancetype)init NS_UNAVAILABLE;
+ (instancetype)instanceWithAppId:(NSString*)appid;
+ (void)destroy;

//引擎APPID
@property (weak, nonatomic, nullable) NSString *appid;

////正在加入的房间ID和用户ID
//@property (nonatomic, nullable, readonly) NSString * joinRoomId;
//@property (nonatomic, assign, readonly) NSUInteger joinRoomUid;
////正在离开的房间ID和用户ID
//@property (nonatomic, nullable, readonly) NSString * leaveRoomId;
////当前所在房间和用户ID
//@property (nonatomic, nullable, readonly) NSString * roomId;
//@property (nonatomic, assign, readonly) NSUInteger uid;
//@property (nonatomic, assign, readonly) NSInteger volume;
//用户
//@property (nonatomic, readonly) NSMutableArray<NSNumber*> * joinedUid;
//@property (nonatomic, readonly) NSMutableDictionary<NSNumber*, AgoraRtcEngineUser*> * joinedUser;

///这里自己写一个

@property (nonatomic, strong) AgoraRtcEngineKit * engine;

- (BOOL)joinRoom:(NSString*)roomId withUID:(NSUInteger)uid success:(nullable void(^)(NSString * _Nonnull channel, NSUInteger uid, NSInteger elapsed))success;
- (BOOL)leaveRoom:(nullable void(^)(AgoraRtcEngineManager*sender))success;

@property (nonatomic, assign, getter = isOffMic) BOOL offMic;  //关闭麦克风采集

@end

NS_ASSUME_NONNULL_END
