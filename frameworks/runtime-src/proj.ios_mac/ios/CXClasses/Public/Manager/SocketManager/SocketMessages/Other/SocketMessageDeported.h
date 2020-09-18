//
//  SocketMessageDeported.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/23.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageDeported : SocketMessageNotification

@property (nonatomic, strong) NSNumber * UserId;
@property (nonatomic, strong) NSString * NickName;
@property (nonatomic, strong) NSString * Code;

// 退出房间回调
@property (nonatomic, strong) NSNumber * LeaveRoomCode;
@property (nonatomic, strong) NSString * LeaveRoomMsg;

// 通知客户端提示语
@property (nonatomic, strong) NSString * Msg;

@end

NS_ASSUME_NONNULL_END
