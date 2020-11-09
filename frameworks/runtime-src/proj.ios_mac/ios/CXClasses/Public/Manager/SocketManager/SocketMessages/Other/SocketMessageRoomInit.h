//
//  SocketMessageRoomInit.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageRoomInitMicroInfoUser : NSObject

@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSString * PrettyId;
@property (nonatomic, strong) NSString * PrettyAvatar;
@property (nonatomic, strong) NSString * Age;
@property (nonatomic, strong) NSString * Stature;
@property (nonatomic, strong) NSString * City;
@property (nonatomic, assign) NSInteger Sex;

@end

@interface SocketMessageRoomInitMicroInfo : NSObject

@property (nonatomic, strong) NSNumber * DaojishiShichang;
@property (nonatomic, strong) NSString * DaojishiShijiandian;
@property (nonatomic, strong) NSNumber * IsDisabled;
@property (nonatomic, strong) NSNumber * IsLocked;
@property (nonatomic, strong) NSNumber * Number;
@property (nonatomic, strong) NSNumber * Type;
@property (nonatomic, strong) SocketMessageRoomInitMicroInfoUser * User;
@property (nonatomic, strong) NSNumber * XinDongZhi;
@property (nonatomic, strong) NSNumber * Rolse;
@property (nonatomic, strong) NSArray <NSString *> * RoseRanks; // 麦位粉丝榜

@end

@interface SocketMessageRoomInitMicroOrder : NSObject

@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSNumber * Stature;
@property (nonatomic, strong) NSNumber * Age;
@property (nonatomic, strong) NSNumber * Number;
@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) SocketMessageRoomInitMicroInfoUser * User;

@end


@interface SocketMessageRoomInitRank : NSObject

@end


@interface SocketMessageRoomInitData : NSObject

@property (nonatomic, strong) NSNumber * ExternVisitorNumbers;
@property (nonatomic, strong) NSNumber * GuildId;
@property (nonatomic, strong) NSNumber * Id;
@property (nonatomic, strong) NSNumber * IsFreeMicro;
@property (nonatomic, strong) NSNumber * IsLive;
@property (nonatomic, strong) NSNumber * IsOpenHeartValue;
@property (nonatomic, strong) NSString * MapId;
@property (nonatomic, strong) NSNumber * OwnerUserId;
@property (nonatomic, strong) NSString * RoomDesc;
@property (nonatomic, strong) NSString * RoomId;
@property (nonatomic, strong) NSString * RoomImage;
@property (nonatomic, strong) NSNumber * RoomLock;
@property (nonatomic, strong) NSNumber * RoomModel;
@property (nonatomic, strong) NSString * RoomName;
@property (nonatomic, strong) NSNumber * RoomTags;
@property (nonatomic, strong) NSNumber * RoomType;
@property (nonatomic, strong) NSString * RoomWelcomes;
@property (nonatomic, strong) NSNumber * VisitorNumbers;

@property BOOL IsExclusiveRoom; // 是否是专属房
@property BOOL IsCloseCamera; // 是否允许音频上麦
@property BOOL IsOpenRedPacket; // 是否开启红包
@property BOOL IsOpenBreakEgg; // 是否开启砸蛋
@property BOOL IsOpenPickSong; // 是否开启点歌
@property BOOL IsOpenMediaLibrary; // 是否开启媒体库
@property BOOL IsOpenVideoFrame; // 是否开启视频框

@end


@interface SocketMessageRoomInit : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Balance;
@property (nonatomic, strong) NSNumber * IsDisableMsg;  //是否被禁言
@property (nonatomic, strong) NSArray<SocketMessageRoomInitMicroInfo*> * MicroInfos;

//那这里就应该要改了， 改成ManMicroOrders和WomenMicroOrders
@property (nonatomic, strong) NSArray<SocketMessageRoomInitMicroOrder*> * LeftMicroOrders; // 男/沙发/左
@property (nonatomic, strong) NSArray<SocketMessageRoomInitMicroOrder*> * RightMicroOrders; // 女/右
@property (nonatomic, strong) NSNumber * WaitMicroLeftNumber; // 男
@property (nonatomic, strong) NSNumber * WaitMicroRightNumber; // 女
@property (nonatomic, strong) NSNumber * OnlineLeftNumber;
@property (nonatomic, strong) NSNumber * OnlineRightNumber;

@property (nonatomic, strong) NSString * ModeName;
@property (nonatomic, strong) NSArray<NSString*> * Ranks;
@property (nonatomic, strong) SocketMessageRoomInitData * RoomData;
@property (nonatomic, strong) NSString * TagName;
@property (nonatomic, strong) NSString * Tips;
@property (nonatomic, strong) NSNumber * UserIdentity;   //0 普通 1 管理员 2 房主
@property (nonatomic, strong) NSString * RoomOwnerNickName;
@property (nonatomic, strong) NSString * RoomOwnerHeadUrl;
@property (nonatomic, strong) NSNumber * IsAttentionRoomOwner;
@property (nonatomic, strong) NSNumber * Hammers; // 锤子
@property (nonatomic, strong) NSNumber * CanBreakEgg; // 是否可以砸蛋

@property (nonatomic, strong) NSString * HuanxinRoomId;
@property (nonatomic, strong) NSString * ShengwangRoomId;


@property NSInteger SongCount; // 预约歌曲数量

@property BOOL CanSendGiftToEmcee;

@property BOOL IsHelpShare; // ture为助力分享，false为普通分享

@property (nonatomic, strong) NSNumber *OnMicroCost; // 邀请上麦需要的金额
@property (nonatomic, strong) NSNumber *WheatCardCount; // 上麦卡数量

@property BOOL IsFirstCharge; // 是不是首充

@property (nonatomic, strong) NSString * GuardSign; // 守护榜标签
@property BOOL IsRoomGuard; // 进房间是否播放进场特效的字段
@property (nonatomic, strong) NSNumber *GuardState; // 0不是主持守护，1是普通守护，2是榜一守护
@property (nonatomic, strong) NSString * GuardHeadImage; // 被守护头像（榜一用到的）

@end

NS_ASSUME_NONNULL_END
