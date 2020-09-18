//
//  SocketMessageInvite.h
//  hairBall
//
//  Created by zyy on 2019/11/4.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageInvite : SocketMessageRequest

@property (nonatomic, strong) NSNumber * Free;
@property (nonatomic, strong) NSNumber * Level;
//@property NSNumber * TargetUserId;
@property (nonatomic, strong) NSArray * TargetUserIds;

@end

@interface SocketMessageUpMricoCost : SocketMessageResponse

@property (nonatomic, strong) NSNumber * Cost;

@end


@interface SocketMessageCost : SocketMessageRequest

@property (nonatomic, strong) SocketMessageUpMricoCost * response;

@end

@interface SocketMessageReplyInvite : SocketMessageRequest

@property (nonatomic, strong) NSNumber * Agree;// 1同意  0不同意
@property BOOL Force; // 是否跳过

@end


@interface SocketMessageInviteResponse : SocketMessageNotification

@property (nonatomic, strong) NSString * NickName;
@property (nonatomic, strong) NSNumber * Free;
@property (nonatomic, strong) NSNumber * Cost;
@property (nonatomic, strong) NSNumber * Level;

@end




NS_ASSUME_NONNULL_END
