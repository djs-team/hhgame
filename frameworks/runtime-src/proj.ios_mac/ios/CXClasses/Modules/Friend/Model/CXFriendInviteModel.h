//
//  CXFriendInviteModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/6.
//

#import <Foundation/Foundation.h>
#import <HyphenateLite/HyphenateLite.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXFriendGiftModel : NSObject

@property (nonatomic, strong) NSString * animation;
@property (nonatomic, strong) NSString * gift_coin;
@property (nonatomic, strong) NSString * gift_id;
@property (nonatomic, strong) NSString * gift_image;
@property (nonatomic, strong) NSString * gift_name;
@property (nonatomic, strong) NSString * gift_number;
@property (nonatomic, strong) NSNumber * num;

@property (nonatomic, strong) NSString * pack_num; // 礼物数量

@property (nonatomic, strong) NSString * gift_blue_coin; // 蓝玫瑰价值

// 蓝玫瑰兑换记录
@property (nonatomic, strong) NSString * gift_num; // 礼物兑换数量
@property (nonatomic, strong) NSString * time;
@property (nonatomic, strong) NSString * value; // 获得蓝玫瑰

@end

@interface CXFriendInviteModel : NSObject

@property (nonatomic, strong) NSString * apply_id;
@property (nonatomic, strong) NSString * user_id;
@property (nonatomic, strong) NSString * nickname;
@property (nonatomic, strong) NSString * room_id;
@property (nonatomic, strong) NSString * avatar;
@property (nonatomic, strong) NSString * sex; // 1男 2 女
@property (nonatomic, strong) NSString * city; // 省
@property (nonatomic, strong) NSString * city_two; // 市
@property (nonatomic, strong) NSString * intro;
@property (nonatomic, strong) NSString * online;
@property (nonatomic, strong) NSString * mstatus; //1.同意 2.拒绝 3.申请中
@property (nonatomic, strong) NSString * gift_id;
@property (nonatomic, strong) NSString * ctime;
@property (nonatomic, strong) NSString * ftime;
@property (nonatomic, strong) NSString * msg;
@property (nonatomic, strong) NSString * age;
@property (nonatomic, strong) NSString * gift_image;
@property (nonatomic, strong) NSString * is_friend;
@property (nonatomic, strong) NSString * lv_dengji;
@property (nonatomic, strong) NSString * stature; //身高

@property (nonatomic, strong) NSString *online_str; // 在线文字描述：在线、离线、刚刚在线
@property (nonatomic, strong) NSString *state; // 2/3相亲中 4/5热聊中 6开播中 其他没有

@property (nonatomic, strong) EMConversation *conversation;

@end

@interface CXSystemMessageModel : NSObject
@property (nonatomic, strong) NSString * messageId;
@property (nonatomic, strong) NSString * content;
@property (nonatomic, strong) NSString * time;

@end

NS_ASSUME_NONNULL_END
