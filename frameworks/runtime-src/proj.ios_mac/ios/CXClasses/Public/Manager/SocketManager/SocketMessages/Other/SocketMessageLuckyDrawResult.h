//
//  SocketMessageLuckyDrawResultReponse.h
//  hairBall
//
//  Created by shiwei on 2019/7/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"
@class SocketMessageLuckyDrawReponseGift;

NS_ASSUME_NONNULL_BEGIN


// {"MsgId":67,"UserId":1000933,"UserNickName":"用户_185****3727","UserLevel":1,"VipLevel":0,"RoomGuardLevel":0,"UserGuardList":[],"DukeLevel":0,"UserIdentity":0,"GiftId":0,"Count":0,"Gifts":[{"Id":222,"Count":1,"TotalCount":1}]}

@interface SocketMessageLuckyDrawResultGift : NSObject

@property (nonatomic, copy) NSString *Id;
@property (nonatomic, copy) NSString *Count;
@property (nonatomic, copy) NSString *TotalCount;

@end

@interface SocketMessageLuckyDrawResult : SocketMessage

@property (nonatomic, strong) NSNumber * DukeLevel;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSNumber * RoomGuardLevel;
@property (nonatomic, strong) NSArray * UserGuardList;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSNumber * UserIdentity;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;
@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) NSNumber * EggType; // 0为金蛋，1为银蛋
@property (nonatomic, strong) NSString * GuardSign;
@property (nonatomic, strong) NSNumber *GuardState; // 0不是主持守护，1是普通守护，2是榜一守护
@property (nonatomic, strong) NSString * GuardHeadImage; // 被守护头像（榜一用到的）

@property (nonatomic, copy) NSString *UserNickName;
@property (nonatomic, copy) NSString *GiftId;
@property (nonatomic, copy) NSString *Count;
@property (nonatomic, strong) SocketMessageLuckyDrawReponseGift *Gift;
//@property (nonatomic, strong) NSArray<SocketMessageLuckyDrawReponseGift *> * Gifts;

@end

@interface SocketMessageLuckyDrawResultArgs : SocketMessage

@property (nonatomic, strong) NSArray <SocketMessageLuckyDrawResult *>*args;

@end


NS_ASSUME_NONNULL_END
