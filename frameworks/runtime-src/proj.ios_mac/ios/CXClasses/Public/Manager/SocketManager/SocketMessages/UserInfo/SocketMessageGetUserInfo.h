//
//  SocketMessageGetUserInfo.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageGetDisableMsgTemplatesDisableMsgData : NSObject

@property (nonatomic, assign) NSInteger Id;
@property (nonatomic, assign) NSInteger State;
@property (nonatomic, assign) NSInteger Time;

@end

@interface SocketMessageGetUserInfoResponseUser : NSObject

@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSNumber * Stature;
@property (nonatomic, strong) NSNumber * Age;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * PrettyId;
@property (nonatomic, strong) NSString * PrettyAvatar;

- (NSString*)displayId;

@end

@interface SocketMessageGetUserInfoResponse : SocketMessageResponse

@property (nonatomic, strong) SocketMessageGetUserInfoResponseUser * User;
@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * UserIdentity;
@property (nonatomic, strong) NSNumber * IsOnMicro;
@property (nonatomic, assign) BOOL IsBlock; // 是否被拉黑
@property (nonatomic, assign) BOOL IsDisableMsg; // 是否被禁言
@property (nonatomic, strong) NSNumber * IsDisabledMicro;
@property (nonatomic, strong) NSNumber * IsAttention;

@end

// 获取用户信息
@interface SocketMessageGetUserInfo : SocketMessageRequest

@property (nonatomic, nullable, strong) SocketMessageGetUserInfoResponse * response;

@property (nonatomic, strong) NSNumber * UserId;

@end

// 设置是否禁言
@interface SocketMessageSetUserIsDisableMsg : SocketMessageRequest

@property (nonatomic, strong) NSNumber * Id;
@property (nonatomic, assign) BOOL IsDisableMsg;

@end

// 禁言表数据获取
@interface SocketMessageGetDisableMsgTemplatesResponse : SocketMessageResponse

@property (nonatomic, strong) NSArray <SocketMessageGetDisableMsgTemplatesDisableMsgData*>* DisableMsgs;

@end
@interface SocketMessageGetDisableMsgTemplates : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGetDisableMsgTemplatesResponse* response;

@end

// 获取守护榜单（亲密值排序）
@class LiveRoomUser;
@interface CXLiveRoomGuardItemModel : NSObject
@property (nonatomic, strong) LiveRoomUser * UserInfo;
@property (nonatomic, strong) NSString * eadlineTime;
@property (nonatomic, strong) NSNumber * Days;
@property (nonatomic, strong) NSNumber * Intimacy;
@end

@interface SocketMessageGetGuardItemListResponse : SocketMessageResponse
@property (nonatomic, assign) BOOL IsGuard;
@property (nonatomic, strong) NSString * GuardHead;
@property (nonatomic, strong) NSString * GuardName;
@property (nonatomic, strong) NSNumber * UserCount;
@property (nonatomic, strong) NSNumber * AllPage;
@property (nonatomic, strong) NSArray <CXLiveRoomGuardItemModel*>* GuardItems;

@end
@interface SocketMessageGetGuardItemListRequest : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGetGuardItemListResponse* response;
@property (nonatomic, strong) NSNumber * UserId;
@property (nonatomic, assign) NSInteger Page;

@end

NS_ASSUME_NONNULL_END
