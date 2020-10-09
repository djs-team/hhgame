//
//  CXLiveRoomModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/1.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, LiveRoomMicroInfoType) {
    LiveRoomMicroInfoTypeHost = 0, // 主持位
    LiveRoomMicroInfoTypeMan = 1, // 男嘉宾
    LiveRoomMicroInfoTypeWomen = 2, // 女嘉宾
    LiveRoomMicroInfoTypeNormal = 3, // 普通位
    LiveRoomMicroInfoTypeSofa = 4, // 沙发位
};

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRecommendModel : NSObject

@property(nonatomic, strong)  NSString * room_id; // age
@property(nonatomic, strong)  NSString * room_type; // 房间类型 5:相亲 6:交友
@property(nonatomic, strong)  NSString * avatar;
@property(nonatomic, strong)  NSString * nickname;
@property(nonatomic, strong)  NSString * stature; // 身高
@property(nonatomic, strong)  NSString * birthday;
@property(nonatomic, strong)  NSString * sex; // 1:男 2:女
@property(nonatomic, strong)  NSString * state; // 相亲房：1:相亲中 2:等待中
@property(nonatomic, strong)  NSString * age;

@end

@interface CXLiveRoomDataModel : NSObject

@property(nonatomic, strong) NSNumber * ExternVisitorNumbers;
@property(nonatomic, strong) NSNumber * GuildId;
@property(nonatomic, strong) NSNumber * Id;
@property(nonatomic, strong) NSNumber * IsFreeMicro;
@property(nonatomic, strong) NSNumber * IsLive;
@property(nonatomic, strong) NSNumber * IsOpenHeartValue;
@property(nonatomic, strong) NSString * MapId;
@property(nonatomic, strong) NSNumber * OwnerUserId;
@property(nonatomic, strong) NSString * RoomDesc;
@property(nonatomic, strong) NSString * RoomId;
@property(nonatomic, strong) NSString * RoomImage;
@property(nonatomic, strong) NSNumber * RoomLock;
@property(nonatomic, strong) NSString * RoomName;
@property(nonatomic, strong) NSNumber * RoomTags;
@property(nonatomic, strong) NSNumber * RoomType; // 5:相亲 6:交友 7:七人 8:8人
@property(nonatomic, strong) NSString * RoomWelcomes;
@property(nonatomic, strong) NSNumber * VisitorNumbers;
@property(nonatomic, strong) NSNumber * VisitorNum;

@property BOOL IsExclusiveRoom; // 是否是专属房
@property BOOL IsCloseCamera; // 是否允许音频上麦
@property BOOL IsOpenRedPacket; // 是否开启红包
@property BOOL IsOpenBreakEgg; // 是否开启砸蛋
@property BOOL IsOpenPickSong; // 是否开启点歌
@property BOOL IsOpenMediaLibrary; // 是否开启媒体库
@property BOOL IsOpenVideoFrame; // 是否开启视频框

@property (nonatomic, assign) CGFloat padding;
@property (nonatomic, assign) CGFloat seatsSizeHeight;
@property (nonatomic, assign) CGSize hostSize;
@property (nonatomic, assign) CGSize manSize;
@property (nonatomic, assign) CGSize womanSize;
@property (nonatomic, assign) CGSize normalSize;
@property (nonatomic, assign) CGSize sofaSize;

@property (nonatomic, assign) CGFloat music_seatsSizeHeight;
@property (nonatomic, assign) CGSize music_songerSize;
@property (nonatomic, assign) CGSize music_normalSize;


@property (nonatomic, assign) CGFloat regular_top;

@end

@class LiveRoomUser;
@interface LiveRoomMicroInfo : NSObject

@property (nonatomic, strong) NSNumber * DaojishiShichang;
@property (nonatomic, strong) NSString * DaojishiShijiandian;
@property (nonatomic, strong) NSDate * DaojishiEndDate;
@property (nonatomic, strong) NSNumber * IsDisabled;
@property (nonatomic, strong) NSNumber * IsLocked;
@property (nonatomic, assign) NSInteger Number;
@property (nonatomic, assign) LiveRoomMicroInfoType Type;
@property (nonatomic, strong) LiveRoomUser * modelUser;
@property (nonatomic, strong) NSNumber * XinDongZhi;
@property (nonatomic, strong) NSNumber * Rolse;
//表情玩法
@property (nonatomic, strong) NSString * EmoticonAnimationUrl;
@property (nonatomic, strong) NSString * ConnectionId;

@property (nonatomic, strong) NSArray <NSString *> * RoseRanks; // 麦位粉丝榜

// 是否静音
@property (nonatomic, assign) BOOL isMute;

- (NSIndexPath*)indexPath;

@end

@interface LiveRoomUser : NSObject

