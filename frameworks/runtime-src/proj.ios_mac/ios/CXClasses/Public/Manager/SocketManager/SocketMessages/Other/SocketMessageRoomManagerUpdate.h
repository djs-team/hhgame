//
//  SocketMessageRoomManagerUpdate.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


//{"MsgId":55,"IsManager":true,"UserId":1001537,"Name":"小提莫","UserLevel":1,"VipLevel":0,"RoomGuardLevel":0,"UserGuardList":[],"DukeLevel":0}
@interface SocketMessageRoomManagerUpdate : SocketMessageNotification

@property (nonatomic, strong) NSNumber * IsManager;
@property (nonatomic, strong) NSNumber * UserId;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;
@property (nonatomic, strong) NSNumber * RoomGuardLevel;
@property (nonatomic, strong) id UserGuardList;
@property (nonatomic, strong) NSNumber * DukeLevel;

@end

NS_ASSUME_NONNULL_END