@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) LiveRoomMicroInfo * modelSeat;
@property (nonatomic, strong) NSNumber * UserIdentity;  //0 普通 1 管理员 2 房主
@property (nonatomic, strong) NSString * PrettyId;
@property (nonatomic, strong) NSString * PrettyAvatar;
@property (nonatomic, strong) NSNumber * Age;
@property (nonatomic, strong) NSNumber * Stature;
@property (nonatomic, strong) NSString * City;
@property (nonatomic, assign) NSInteger Sex; // 1:男 2:女
@property (nonatomic, strong) NSNumber * VipLevel;

- (NSString*)displayId;

@end

@interface LiveRoomMicroOrder : NSObject

@property (nonatomic, strong) LiveRoomUser * modelUser;
@property (strong , nonatomic) NSNumber * Sex;
@property (strong , nonatomic) NSNumber * Level;
@property (strong , nonatomic) NSNumber * Number;
@property (strong , nonatomic) NSNumber * Stature;
@property (strong , nonatomic) NSNumber * Age;
@property (copy , nonatomic) NSString * City;
@property (copy , nonatomic) NSString * Name;

@property (assign , nonatomic) BOOL isCanUpMicro; // 是否可以上麦

@end

@interface CXLiveRoomModel : NSObject

/// 接口返回
@property (nonatomic, strong) NSNumber * Balance; // 余额
@property (nonatomic, strong) NSNumber * IsDisableMsg;  //是否被禁言
@property (nonatomic, strong) NSNumber * WaitMicroLeftNumber; // 男
@property (nonatomic, strong) NSNumber * WaitMicroRightNumber; // 女
@property (nonatomic, strong) NSNumber * OnlineLeftNumber;
@property (nonatomic, strong) NSNumber * OnlineRightNumber;
@property (nonatomic, strong) NSString * ModeName;
@property (nonatomic, strong) NSArray<NSString*> * Ranks;
@property (nonatomic, strong) CXLiveRoomDataModel * RoomData;
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
@property (nonatomic, strong) NSString *rmb;
@property (nonatomic, strong) NSString *diamond;
@property (nonatomic, strong) NSString *iosflag;
@property (nonatomic, strong) NSString *chargeId; 

@property (nonatomic, strong) NSString * GuardSign; // 守护榜标签
@property BOOL IsRoomGuard; // 进房间是否播放进场特效的字段

/// 自定义属性
//HeatValue 初始化房间时候这个值由RoomData.ExternVisitorNumbers + RoomData.VisitorNumbers得到。之后由MsgId(54)直接更新
@property (nonatomic, strong) NSNumber * HeatValue;
@property (nonatomic, strong) NSMutableDictionary<NSIndexPath*, LiveRoomMicroInfo*> * seats; //房间里的麦位
//房间里用户信息
@property (nonatomic, strong) NSMutableDictionary<NSString*, LiveRoomUser*> * users;
//房间里男的排队
@property (nonatomic, strong) NSMutableDictionary<NSString*, LiveRoomMicroOrder*> * leftOrders; // 男/沙发/左
//房间里女的排队
@property (nonatomic, strong) NSMutableDictionary<NSString*, LiveRoomMicroOrder*> * rightOrders; // 女/右
//用户ID到麦位ID
@property (nonatomic, strong) NSMutableDictionary<NSString*, NSIndexPath*> * userSeats;
//麦位ID到用户ID
@property (nonatomic, strong) NSMutableDictionary<NSIndexPath*, NSString*> * seatUsers;

// YES：房间内 NO：房间外
@property (nonatomic, assign) BOOL isJoinedRoom;
// 自己是不是这个房间的房主
@property (nonatomic, assign) BOOL isHost;
//房间还没初始化完成收到的消息
@property (nonatomic, strong) NSMutableArray * roomMessages;

// 已申请/在线 数量
@property (nonatomic, strong) NSString * applyNumber_man;
@property (nonatomic, strong) NSString * applyNumber_woman;
@property (nonatomic, strong) NSString * onlineNumber_man;
@property (nonatomic, strong) NSString * onlineNumber_woman;

//跑马灯
@property (nonatomic, assign) float horseLampHeight;

@property (nonatomic, strong) SocketMessageUserJoinRoom * mineInfoInRoom;

// Music
@property (nonatomic, assign) BOOL isConsertModel; //  是否是伴唱模式
@property (nonatomic, assign) BOOL isSonger; // 是否是演唱者
@property (nonatomic, strong) NSMutableArray *music_reverseArrays;
@property (nonatomic, assign) NSInteger music_reverse_page;
@property (nonatomic, assign) NSInteger music_reverse_allPage;
@property (nonatomic, copy) NSString *music_songerId;

@property (nonatomic, strong) CXSocketMessageMusicModel * playing_NextSongInfo;
@property (nonatomic, strong) CXSocketMessageMusicModel * playing_SongInfo;

@property (nonatomic, assign) float music_Volume;


// 初始化
- (instancetype)initWithMessage:(SocketMessageRoomInit*)message;

- (nullable LiveRoomMicroInfo*)microInfoForUser:(NSString*)userId;
- (nullable LiveRoomMicroInfo*)microOrderForUser:(NSString*)userId;

@end

NS_ASSUME_NONNULL_END
